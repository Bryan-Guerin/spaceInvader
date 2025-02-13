package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.player.Player;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Game {

    private final static Logger logger = LogManager.getLogger(Game.class);
    private static Thread playerShootingThread;

    private final Settings settings = Settings.getInstance();
    private final Canvas canvas;
    private final Player player;
    private final GameConfig gameConfig;
    private final Progress progress;
    private final VBox shopMenu;
    private final VBox pauseMenu;
    private boolean paused = false;
    private boolean debug = false;

    private record Rect(int leftGap, int topGap, int xGap, int yGap) {
        public HashMap<Direction, Integer> getDimensions(GameConfig gameConfig) {
            HashMap<Direction, Integer> dimensions = new HashMap<>(2);
            dimensions.put(Direction.HEIGHT, yGap * gameConfig.getNumberOfRows());
            dimensions.put(Direction.WIDTH, xGap * gameConfig.getNumberOfColumns());
            return dimensions;
        }

        public enum Direction {
            HEIGHT, WIDTH
        }
    }

    private boolean isShooting, isMovingLeft, isMovingRight;

    // Const of the rect around invaders and define different properties like gap, spacing and size
    // No idea how to name it better
    private final Rect rect;

//    TODO s'occuper de la difficulté pour créer la config de la partie et donc générer la vague

    private ArrayList<ArrayList<AbsInvader>> waves;
    private final ArrayList<InvaderShooter> shooters = new ArrayList<>();
    private final List<Bullet> invaderBullets = Collections.synchronizedList(new ArrayList<>());
    private final List<Bullet> playerBullets = Collections.synchronizedList(new ArrayList<>());

    public Game(Canvas canvas, VBox scoreBar, VBox healthBar, VBox shopMenu, VBox pauseMenu) {
        this.canvas = canvas;
        this.shopMenu = shopMenu;
        this.pauseMenu = pauseMenu;
        gameConfig = GameConfig.getGameConfig(settings.getDifficulty());
        rect = new Rect((int) ((canvas.getWidth() - gameConfig.getNumberOfColumns() * 90 + 45) / 2), 50, 90, 60);

        this.player = new Player((int) (canvas.getWidth() / 2), (int) (canvas.getHeight() - rect.yGap), healthBar);
        progress = new Progress(scoreBar);
        generateWaves(0);
        handlePlayerShooting();
    }

    private void handlePlayerShooting() {
        playerShootingThread = new Thread(() -> {
            while (true) {
                try {
                    if (isShooting) {
                        playerBullets.add(player.shoot());
                        Thread.sleep(player.getAttackDelay());
                    }
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    logger.info("Player Shooting Thread interrupted");
                } catch (Exception e) {
                    logger.error("Error in Player Shooting Thread", e);
                }
            }
        });
        playerShootingThread.setName("Player Shooting Thread");
        playerShootingThread.start();
    }

    public void keyPressed(KeyEvent event) {
        Settings.GameAction action = settings.getGameActionByKeyCode(event.getCode());
        if (nonNull(action))
            switch (action) {
                case SHOOT -> isShooting = true;
                case MOVE_LEFT -> isMovingLeft = true;
                case MOVE_RIGHT -> isMovingRight = true;
            }
        if (event.getCode().equals(KeyCode.P))
            killAllInvaders();
        if (event.getCode().equals(KeyCode.ESCAPE))
            handlePauseResume();
        if (logger.isTraceEnabled())
            logger.trace("Key {} pressed", event.getCode());
        event.consume();
    }

    private void killAllInvaders() {
        for (ArrayList<AbsInvader> wave : waves) {
            wave.forEach(invader -> {
                if (nonNull(invader))
                    progress.recordKill(invader.getType().getScore());
            });
        }
        waves.clear();
        shooters.clear();
    }

    public void keyReleased(KeyEvent event) {
        Settings.GameAction action = settings.getGameActionByKeyCode(event.getCode());
        if (nonNull(action))
            switch (action) {
                case SHOOT -> isShooting = false;
                case MOVE_LEFT -> isMovingLeft = false;
                case MOVE_RIGHT -> isMovingRight = false;
            }
        if (logger.isTraceEnabled())
            logger.trace("Key {} released", event.getCode());
        event.consume();
    }

    private void generateWaves(int level) {
        this.waves = new ArrayList<>();
        int totalInvaders = 0;

        for (int i = 0; i < gameConfig.getNumberOfRows(); i++) {
            ArrayList<AbsInvader> line = new ArrayList<>();
            for (int j = 0; j < gameConfig.getNumberOfColumns(); j++) {
                InvaderType type = generateInvaderType(i, j, level);
                if (isNull(type))
                    continue;
                if (type.isShooter()) {
                    InvaderShooter invader = new InvaderShooter(new Position(rect.leftGap + rect.xGap * j, rect.topGap + rect.yGap * i), type);
                    shooters.add(invader);
                    line.add(invader);
                } else {
                    line.add(new Invader(new Position(rect.leftGap + rect.xGap * j, rect.topGap + rect.yGap * i), type));
                }
                totalInvaders++;
            }
            waves.add(line);
        }
        progress.setTotalInvaders(totalInvaders);
    }

    private InvaderType generateInvaderType(int row, int column, int level) {
        // TODO compléter pour faire mieux avec tous les types en fonction des probas généré dans le gameConfig
        // Certainement placer ça dans une autre classe pour mieux l'organiser et permettre de le compléter en appelant d'autres classes
        if (row <= 1)
            return InvaderType.SHOOTER;
        double prob = Math.random();
        if (prob <= 0.35)
            return InvaderType.SOLDIER;
        if (prob <= 0.5)
            return InvaderType.SHOOTER;
        return null;
    }

    private void handleInputs() {
        if (isMovingLeft)
            player.moveLeft();

        if (isMovingRight)
            player.moveRight();
    }

    private void bulletMove() {
        invaderBullets.forEach(Bullet::move);
        playerBullets.forEach(Bullet::move);
    }

    private void invaderShoot() {
        shooters.forEach(invaderShooter -> {
            if (Math.random() < gameConfig.getInvaderShotProbability())
                invaderBullets.add(invaderShooter.shoot());
        });
    }

    private void computeProgress() {
        if (player.isDead()) {
            progress.loseLife();
            paused = true;
            //TODO faire la fin de jeu propre (recommencer niveau et perdre une vie. Ou perdre (sauvegarde score etc ...)
            logger.info("Life lost. Restarting level {}.", progress.getCurrentLevel());
            restartLevel();
        }

        if (progress.isLevelCompleted()) {
            logger.info("Level {} completed. Starting next level.", progress.getCurrentLevel());
            endCurrentLevel();
        }

        if (progress.isLevelCompleted())
            endCurrentLevel();
    }

    private void computeCollision() {
        // Player with border of canvas
        if (player.position.x < 0)
            player.position.x = 0;

        if (player.position.x > canvas.getWidth() - Player.SIZE)
            player.position.x = (int) (canvas.getWidth() - Player.SIZE);

        // Bullets out of canvas
        playerBullets.removeIf(bullet -> bullet.position.y < 0);
        invaderBullets.removeIf(bullet -> bullet.position.y > canvas.getHeight());

        int k = 0;
        // Player bullets hits invader
        while (k < playerBullets.size()) {
            if (nonNull(playerBullets.get(k))) {
                for (ArrayList<AbsInvader> wave : waves) {
                    for (int j = 0; j < wave.size(); j++) {
                        if (k > playerBullets.size() - 1)
                            break;
                        if (nonNull(wave.get(j)) && wave.get(j).position.isInSquarePerimeter(playerBullets.get(k), AbsInvader.SIZE)) {
                            if (wave.get(j).takeDamage(playerBullets.get(k).damage)) { // If the invader is killed
                                progress.recordKill(wave.get(j).getType().getScore());
                                int finalI = j;
                                if (wave.get(j).getType().isShooter())
                                    shooters.removeIf(shooter -> shooter.equals(wave.get(finalI)));
                                wave.set(j, null);
                            }
                            playerBullets.remove(k);
                        }
                    }
                }
            }
            k++;
        }

        // Invaders bullets hits player
        for (int i = 0; i < invaderBullets.size(); i++) {
            if (player.position.isInSquarePerimeter(invaderBullets.get(i), Player.SIZE)) {
                player.takeDamage(invaderBullets.get(i).damage);
                invaderBullets.set(i, null);
            }
        }

        invaderBullets.removeIf(Objects::isNull);
        playerBullets.removeIf(Objects::isNull);
    }

    private void render() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        renderInvaders();
        renderPlayer();
        renderBullets();
        canvas.getGraphicsContext2D().stroke();
    }

    private void renderBullets() {
        Image bulletImage = ResourceManager.loadResource(
                Bullet.PLAYER_BULLET_TEXTURE,
                Image.class, ResourceType.IMAGE);

        for (Bullet bullet : playerBullets) {
            drawBullet(bulletImage, bullet);
        }

        bulletImage = ResourceManager.loadResource(
                Bullet.INVADER_BULLET_TEXTURE,
                Image.class, ResourceType.IMAGE);

        for (Bullet bullet : invaderBullets) {
            drawBullet(bulletImage, bullet);
        }
    }

    private void drawBullet(Image bulletImage, Bullet bullet) {
        canvas.getGraphicsContext2D().drawImage(
                bulletImage,
                bullet.position.x + 20, // Offset equivalent to Player.WIDTH / 2 - bulletImage.getWidth() / 2. Same for invaders
                bullet.position.y);
    }

    private void renderInvaders() {
        for (ArrayList<AbsInvader> wave : waves) {
            for (AbsInvader invader : wave) {
                if (nonNull(invader))
                    drawInvader(invader);
            }
        }
    }

    private void drawInvader(AbsInvader invader) {
        if (debug) {
            canvas.getGraphicsContext2D().setFill(Color.RED);
            canvas.getGraphicsContext2D()
                    .fillRect(
                            invader.position.x,
                            invader.position.y,
                            AbsInvader.SIZE, AbsInvader.SIZE);
        }
        canvas.getGraphicsContext2D().drawImage(
                ResourceManager.loadResource(
                        invader.type.getTextureName(),
                        Image.class, ResourceType.IMAGE),
                invader.position.x,
                invader.position.y);
    }

    private void renderPlayer() {
        canvas.getGraphicsContext2D().drawImage(
                ResourceManager.loadResource(
                        player.getVesselTexture(),
                        Image.class, ResourceType.IMAGE),
                player.position.x,
                player.position.y);
    }

    public void play() {
        //  TODO la fin de partie
        if (!paused) {
            handleInputs();
            invaderShoot();
            bulletMove();
            computeCollision();
            computeProgress();
            render();
        }
    }

    private void endCurrentLevel() {
        resetKeyEvents();
        paused = true;
        clearBullets();
        progress.nextLevel();
        player.softReset();
        shopMenu.setVisible(true);
    }

    public void startNextLevel() {
        generateWaves(progress.getCurrentLevel());
        pauseMenu.setVisible(false);
        paused = false;
    }

    private void restartLevel() {
        clearBullets();
        generateWaves(progress.getCurrentLevel());
        player.softReset();
        paused = false;
    }

    public void handlePauseResume() {
        resetKeyEvents();
        if (paused) {
            paused = false;
            pauseMenu.setVisible(false);
        } else {
            paused = true;
            pauseMenu.setVisible(true);
        }
    }

    public void resetKeyEvents() {
        isMovingLeft = false;
        isMovingRight = false;
        isShooting = false;
    }

    private void clearBullets() {
        invaderBullets.clear();
        playerBullets.clear();
        shooters.clear();
    }

    public static void stopPlayerShootingThread() {
        if (nonNull(playerShootingThread))
            playerShootingThread.interrupt();
    }

    public Progress getProgress() {
        return progress;
    }

    public Player getPlayer() {
        return player;
    }

    public void save() {
        // TODO un jour peut être (suffit de save le current level, hp, score, money, etc ...)
        //  Peut être créer une classe Backup pour regrouper les trucs a sauvegarder et persister (ne pas oublier les boosts player)
        logger.info("Not supported yet");
    }
}
