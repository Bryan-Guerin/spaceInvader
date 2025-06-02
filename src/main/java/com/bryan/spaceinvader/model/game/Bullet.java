package com.bryan.spaceinvader.model.game;

import javafx.scene.paint.Paint;

public class Bullet {
    public static final String PLAYER_BULLET_TEXTURE = "bullet_player.png";
    public static final String INVADER_BULLET_TEXTURE = "bullet_invader.png";
    public static final double WIDTH = 5;
    public static final double HEIGHT = 18;

    public Position position;
    public Vector vector;
    public final double damage;
    public Paint color;

    public Bullet(Position position, Vector vector, double damage, Paint color) {
        this.damage = damage;
        this.position = position.copy();
        this.vector = vector;
        this.color = color;
    }

    public void move() {
        position.move(vector);
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "position=" + position.toString() +
                ", vector=" + vector +
                ", damage=" + damage +
                '}';
    }
}