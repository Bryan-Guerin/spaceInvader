package com.bryan.spaceinvader.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;

public class Progress {
    private final VBox scoreBar;

    private int totalScore;
    private int currentLevel;
    private int score;
    private int lives;

    @JsonIgnore
    private transient int totalInvaders;
    @JsonIgnore
    private transient int invadersKilled;

    private final StringProperty propertyScore = new SimpleStringProperty();
    private final StringProperty propertyCurrentLevel = new SimpleStringProperty();
    private final StringProperty propertyLives = new SimpleStringProperty();

    public Progress(VBox scoreBar) {
        this.scoreBar = scoreBar;
        reset();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    private void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
        propertyCurrentLevel.set("Niveau " + currentLevel);
    }

    public void addScore(int score) {
        totalScore += score;
        setScore(this.score + score);
    }

    private void setScore(int i) {
        this.score = i;
        propertyScore.set(String.valueOf(this.score));
    }

    public void loseLife() {
        setLives(lives - 1);
    }

    private void setLives(int lives) {
        this.lives = lives;
        propertyLives.set("Vies : " + this.lives);
    }

    public void nextLevel() {
        setCurrentLevel(currentLevel + 1);
        setTotalInvaders(0);
    }

    public void reset() {
        setCurrentLevel(0);
        setScore(0);
        setLives(3);
        setTotalInvaders(0);
        totalScore = 0;
    }

    public void recordKill(int score) {
        invadersKilled++;
        updateProgressPercentage((double) invadersKilled / totalInvaders);
        addScore(score);
    }

    public void setTotalInvaders(int totalInvaders) {
        this.totalInvaders = totalInvaders;
        this.invadersKilled = 0;
        updateProgressPercentage(0);
    }

    public void spendScore(int score) {
        this.score -= score;
        setScore(this.score);
    }

    /**
     * Update the score bar
     *
     * @param progressPercentage Percentage of invaders killed [0, 1]
     */
    private void updateProgressPercentage(double progressPercentage) {
        scoreBar.setPrefHeight(scoreBar.getMaxHeight() * progressPercentage);
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

    public boolean isLevelCompleted() {
        return invadersKilled == totalInvaders;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getScore() {
        return this.score;
    }
}
