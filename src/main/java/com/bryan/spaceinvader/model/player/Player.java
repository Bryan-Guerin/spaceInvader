package com.bryan.spaceinvader.model.player;

import com.bryan.spaceinvader.model.Settings;
import com.bryan.spaceinvader.model.game.Bullet;
import com.bryan.spaceinvader.model.game.Position;
import com.bryan.spaceinvader.model.game.Vector;
import com.bryan.spaceinvader.model.ressource.manager.ResourceManager;
import com.bryan.spaceinvader.model.ressource.manager.ResourceType;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;

public class Player {
    public static final int WIDTH = 45;
    private static final Settings settings = Settings.getInstance();

    private final MediaPlayer shootMediaPlayer = new MediaPlayer(ResourceManager.loadResource("shoot.wav", Media.class, ResourceType.AUDIO));
    private final MediaPlayer explosionMediaPlayer = new MediaPlayer(ResourceManager.loadResource("explosion.wav", Media.class, ResourceType.AUDIO));
    private final HashMap<AttributeType, Attribute> attributes = new HashMap<>();
    private final int POSITION_Y = 900;
    private final int POSITION_X;
    private final VBox healthBar;

    private VesselType vesselType = VesselType.V0;
    public Position position;
    private double health;

    public Player(int x, VBox healthBar) {
        POSITION_X = x;
        this.position = new Position(x, POSITION_Y);
        this.healthBar = healthBar;
        initAttributes();
        health = attributes.get(AttributeType.MAX_HEALTH).getValue();
    }

    private void initAttributes() {
        attributes.put(AttributeType.ATTACK_SPEED, new Attribute(vesselType.getBaseAttackSpeed()));
        attributes.put(AttributeType.BULLET_DAMAGE, new Attribute(vesselType.getBaseBulletDamage()));
        attributes.put(AttributeType.BULLET_SPEED, new Attribute(3)); // TODO get from vessel ??
        attributes.put(AttributeType.MOVEMENT_SPEED, new Attribute(vesselType.getBaseMovementSpeed()));
        attributes.put(AttributeType.MAX_HEALTH, new Attribute(vesselType.getBaseMaxHealth()));
    }

    private void resetBoost() {
        for (Attribute attribute : attributes.values()) {
            attribute.resetBoost();
        }
    }

    public Bullet shoot() {
        shootMediaPlayer.stop();
        shootMediaPlayer.setVolume(settings.getVolume() / 40); // This sound is too loud. Have to fix the source
        shootMediaPlayer.play();
        return new Bullet(position, new Vector(0, -getAttributeValue(AttributeType.BULLET_SPEED)), getAttributeValue(AttributeType.BULLET_DAMAGE));
    }

    public void moveLeft() {
        position.move(new Vector(-getAttributeValue(AttributeType.MOVEMENT_SPEED), 0));
    }

    public void moveRight() {
        position.move(new Vector(getAttributeValue(AttributeType.MOVEMENT_SPEED), 0));
    }

    public void nextVessel() {
        if (!isMaxVessel()) {
            vesselType = VesselType.values()[vesselType.ordinal() + 1];
            attributes.get(AttributeType.ATTACK_SPEED).setBaseStat(vesselType.getBaseAttackSpeed());
            attributes.get(AttributeType.BULLET_DAMAGE).setBaseStat(vesselType.getBaseBulletDamage());
            attributes.get(AttributeType.MOVEMENT_SPEED).setBaseStat(vesselType.getBaseMovementSpeed());
            attributes.get(AttributeType.MAX_HEALTH).setBaseStat(vesselType.getBaseMaxHealth());
        }
    }

    public String getVesselTexture() {
        return vesselType.getTexture();
    }

    public boolean isMaxVessel() {
        return vesselType.ordinal() == VesselType.values().length;
    }

    public long getAttackDelay() {
        return (long) (1000 / getAttributeValue(AttributeType.ATTACK_SPEED));
    }

    public void heal(double amount) {
        health += amount;
        if (health > getAttributeValue(AttributeType.MAX_HEALTH))
            health = getAttributeValue(AttributeType.MAX_HEALTH);
        updateHealthBar((double) health / getAttributeValue(AttributeType.MAX_HEALTH));
    }

    public void takeDamage(double damage) {
        explosionMediaPlayer.stop();
        explosionMediaPlayer.setVolume(settings.getVolume() / 40);
        health -= damage;
        explosionMediaPlayer.play();
        if (health < 0)
            health = 0;
        updateHealthBar((double) health / getAttributeValue(AttributeType.MAX_HEALTH));
    }

    public boolean isDead() {
        return health == 0;
    }

    public void softReset() {
        heal(getAttributeValue(AttributeType.MAX_HEALTH));
        resetBoost();
        this.position = new Position(POSITION_X, POSITION_Y);
    }

    private double getAttributeValue(AttributeType attributeType) {
        return attributes.get(attributeType).getValue();
    }

    /**
     * Update the health bar
     *
     * @param healthPercentage Percentage of health [0, 1]
     */
    public void updateHealthBar(double healthPercentage) {
        healthBar.setPrefHeight(healthBar.getMaxHeight() * healthPercentage);
    }

    public void handleUpgrade(Upgrade upgrade) {
        if (upgrade.getAttribute().equals(AttributeType.VESSEL)) {
            nextVessel();
            return;
        }
        attributes.get(upgrade.getAttribute()).upgrade(upgrade);
    }

    private static class Attribute {

        private int baseStat;       // The base stat of the attribute, depend on the vessel
        private int boostValue;     // The value of the boost, reset at the end of the level
        private double upgradeValue;   // The total value of all the upgrades applied to the attribute

        public Attribute(int baseStat) {
            this.baseStat = baseStat;
            boostValue = 0;
            upgradeValue = 0;
        }

        public void setBaseStat(int baseStat) {
            this.baseStat = baseStat;
        }

        public void upgrade(Upgrade upgrade) {
            upgradeValue += upgrade.getValue();
            boostValue += upgrade.getBoostValue();
        }

        public void resetBoost() {
            boostValue = 0;
        }

        public double getValue() {
            return baseStat + upgradeValue + boostValue;
        }

    }
}