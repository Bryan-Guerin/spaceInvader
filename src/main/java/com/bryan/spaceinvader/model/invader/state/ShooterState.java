package com.bryan.spaceinvader.model.invader.state;

import com.bryan.spaceinvader.model.game.Bullet;
import com.bryan.spaceinvader.model.game.Vector;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.AbsInvaderShooter;
import com.bryan.spaceinvader.model.invader.InvaderShooter;

import java.util.Collection;
import java.util.Collections;

public class ShooterState extends DefaultState {
    @Override
    public Collection<Bullet> shoot(AbsInvaderShooter shooter) {
        return Collections.singletonList(new Bullet(
                shooter.position.copy().offset((double) AbsInvader.SIZE / 2 - Bullet.WIDTH / 2, 0),
                new Vector(0, InvaderShooter.BULLET_SPEED), shooter.getPower(), shooter.type.getColor()));
    }
}