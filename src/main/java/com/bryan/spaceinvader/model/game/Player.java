package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {
    public static final int WIDTH = 45;

    // TODO retirer les static sur les trucs pas besoin et init dans le constructor
    private static final Settings settings = Settings.getInstance();
    private static final int POSITION_Y = 900;
    private static final int SPEED = 3;
    private static int BULLET_SPEED = -3;
    private static int ATTACK_SPEED = 3; // bullets per second
    private static final String[] vesselTextures = {"V0.png", "V1.png", "V2.png"};
    private final MediaPlayer mediaPlayer = new MediaPlayer(ResourceManager.loadResource("shoot.wav", Media.class, ResourceType.AUDIO));
    private int currentVessel = 0;
    private int bulletDamage = 1;
    private int health = 3;

    public Position position;

    public Player(int x) {
        this.position = new Position(x, POSITION_Y);
    }

    public Bullet shoot() {
        mediaPlayer.stop();
        mediaPlayer.setVolume(settings.getVolume() / 4); // This sound is too loud. Have to fix the source
        mediaPlayer.play();
        return new Bullet(position, new Vector(0, BULLET_SPEED), bulletDamage);
    }

    public void moveLeft() {
        position = position.add(new Vector(-SPEED, 0));
    }

    public void moveRight() {
        position = position.add(new Vector(SPEED, 0));
    }

    public void nextVessel() {
        if (currentVessel < vesselTextures.length - 1)
            currentVessel++;
    }

    public String getVesselTexture() {
        return vesselTextures[currentVessel];
    }

    public boolean isMaxVessel() {
        return currentVessel == vesselTextures.length - 1;
    }

    public int getAttackSpeed() {
        return ATTACK_SPEED;
    }

    public int getAttackDelay() {
        return 1000 / ATTACK_SPEED;
    }

    public void increaseAttackSpeed(int speed) {
        ATTACK_SPEED += speed;
    }

    public void decreaseAttackSpeed(int speed) {
        ATTACK_SPEED -= speed;
        if (ATTACK_SPEED < 1)
            ATTACK_SPEED = 1;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public void increaseBulletDamage(int damage) {
        bulletDamage += damage;
    }

    public void decreaseBulletDamage(int damage) {
        bulletDamage -= damage;
        if (bulletDamage < 1)
            bulletDamage = 1;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0)
            health = 0;
    }

    public boolean isDead() {
        return health == 0;
    }
}