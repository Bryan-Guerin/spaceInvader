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

import static java.util.Objects.nonNull;

public class Game {

    private final static Logger logger = LogManager.getLogger(Game.class);
    private static Thread playerShootingThread;

    private final Settings settings = Settings.getInstance();
    private final Canvas canvas;
    private final Player player;
    private final GameConfig gameConfig;
    private Progress progress; // TODO

    private record Rect(int leftGap, int topGap, int x, int y, int width, int height) {
    }

    private boolean isShooting, isMovingLeft, isMovingRight;

    // Const of the rect around invaders and define different properties like gap, spacing and size
    // No idea how to name it better
    private final Rect rect;

//    TODO s'occuper de la difficulté pour créer la config de la partie et donc générer la vague

    private ArrayList<ArrayList<AbsInvader>> waves;
    private final List<Bullet> invaderBullets = Collections.synchronizedList(new ArrayList<>());
    private final List<Bullet> playerBullets = Collections.synchronizedList(new ArrayList<>());

    public Game(Canvas canvas) {
        this.canvas = canvas;
        canvas.setWidth(1420);
        canvas.setHeight(1080);
        this.player = new Player((int) (canvas.getWidth() / 2));
        gameConfig = GameConfig.getGameConfig(settings.getDifficulty());
        rect = new Rect((int) ((canvas.getWidth() - gameConfig.getNumberOfColumns() * (90) + 45) / 2), 50, 90, 60, 45, 45);
        generateWaves(0);
        handlePlayerShooting();
    }

    private void handlePlayerShooting() {
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

        for (int i = 0; i < gameConfig.getNumberOfRows(); i++) {
            ArrayList<AbsInvader> line = new ArrayList<>();
            for (int j = 0; j < gameConfig.getNumberOfColumns(); j++) {
                line.add(new Invader(new Position(rect.leftGap + rect.x * j, rect.topGap + rect.y * i), generateInvaderType()));
            }
            waves.add(line);
        }
    }

    private InvaderType generateInvaderType() {
        // TODO compléter pour faire mieux avec tous les types en fonction des probas généré dans le gameConfig
        return InvaderType.SOLDIER;
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

    }

    private void computeProgress() {

    }

    private void computeCollision() {
        // Player with border of canvas
        if (player.position.x < 0)
            player.position.x = 0;

        if (player.position.x > canvas.getWidth() - Player.WIDTH)
            player.position.x = (int) (canvas.getWidth() - Player.WIDTH);

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
        handleInputs();
        invaderShoot();
        bulletMove();
        computeCollision();
        computeProgress();
        render();
    }

    public static void stopPlayerShootingThread() {
        if (nonNull(playerShootingThread))
            playerShootingThread.interrupt();
    }
}
