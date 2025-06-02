package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.invader.InvaderType;

import java.util.HashMap;

public class GameConfig {
    private final int numberOfRows;
    private final int numberOfColumns;
    private int invaderSpeed;
    private final int bossRate; // Number of level between boss spawn
    private final HashMap<InvaderType, Double> invaderProbabilities = new HashMap<>();
    private final double invaderShotProbability;

    public GameConfig(int numberOfRows, int numberOfColumns, int invaderSpeed, int bossRate, double invaderShotProbability, Settings.Difficulty difficulty) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.invaderSpeed = invaderSpeed;
        this.bossRate = bossRate;
        this.invaderShotProbability = invaderShotProbability;
        computeProbabilities(difficulty);
    }

    private void computeProbabilities(Settings.Difficulty difficulty) {
        // Probabilities are normalized during level generation, so no need to sum to 1
        switch (difficulty) {
            case EASY -> {
                invaderProbabilities.put(InvaderType.NONE, 0.30);
                invaderProbabilities.put(InvaderType.SOLDIER, 0.35);
                invaderProbabilities.put(InvaderType.SHOOTER, 0.20);
                invaderProbabilities.put(InvaderType.HEALER, 0.05);
                invaderProbabilities.put(InvaderType.GUARDIAN, 0.15);
                invaderProbabilities.put(InvaderType.GUARDIAN_SHOOTER, 0.00);
                invaderProbabilities.put(InvaderType.COMMANDER, 0.10);
            }
            case NORMAL -> {
                invaderProbabilities.put(InvaderType.NONE, 0.10);
                invaderProbabilities.put(InvaderType.SOLDIER, 0.35);
                invaderProbabilities.put(InvaderType.SHOOTER, 0.15);
                invaderProbabilities.put(InvaderType.HEALER, 0.10);
                invaderProbabilities.put(InvaderType.GUARDIAN, 0.10);
                invaderProbabilities.put(InvaderType.GUARDIAN_SHOOTER, 0.02);
                invaderProbabilities.put(InvaderType.COMMANDER, 0.10);
            }
            case HARD -> {
                invaderProbabilities.put(InvaderType.NONE, 0.05);
                invaderProbabilities.put(InvaderType.SOLDIER, 0.40);
                invaderProbabilities.put(InvaderType.SHOOTER, 0.30);
                invaderProbabilities.put(InvaderType.HEALER, 0.10);
                invaderProbabilities.put(InvaderType.GUARDIAN, 0.20);
                invaderProbabilities.put(InvaderType.GUARDIAN_SHOOTER, 0.005);
                invaderProbabilities.put(InvaderType.COMMANDER, 0.05);
            }
        }
        invaderProbabilities.put(InvaderType.BOSS, 0d);
    }

    public static GameConfig getGameConfig(Settings.Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> new GameConfig(8, 10, 2, 10, 0.0005, difficulty);
            case NORMAL -> new GameConfig(9, 12, 3, 5,0.001, difficulty);
            case HARD -> new GameConfig(10, 13, 3, 5, 0.0015, difficulty);
            default -> new GameConfig(3, 3, 1, 1, 0, difficulty);
        };
    }

    public HashMap<InvaderType, Double> getInvaderProbabilities() {
        return invaderProbabilities;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getInvaderSpeed() {
        return invaderSpeed;
    }

    public void setInvaderSpeed(int invaderSpeed) {
        this.invaderSpeed = invaderSpeed;
    }

    public int getBossRate() {
        return bossRate;
    }

    public double getInvaderShotProbability() {
        return invaderShotProbability;
    }

}
