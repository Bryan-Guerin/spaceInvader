package com.bryan.spaceinvader.model.game.level.generator;

import com.bryan.spaceinvader.model.game.GameConfig;
import com.bryan.spaceinvader.model.game.Rect;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.InvaderFactory;
import com.bryan.spaceinvader.model.invader.InvaderShooter;
import com.bryan.spaceinvader.model.invader.InvaderType;
import com.bryan.spaceinvader.model.invader.state.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class RandomLevelGenerator extends SimpleLevelGenerator {

    public RandomLevelGenerator(int currentLevel, GameConfig gameConfig, Rect rect) {
        super(currentLevel, gameConfig, rect);
    }

    public ArrayList<ArrayList<AbsInvader>> generate() {
        waves.clear();

        for (int i = 0; i < gameConfig.getNumberOfRows(); i++) {
            ArrayList<AbsInvader> line = new ArrayList<>();
            for (int j = 0; j < gameConfig.getNumberOfColumns(); j++) {
                AbsInvader invader;
                if (currentLevel % gameConfig.getBossRate() == 0) {
                    if (i != gameConfig.getNumberOfRows() / 2 || j != gameConfig.getNumberOfColumns() / 2)
                        continue;
                    invader = InvaderFactory.createInvader(getPosition(j, i), InvaderType.BOSS);
                } else {
                    HashMap<InvaderType, Double> probabilities = computeProbabilities(line, i, j);
                    InvaderType type = generateInvaderType(probabilities);

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

        // TODO faire la mécanique de boost et de heal

        return waves;
    }

    private HashMap<InvaderType, Double> computeProbabilities(ArrayList<AbsInvader> currentLine, int row, int column) {
        HashMap<InvaderType, Double> probabilities = new HashMap<>(gameConfig.getInvaderProbabilities());

        double rowRatio = (double) row / gameConfig.getNumberOfRows();
        double colRatio = (double) column / gameConfig.getNumberOfColumns();

        for (InvaderType type : probabilities.keySet()) {
            if (type.getState() instanceof ShooterState || type.getState() instanceof ConeShooterState) {
                // More shooter behind, no longer in front of soldier
                double patternArrangement = isSoliderBehind(row, column) ? 0.0 : 1.0;
                probabilities.put(type, probabilities.get(type) * rowRatio * patternArrangement);
                continue;
            }
            if (type.getState() instanceof HealerState || type.getState() instanceof BoosterState) {
                // More likely in the middle and not close to others healer or booster
                double patternArrangement = (rowRatio > 0.4 || rowRatio < 0.6) && (colRatio > 0.4 || colRatio < 0.6) ? 1 : 0;
                patternArrangement = isHealerOrBoosterClose(currentLine, row, column) ? 0 : patternArrangement;
                probabilities.put(type, probabilities.get(type) * patternArrangement);
            }
            if (type.getState() instanceof DefaultState) {
                // More soldier in front of shooter
                double patternArrangement = isShooterBehind(row, column) ? 2 : 1;
                probabilities.put(type, probabilities.get(type) * rowRatio * patternArrangement);
                continue;
            }
            probabilities.put(type, probabilities.get(type));
        }

        return normalizeProbabilities(probabilities);
    }

    private HashMap<InvaderType, Double> normalizeProbabilities(HashMap<InvaderType, Double> probabilities) {
        double total = 0;
        for (Double prob : probabilities.values()) {
            total += prob;
        }
        for (InvaderType type : probabilities.keySet()) {
            probabilities.put(type, probabilities.get(type) / total);
        }
        return probabilities;
    }

    private InvaderType generateInvaderType(HashMap<InvaderType, Double> probabilities) {
        double rand = Math.random();
        double cumul = 0;
        for (InvaderType type : probabilities.keySet()) {
            cumul += probabilities.get(type);
            if (rand < cumul)
                return type;
        }
        return InvaderType.NONE;
    }

    private boolean isHealerOrBoosterClose(ArrayList<AbsInvader> currentLine, int row, int column) {
        List<AbsInvader> invadersToCheck = new ArrayList<>(3);

//        At this point, the wave is being generated. So neighbors are only to the left, up and up-left.
//        | invader0 | invader1 |
//        | invader2 | currentLine.get(column) |
        AbsInvader invader0 = row == 0 || column == 0 ? null : waves.get(row - 1).get(column - 1);
        AbsInvader invader1 = row == 0 ? null : waves.get(row - 1).get(column);
        AbsInvader invader2 = column == 0 ? null : currentLine.get(column - 1);

        invadersToCheck.add(invader0);
        invadersToCheck.add(invader1);
        invadersToCheck.add(invader2);

        for (AbsInvader invaderToCheck : invadersToCheck) {
            if (nonNull(invaderToCheck) && isHealerOrBooster(invaderToCheck)) {
                return true;
            }
        }
        return false;
    }

    private boolean isHealerOrBooster(AbsInvader invader) {
        return invader.isHealer() || invader.isBooster();
    }

    private boolean isSoliderBehind(int row, int column) {
        AbsInvader invader = row == 0 ? null : waves.get(row - 1).get(column);
        return !isNull(invader) && invader.isNormal();
    }

    private boolean isShooterBehind(int row, int column) {
        AbsInvader invader = row == 0 ? null : waves.get(row - 1).get(column);
        return !isNull(invader) && invader.isShooter();
    }

}
