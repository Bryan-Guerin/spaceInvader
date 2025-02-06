package com.bryan.spaceinvader.model.shop;

public enum ShopItemType {
    FIRE_RATE("Fire rate", 100, 1, 3, "fire_rate.png"),
    FIRE_DAMAGE("Fire damage", 100, 1, 3, "fire_damage.png"),
    BULLET_SPEED("Bullet speed", 100, 1, 3, "bullet_speed.png"),
    SHIELD("Shield", 100, 1, 3, "shield.png"),
    HEALTH_POINT("Health point", 20, 1, 3, "health_point.png"),
    VESSEL_UPGRADE("Vessel upgrade", 100, 1, 3, "vessel_upgrade.png");

    private final String name;
    private final int initialCost;
    private final double costFactor;
    private final String texture;
    private final int maxLevel;

    ShopItemType(String name, int initialCost, double costFactor, int maxLevel, String texture) {
        this.name = name;
        this.initialCost = initialCost;
        this.costFactor = costFactor;
        this.texture = texture;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
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
