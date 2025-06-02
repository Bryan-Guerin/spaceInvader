package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.game.Position;

import java.util.Collection;

public class AbsInvaderBooster extends AbsInvader implements Booster {
    public AbsInvaderBooster(Position position, InvaderType type) {
        super(position, type);
    }

    @Override
    public void boost(Collection<AbsInvader> invadersToBoost) {
        this.state.boost(this, invadersToBoost);
    }
}
