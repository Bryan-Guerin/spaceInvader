package com.bryan.spaceinvader.model.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;

import static java.util.Objects.isNull;

public class Progress {
    private int currentLevel = 0;
    private int score = 0;
    private int lives = 3;
    private int totalInvaders = 0;
    private int invadersKilled = 0;

    private final IntegerProperty propertyProgressPercentage = new SimpleIntegerProperty(0);
    private final StringProperty propertyScore = new SimpleStringProperty("Score : " + score);
    private final StringProperty propertyCurrentLevel = new SimpleStringProperty("Level " + currentLevel);
    private final StringProperty propertyLives = new SimpleStringProperty("Lives : " + lives);

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public void nextLevel() {
        setCurrentLevel(currentLevel + 1);
        setTotalInvaders(0);
    }

    private void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
        propertyCurrentLevel.set("Level " + currentLevel);
    }

    public void addScore(int score) {
        setScore(this.score + score);
    }

    private void setScore(int i) {
        this.score = i;
        propertyScore.set("Score : " + this.score);
    }

    public void loseLife() {
        setLives(lives - 1);
    }

    private void setLives(int lives) {
        this.lives = lives;
        propertyLives.set("Lives : " + this.lives);
    }

    public void reset() {
        setCurrentLevel(0);
        setScore(0);
        setLives(3);
        setTotalInvaders(0);
    }

    public void decreaseTotalInvadersAlive() {
        invadersKilled--;
        propertyProgressPercentage.set((int) ((invadersKilled / (double) totalInvaders) * 100));
    }

    public void setTotalInvaders(int totalInvaders) {
        this.totalInvaders = totalInvaders;
        this.invadersKilled = 0;
        propertyProgressPercentage.set(0);
    }

    public ObservableValue<? extends Number> progressProperty() {
        return propertyProgressPercentage;
    }

    public ObservableValue<String> scoreProperty() {
        return propertyScore;
    }

    public ObservableValue<String> currentLevelProperty() {
        return propertyCurrentLevel;
    }

    public ObservableValue<String> livesProperty() {
        return propertyLives;
    }
}
