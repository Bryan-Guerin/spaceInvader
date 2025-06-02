package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.game.Bullet;

import java.util.Collection;

public interface Shooter {
    Collection<Bullet> shoot();
}
