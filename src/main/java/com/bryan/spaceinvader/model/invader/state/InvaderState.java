package com.bryan.spaceinvader.model.invader.state;

import com.bryan.spaceinvader.model.game.Bullet;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.AbsInvaderShooter;

import java.util.Collection;

public interface InvaderState {
    Collection<Bullet> shoot(AbsInvaderShooter invader);

    void heal(AbsInvader invader, Collection<AbsInvader> invaders);

    void boost(AbsInvader invader, Collection<AbsInvader> invaders);
}
