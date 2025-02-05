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

    public boolean equals(Position p) {
        return p.x == x && p.y == y;
    }

    public boolean isInRange(Position p, int range) {
        return Math.abs(p.x - x) <= range && Math.abs(p.y - y) <= range;
    }
}
