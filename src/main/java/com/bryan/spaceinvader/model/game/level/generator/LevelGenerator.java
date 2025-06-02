package com.bryan.spaceinvader.model.game.level.generator;

import com.bryan.spaceinvader.model.invader.AbsInvader;

import java.util.ArrayList;

public interface LevelGenerator {
    ArrayList<ArrayList<AbsInvader>> generate();
}
