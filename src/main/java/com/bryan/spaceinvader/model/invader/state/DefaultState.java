package com.bryan.spaceinvader.model.invader.state;

import com.bryan.spaceinvader.model.game.Bullet;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.AbsInvaderShooter;

import java.util.Collection;
import java.util.List;

public class DefaultState implements InvaderState {
    @Override
    public Collection<Bullet> shoot(AbsInvaderShooter invader) {
        // Comportement par défaut pour tirer
        return List.of();
    }

    @Override
    public void heal(AbsInvader invader, Collection<AbsInvader> invaders) {
    }

    @Override
    public void boost(AbsInvader invader, Collection<AbsInvader> invaders) {
    }

}