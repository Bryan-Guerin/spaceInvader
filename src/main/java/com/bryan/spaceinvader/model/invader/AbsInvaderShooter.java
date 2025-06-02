package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.game.Bullet;
import com.bryan.spaceinvader.model.game.Position;

import java.util.Collection;

public class AbsInvaderShooter extends AbsInvader implements Shooter {

    public static final int BULLET_SPEED = 3;

    public AbsInvaderShooter(Position position, InvaderType type) {
        super(position, type);
    }

    @Override
    public Collection<Bullet> shoot() {
        return state.shoot(this);
    }
}
