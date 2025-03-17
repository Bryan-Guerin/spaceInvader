package com.bryan.spaceinvader.model.game;

public class Position {
    public double x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(double x, double y) {
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

    /**
     * The given position is the upper left corner of the square.
     * Check if the bullet is in the square of side a
     *
     * @param bullet bullet to check
     * @param a      side of the square
     * @return true if the bullet is in the square
     */
    public boolean isInSquarePerimeter(Bullet bullet, int a) {
        return bullet.position.y < y + a && bullet.position.y + Bullet.HEIGHT > y
                && bullet.position.x + Bullet.WIDTH > x && (bullet.position.x < x + a);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Position offset(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }
}
