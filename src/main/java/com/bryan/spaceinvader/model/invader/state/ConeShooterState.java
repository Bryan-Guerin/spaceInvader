package com.bryan.spaceinvader.model.invader.state;

import com.bryan.spaceinvader.model.game.Bullet;
import com.bryan.spaceinvader.model.game.Position;
import com.bryan.spaceinvader.model.game.Vector;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.AbsInvaderShooter;
import com.bryan.spaceinvader.model.invader.InvaderShooter;

import java.util.Collection;
import java.util.List;

public class ConeShooterState extends DefaultState {

    /**
     * The pattern is : <br>
     * <pre>
     *        /|\
     *       / | \
     *      /  |  \
     * </pre>
     *
     * @param shooter The invader shooting
     * @return A collection of bullets
     */
    @Override
    public Collection<Bullet> shoot(AbsInvaderShooter shooter) {
        Position position = shooter.position.copy().offset((double) AbsInvader.SIZE / 2 - Bullet.WIDTH / 2, 0);
        return List.of(
                new Bullet(position, new Vector(-1, InvaderShooter.BULLET_SPEED), shooter.getPower(), shooter.type.getColor()),
                new Bullet(position.copy(), new Vector(0, InvaderShooter.BULLET_SPEED), shooter.getPower(), shooter.type.getColor()),
                new Bullet(position.copy(), new Vector(1, InvaderShooter.BULLET_SPEED), shooter.getPower(), shooter.type.getColor())
        );
    }
}
