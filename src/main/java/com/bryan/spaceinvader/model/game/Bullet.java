package com.bryan.spaceinvader.model.game;

public class Bullet {
    public static final String PlAYER_BULLET_TEXTURE = "Bullet_player.png";
    public static final String INVADER_BULLET_TEXTURE = "Bullet_invader.png";

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