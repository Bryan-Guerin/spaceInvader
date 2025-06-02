package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.game.Position;
import com.bryan.spaceinvader.model.game.Vector;
import com.bryan.spaceinvader.model.invader.state.InvaderState;

public abstract class AbsInvader {
    private static final double WIDTH_RATIO = 0.0234375; // Basically 45(default size) divide by 1920
    public static int SIZE = 45; // It's a square

    protected final InvaderState state;
    public Position position;
    public InvaderType type;
    private double health;
    protected boolean isShielded;
    protected double power;

    public AbsInvader(Position position, InvaderType type) {
        this.state = type.getState();
        this.position = position;
        this.type = type;
        this.health = type.getMaxHealth();
        this.isShielded = type.isDefaultShielded();
        this.power = type.getBasePower();
    }

    /**
     * Apply damage to the invader
     * @param damage damage to deal
     * @return true if the invader is dead
     */
    public boolean takeDamage(double damage) {
        if (isShielded) {
            isShielded = false;
            return false;
        }
        this.health -= damage;
        return this.health <= 0;
    }

    public void receiveHeal(int amount) {
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

    public boolean isShielded() {
        return isShielded;
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

    public void boost(double power) {
        this.power += power;
    }

    public double getPower() {
        return power;
    }

    public boolean isHealer() {
        return this instanceof Healer;
    }

    public boolean isBooster() {
        return this instanceof Booster;
    }

    public boolean isShooter() {
        return this instanceof Shooter;
    }

    public boolean isNormal() {
        return this instanceof Invader;
    }
}
