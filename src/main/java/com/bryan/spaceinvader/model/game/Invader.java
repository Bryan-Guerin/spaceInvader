package com.bryan.spaceinvader.model.game;

public class Invader extends AbsInvader {

    public Invader(Position position, InvaderType invaderType) {
        super(position, invaderType);
        if (invaderType.isShooter())
            throw new UnsupportedOperationException("Shooter invader not supported");
    }

}