package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.game.Position;

import java.util.Collection;

public class AbsInvaderHealer extends AbsInvader implements Healer {

    public AbsInvaderHealer(Position position, InvaderType type) {
        super(position, type);
    }

    @Override
    public void heal(Collection<AbsInvader> invaders) {
        this.state.heal(this, invaders);
    }
}
