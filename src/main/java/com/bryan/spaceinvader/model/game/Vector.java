package com.bryan.spaceinvader.model.game;

public class Vector {
    int dx, dy;

    public Vector(int x, int y) {
        this.dx = x;
        this.dy = y;
    }

    public Vector add(Vector v) {
        return new Vector(dx + v.dx, dy + v.dy);
    }

    public Vector multiply(int scalar) {
        return new Vector(dx * scalar, dy * scalar);
    }
}
