package com.bryan.spaceinvader.model.game;

public class Position {
    public double x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move(Vector v) {
        x += v.dx;
        y += v.dy;
    }

    public Position copy() {
        return new Position(x, y);
    }

    public boolean equals(Position p) {
        return p.x == x && p.y == y;
    }

    public boolean isInRange(Position p, int range) {
        return Math.abs(p.x - x) <= range && Math.abs(p.y - y) <= range;
    }
}
