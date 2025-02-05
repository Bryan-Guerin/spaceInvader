package com.bryan.spaceinvader.model.game;

public class Bullet {
    public static final String PlAYER_BULLET_TEXTURE = "Bullet_player.png";
    public static final String INVADER_BULLET_TEXTURE = "Bullet_invader.png";

    public Position position;
    public Vector vector;
    public int damage;

    public Bullet(Position position, Vector vector, int damage) {
        this.damage = damage;
        this.position = position;
        this.vector = vector;
    }

    public void move() {
        position = position.add(vector);
    }
}