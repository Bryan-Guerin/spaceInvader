package com.bryan.spaceinvader.model.shop;

import com.bryan.spaceinvader.model.player.AttributeType;

public enum ShopItemType {
    FIRE_RATE(AttributeType.ATTACK_SPEED, 0.5, 1000, 2, 10, "upgrade_fire_rate.png"),
    FIRE_DAMAGE(AttributeType.BULLET_DAMAGE, 1, 5000, 2, 3, "upgrade_fire_damage.png"),
    BULLET_SPEED(AttributeType.BULLET_SPEED, 0.5, 4000, 2, 4, "upgrade_bullet_speed.png"),
    SHIELD(AttributeType.SHIELD, 1, 100, 2, Integer.MAX_VALUE, "upgrade_shield.png"), // Can have only 1 shield at a time. But it stops the first bullet without taking any damage
    HEALTH_POINT(AttributeType.MAX_HEALTH, 1, 1000, 1.2, 10, "upgrade_max_health.png"),
    VESSEL_UPGRADE(AttributeType.VESSEL, 1, 20000, 3, 2, "upgrade_vessel.png");

    private final AttributeType stat;
    private final double attributeGain;
    private final int initialCost;
    private final double costFactor;
    private final String texture;
    private final int maxLevel;

    ShopItemType(AttributeType stat, double attributeGain, int initialCost, double costFactor, int maxLevel, String texture) {
        this.stat = stat;
        this.attributeGain = attributeGain;
        this.initialCost = initialCost;
        this.costFactor = costFactor;
        this.texture = texture;
        this.maxLevel = maxLevel;
    }

    public double getAttributeGain() {
        return attributeGain;
    }

    public AttributeType getAttribute() {
        return stat;
    }

    public int getInitialCost() {
        return initialCost;
    }

    public String getTexture() {
        return texture;
    }

    public double getCostFactor() {
        return costFactor;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
