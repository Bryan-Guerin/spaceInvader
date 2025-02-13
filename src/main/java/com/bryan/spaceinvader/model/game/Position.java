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

    public boolean isInSquarePerimeter(Bullet bullet, int a) {
        double left = x - a / 2.0;
        double right = x + a / 2.0;
        double top = y - a / 2.0;
        double bottom = y + a / 2.0;
        double bulletHalfWidth = Bullet.WIDTH / 2.0;
        double bulletHalfHeight = Bullet.HEIGHT / 2.0;

        return (bullet.position.x + bulletHalfWidth >= left &&
                bullet.position.x - bulletHalfWidth <= right &&
                bullet.position.y + bulletHalfHeight >= top &&
                bullet.position.y - bulletHalfHeight <= bottom);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
