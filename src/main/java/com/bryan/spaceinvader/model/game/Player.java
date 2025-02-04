package com.bryan.spaceinvader.model.game;

public class Player {
    public static final int WIDTH = 45;

    // TODO retirer les static sur les trucs pas besoin et init dans le constructor
    private static final int POSITION_Y = 900;
    private static final int SPEED = 3;
    private static int BULLET_SPEED = -3;
    private static int ATTACK_SPEED = 3; // bullets per second
    private static final String[] vesselTextures = {"V0.png", "V1.png", "V2.png"};
    private int currentVessel = 0;

    public Position position;

    public Player(int x) {
        this.position = new Position(x, POSITION_Y);
    }

    public Bullet shoot() {
        return new Bullet(position, new Vector(0, BULLET_SPEED));
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
}