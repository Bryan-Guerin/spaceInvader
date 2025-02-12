package com.bryan.spaceinvader.model.game;

public class Vector {
    final double dx, dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Vector add(Vector v) {
        return new Vector(dx + v.dx, dy + v.dy);
    }
}
