package com.bryan.spaceinvader.model.game;

public abstract class AbsInvader {
    private static final double WIDTH_RATIO = 0.0234375; // Basically 45(default size) divide by 1920
    public static int SIZE = 45; // It's a square

    public Position position;
    public double health;
    public InvaderType type;

    public AbsInvader(Position position, InvaderType type) {
        this.position = position;
        this.type = type;
        this.health = type.getMaxHealth();
    }

    /**
     * Apply damage to the invader
     * @param damage damage to deal
     * @return true if the invader is dead
     */
    public boolean takeDamage(double damage) {
        this.health -= damage;
        return this.health <= 0;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.type.getMaxHealth())
            this.health = this.type.getMaxHealth();
    }

    public Position getPosition() {
        return position;
    }

    public double getHealth() {
        return health;
    }

    public InvaderType getType() {
        return type;
    }

    public void move(Vector vector) {
        position.move(vector);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbsInvader that = (AbsInvader) o;
        return health == that.health && type == that.type && position.equals(that.position);
    }

    public static void computeInvaderSize(double stageWidth) {
        SIZE = (int) (stageWidth * WIDTH_RATIO);
    }

    @Override
    public String toString() {
        return "AbsInvader{" +
                "position=" + position +
                ", type=" + type +
                '}';
    }
}
