package com.bryan.spaceinvader.model.game;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(Vector v) {
        return new Position(x + v.dx, y + v.dy);
    }
}
