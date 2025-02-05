package com.bryan.spaceinvader.model.game;

public class InvaderShooter extends AbsInvader implements Shooter {

    public static final int BULLET_SPEED = 3; // TODO A déclarer dans le type ? pour varier en fonction du type, mais plus variable (boost)...
    private int damage;

    public InvaderShooter(Position position, InvaderType type) {
        super(position, type);
        if (!type.isShooter())
            throw new IllegalArgumentException("Invader type must be shooter");

        switch (type) {
            case SHOOTER -> damage = 1;
            case GUARDIAN_SHOOTER -> damage = 2;
        }
    }

    @Override
    public Bullet shoot() {
        return new Bullet(position, new Vector(0, BULLET_SPEED), damage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }
}
