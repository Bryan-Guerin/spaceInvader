package com.bryan.spaceinvader.model.game;

public class Progress {
    private int currentLevel = 0;
    private int score = 0;
    private int lives = 3;
    private int totalInvaders = 0;
    private int invadersKilled = 0;

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
        reset();
        currentLevel++;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void loseLife() {
        lives--;
    }

    public void decreaseTotalInvadersAlive() {
        invadersKilled--;
    }

    public void reset() {
        currentLevel = 0;
        score = 0;
        lives = 3;
        totalInvaders = 0;
        invadersKilled = 0;
    }

    public void setTotalInvaders(int totalInvaders) {
        this.totalInvaders = totalInvaders;
        this.invadersKilled = 0;
    }
}
