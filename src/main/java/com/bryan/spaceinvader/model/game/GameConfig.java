package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.Settings;

public class GameConfig {
    private int numberOfRows;
    private int numberOfColumns;
    private int invaderSpeed;
    private double invaderShotProbability;

    public GameConfig(int numberOfRows, int numberOfColumns, int invaderSpeed, double invaderShotProbability) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.invaderSpeed = invaderSpeed;
        this.invaderShotProbability = invaderShotProbability;
    }

    public static GameConfig getGameConfig(Settings.Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> new GameConfig(8, 10, 2, 0.0005);
            case NORMAL -> new GameConfig(9, 15, 3, 0.001);
            case HARD -> new GameConfig(10, 20, 3, 0.002);
            default -> new GameConfig(5, 8, 1, 0);
        };
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

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public void setInvaderSpeed(int invaderSpeed) {
        this.invaderSpeed = invaderSpeed;
    }

    public double getInvaderShotProbability() {
        return invaderShotProbability;
    }

}
