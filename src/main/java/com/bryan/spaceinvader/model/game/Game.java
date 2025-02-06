package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private boolean isPaused = false;

    private record Rect(int leftGap, int topGap, int x, int y, int width, int height) {
    }

    private boolean isShooting, isMovingLeft, isMovingRight;

    // Const of the rect around invaders and define different properties like gap, spacing and size
    // No idea how to name it better
    private final Rect rect;

//    TODO s'occuper de la difficulté pour créer la config de la partie et donc générer la vague

    private ArrayList<ArrayList<AbsInvader>> waves;
    private final ArrayList<InvaderShooter> shooters = new ArrayList<>();
    private List<Bullet> invaderBullets = Collections.synchronizedList(new ArrayList<>());
    private List<Bullet> playerBullets = Collections.synchronizedList(new ArrayList<>());

    public Game(Canvas canvas) {
        this.canvas = canvas;
        canvas.setWidth(1420);
        canvas.setHeight(1080);
        this.player = new Player((int) (canvas.getWidth() / 2));
        gameConfig = GameConfig.getGameConfig(settings.getDifficulty());
        rect = new Rect((int) ((canvas.getWidth() - gameConfig.getNumberOfColumns() * (90) + 45) / 2), 50, 90, 60, 45, 45);
        progress = new Progress();
        generateWaves(0);
        handlePlayerShooting();
    }

    private void handlePlayerShooting() {
        // TODO peaufiner en démarrant le thread sur le keyEvent et le stoppant pareil. Ainsi, il n'y a pas de décalage de phase lors du tir.
        //    PB de synchro sur le début du tire du joueur => Ne commence pas à tirer directement, mais seulement si dans le timing.
        //    Utiliser une autre méthode que le thread avec sleep. see : https://stackoverflow.com/questions/69974804/avoid-busy-waiting-warning-in-a-thread
        playerShootingThread = new Thread(() -> {
            while (true) {
                if (isShooting)
                    playerBullets.add(player.shoot());
                try {
                    Thread.sleep(player.getAttackDelay());
                } catch (InterruptedException e) {
                    logger.info("Player Shooting Thread interrupted");
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
        if (logger.isTraceEnabled())
            logger.trace("Key {} pressed", event.getCode());
        event.consume();
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
                    InvaderShooter invader = new InvaderShooter(new Position(rect.leftGap + rect.x * j, rect.topGap + rect.y * i), type);
                    shooters.add(invader);
                    line.add(invader);
                } else {
                    line.add(new Invader(new Position(rect.leftGap + rect.x * j, rect.topGap + rect.y * i), type));
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
            isPaused = true;
            //TODO faire la fin de jeu propre (recommencer niveau et perdre une vie. Ou perdre (sauvegarde score etc ...)
            logger.info("Life lost. Restarting game");
        }

        if (progress.isLevelCompleted())
            startNextLevel();
    }

    private void computeCollision() {
        // Player with border of canvas
        if (player.position.x < 0)
            player.position.x = 0;

        if (player.position.x > canvas.getWidth() - Player.WIDTH)
            player.position.x = (int) (canvas.getWidth() - Player.WIDTH);

        // Bullets out of canvas
        playerBullets.removeIf(bullet -> bullet.position.y < 0);
        invaderBullets.removeIf(bullet -> bullet.position.y > canvas.getHeight());

        // Invader with player
        playerBullets = playerBullets.stream()
                .map(bullet -> {
                    for (ArrayList<AbsInvader> wave : waves) {
                        for (int i = 0; i < wave.size(); i++) {
                            if (nonNull(wave.get(i)) && wave.get(i).position.isInRange(bullet.position, rect.width)) {
                                if (wave.get(i).takeDamage(bullet.damage)) { // If the invader is killed
                                    progress.decreaseTotalInvadersAlive();
                                    progress.addScore(wave.get(i).getType().getScore());
                                    int finalI = i;
                                    if (wave.get(i).getType().isShooter())
                                        shooters.removeIf(shooter -> shooter.equals(wave.get(finalI)));
                                    wave.set(i, null);
                                }
                                return null; // Remove the bullet
                            }
                        }
                    }
                    return bullet;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

        // Player with invaders
        invaderBullets = invaderBullets.stream()
                .map(bullet -> {
                    if (player.position.isInRange(bullet.position, Player.WIDTH)) {
                        player.takeDamage(bullet.damage);
                        return null;
                    }
                    return bullet;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

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
                Bullet.PlAYER_BULLET_TEXTURE,
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
                bullet.position.x,
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
        // TODO faire le passage au niveau suivant
        //  Puis la boutique et la fin de partie
        //  Faire barre de progression en % et ajouter barre de PV actuel du joueur
        //  Faire un affichage des vies restantes en image (pas en texte)
        if (!isPaused) {
            handleInputs();
            invaderShoot();
            bulletMove();
            computeCollision();
            computeProgress();
            render();
        }
    }

    public void startNextLevel() {
        progress.nextLevel();
        invaderBullets.clear();
        playerBullets.clear();
        generateWaves(progress.getCurrentLevel());
        isPaused = false;
    }

    public static void stopPlayerShootingThread() {
        if (nonNull(playerShootingThread))
            playerShootingThread.interrupt();
    }

    public Progress getProgress() {
        return progress;
    }

    public void save() {
        // TODO un jour peut être (suffit de save le current level, hp, score, money, etc ...)
        logger.info("Not supported yet");
    }
}
