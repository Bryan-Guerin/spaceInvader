package com.bryan.spaceinvader.model.game;

public enum InvaderType {
    SOLDIER(2, "invader_soldier.png", false, 10),                       // Classic invader
    SHOOTER(1, "invader_shooter.png", true, 15),                        // Invader that can shoot
    GUARDIAN(3, "invader_guardian.png", false, 20),                     // Invader with a shield
    GUARDIAN_SHOOTER(3, "invader_guardian_shooter.png", true, 30),      // Invader with a shield that can shoot
    COMMANDER(5, "invader_commander.png", false, 40),                   // Invader boosting surrounding invaders
    HEALER(2, "invader_healer.png", false, 50),                         // Invader healing surrounding invaders
    BOSS(10, "invader_boss.png", false, 1000);                          // Boss invader

    private final int maxHealth;
    private final String textureName;
    private final boolean isShooter;
    private final int score;

    InvaderType(int maxHealth, String textureName, boolean isShooter, int score) {
        this.maxHealth = maxHealth;
        this.textureName = textureName;
        this.isShooter = isShooter;
        this.score = score;
    }

    public String getTextureName() {
        return textureName;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isShooter() {
        return isShooter;
    }

    public int getScore() {
        return score;
    }
}