package com.bryan.spaceinvader.model.game;

public abstract class AbsInvader {
    public Position position;
    public int health;
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
    public boolean takeDamage(int damage) {
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

    public int getHealth() {
        return health;
    }

    public InvaderType getType() {
        return type;
    }

    public void move(Vector vector) {
        position = position.add(vector);
    }
}
