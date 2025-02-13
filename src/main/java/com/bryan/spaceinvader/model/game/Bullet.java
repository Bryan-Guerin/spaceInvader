package com.bryan.spaceinvader.model.game;

public class Bullet {
    public static final String PLAYER_BULLET_TEXTURE = "bullet_player.png";
    public static final String INVADER_BULLET_TEXTURE = "bullet_invader.png";
    public static final int WIDTH = 5;
    public static final double HEIGHT = 18;

    public Position position;
    public Vector vector;
    public final double damage;

    public Bullet(Position position, Vector vector, double damage) {
        this.damage = damage;
        this.position = position.copy();
        this.vector = vector;
    }

    public void move() {
        position.move(vector);
    }
}