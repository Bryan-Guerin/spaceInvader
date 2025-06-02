package com.bryan.spaceinvader.model.invader.state;

import com.bryan.spaceinvader.model.invader.AbsInvader;

import java.util.Collection;

public class BoosterState extends DefaultState {

    @Override
    public void boost(AbsInvader booster, Collection<AbsInvader> invaders) {
        for (AbsInvader invaderToBoost : invaders) {
            invaderToBoost.boost(booster.getPower());
        }
    }

}
