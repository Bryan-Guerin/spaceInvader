package com.bryan.spaceinvader.model.game;

public class InvaderShooter extends AbsInvader implements Shooter {

    public static final int BULLET_SPEED = 3;

    public InvaderShooter(Position position, InvaderType type) {
        super(position, type);
        if (!type.isShooter())
            throw new IllegalArgumentException("Invader type must be shooter");
    }

    @Override
    public Bullet shoot() {
        return new Bullet(position, new Vector(0, BULLET_SPEED));
    }
}
