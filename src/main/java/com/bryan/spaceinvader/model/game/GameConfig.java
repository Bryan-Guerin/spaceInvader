package com.bryan.spaceinvader.model.game;

import com.bryan.spaceinvader.model.Settings;

public class GameConfig {
    private int numberOfRows;
    private int numberOfColumns;
    private int invaderSpeed;

    public GameConfig(int numberOfRows, int numberOfColumns, int invaderSpeed) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.invaderSpeed = invaderSpeed;
    }

    public static GameConfig getGameConfig(Settings.Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> new GameConfig(8, 10, 2);
            case NORMAL -> new GameConfig(9, 15, 3);
            case HARD -> new GameConfig(10, 20, 3);
            default -> new GameConfig(5, 8, 1);
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

}
