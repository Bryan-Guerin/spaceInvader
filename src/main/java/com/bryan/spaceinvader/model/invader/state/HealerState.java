package com.bryan.spaceinvader.model.invader.state;

import com.bryan.spaceinvader.model.invader.AbsInvader;

import java.util.Collection;

public class HealerState extends DefaultState {

    @Override
    public void heal(AbsInvader healer, Collection<AbsInvader> invaders) {
        for (AbsInvader invaderToHeal : invaders) {
            invaderToHeal.receiveHeal((int) healer.getPower());
        }
    }
}