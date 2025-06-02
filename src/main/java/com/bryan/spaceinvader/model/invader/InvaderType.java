package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.invader.state.*;
import javafx.scene.paint.Color;

// TODO ajouter un param (liste ?) pour définir la classe de comportement de l'invader (tireur, boosteur, healer, etc ...)
//  pour simplifier le choix de la classe lors de la génération de niveau
public enum InvaderType {
    // No invader
    NONE(0, 0, "", new DefaultState(), false, Color.RED, 0),
    // Classic invader
    SOLDIER(2, 0, "invader_soldier.png", new DefaultState(), false, Color.WHITE, 10),
    // Invader that can shoot
    SHOOTER(1, 1, "invader_shooter.png", new ShooterState(), false, Color.GREEN, 15),
    // Invader with a shield
    GUARDIAN(5, 0, "invader_guardian.png", new DefaultState(), true, Color.WHITE, 20),
    // Invader with a shield that can shoot
    GUARDIAN_SHOOTER(3, 3, "invader_guardian_shooter.png", new ConeShooterState(), true, Color.BLUE, 30),
    // Invader boosting surrounding invaders
    COMMANDER(5, 2, "invader_commander.png", new BoosterState(), false, Color.WHITE, 40),
    // Invader healing surrounding invaders
    HEALER(2, 1, "invader_healer.png", new HealerState(), false, Color.WHITE, 50),
    // Boss invader
    BOSS(20, 1, "invader2a.png", new ConeShooterState(), false, Color.YELLOW, 2000);

    private final int maxHealth;
    private final double basePower; // It's the amount of damage / heal / boost or whatever the invader does
    private final String textureName;
    private final InvaderState state;
    private final boolean isDefaultShielded;
    private final Color color;
    private final int score;

    InvaderType(int maxHealth, double basePower, String textureName, InvaderState state, boolean isDefaultShielded, Color color, int score) {
        this.maxHealth = maxHealth;
        this.basePower = basePower;
        this.textureName = textureName;
        this.state = state;
        this.isDefaultShielded = isDefaultShielded;
        this.color = color;
        this.score = score;
    }

    public double getBasePower() {
        return basePower;
    }

    public String getTextureName() {
        return textureName;
    }

    public InvaderState getState() {
        return state;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isDefaultShielded() {
        return isDefaultShielded;
    }

    public Color getColor() {
        return color;
    }

    public int getScore() {
        return score;
    }
}