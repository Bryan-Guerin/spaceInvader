package com.bryan.spaceinvader.model.game.level.generator;

import com.bryan.spaceinvader.model.game.GameConfig;
import com.bryan.spaceinvader.model.game.Position;
import com.bryan.spaceinvader.model.game.Rect;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.InvaderFactory;
import com.bryan.spaceinvader.model.invader.InvaderShooter;
import com.bryan.spaceinvader.model.invader.InvaderType;

import java.util.ArrayList;

import static java.util.Objects.isNull;

public class SimpleLevelGenerator implements LevelGenerator {

    protected final GameConfig gameConfig;
    protected final Rect rect;
    protected final int currentLevel;
    protected int totalInvaders;
    protected final ArrayList<ArrayList<AbsInvader>> waves = new ArrayList<>();
    protected final ArrayList<InvaderShooter> shooters = new ArrayList<>();

    public SimpleLevelGenerator(int currentLevel, GameConfig gameConfig, Rect rect) {
        this.currentLevel = currentLevel;
        this.gameConfig = gameConfig;
        this.rect = rect;
    }

    @Override
    public ArrayList<ArrayList<AbsInvader>> generate() {
        waves.clear();
        totalInvaders = 0;

        for (int i = 0; i < gameConfig.getNumberOfRows(); i++) {
            ArrayList<AbsInvader> line = new ArrayList<>();
            for (int j = 0; j < gameConfig.getNumberOfColumns(); j++) {
                AbsInvader invader;
                if (currentLevel % gameConfig.getBossRate() == 0) {
                    if (i != gameConfig.getNumberOfRows() / 2 || j != gameConfig.getNumberOfColumns() / 2)
                        continue;
                    invader = InvaderFactory.createInvader(getPosition(j, i), InvaderType.BOSS);
                } else {
                    InvaderType type = generateInvaderType(i, j);

                    if (isNull(type) || type == InvaderType.NONE) {
                        line.add(null);
                        continue;
                    }

                    invader = InvaderFactory.createInvader(getPosition(j, i), type);
                }
                line.add(invader);
                totalInvaders++;
                if (isNull(invader))
                    continue;

                if (invader.isShooter())
                    shooters.add((InvaderShooter) invader);
            }
            waves.add(line);
        }
        return waves;
    }

    private InvaderType generateInvaderType(int row, int column) {
        double rowRatio = (double) row / gameConfig.getNumberOfRows();
        double colRatio = (double) column / gameConfig.getNumberOfColumns();

        if (currentLevel % gameConfig.getBossRate() == 0) {
            // BOSS LEVEL déplacer plus haut la gestion, ne mettre que le boss dans le tableau
            if (row == gameConfig.getNumberOfRows() / 2 && column == gameConfig.getNumberOfColumns() / 2)
                return InvaderType.BOSS;
            return null;
        }
        double prob = Math.random();

        if (row == 0)
            return InvaderType.GUARDIAN_SHOOTER;

        if (prob <= 0.35)
            return InvaderType.SOLDIER;
        if (prob <= 0.5)
            return InvaderType.SHOOTER;
        if (prob <= 0.75)
            return InvaderType.HEALER;
        if (prob <= 0.9)
            return InvaderType.GUARDIAN;
        return null;
    }

    protected Position getPosition(int column, int row) {
        return new Position(rect.leftGap + rect.xGap * column, rect.topGap + rect.yGap * row);
    }

    public ArrayList<InvaderShooter> getShooters() {
        return shooters;
    }

    public int getTotalInvaders() {
        return totalInvaders;
    }
}
