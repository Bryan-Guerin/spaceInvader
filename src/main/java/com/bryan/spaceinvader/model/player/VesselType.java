package com.bryan.spaceinvader.model.player;

public enum VesselType {
    V0(3, 3, 3, 1, "V0.png"),
    V1(5, 4, 4, 2, "V1.png"),
    V2(7, 5, 4, 3, "V2.png");

    private final int baseMaxHealth;
    private final int baseMovementSpeed;
    private final int baseAttackSpeed; // Bullet per second
    private final int baseBulletDamage;
    private final String texture;

    VesselType(int baseMaxHealth, int baseMovementSpeed, int baseAttackSpeed, int baseBulletDamage, String texture) {
        this.baseMaxHealth = baseMaxHealth;
        this.baseMovementSpeed = baseMovementSpeed;
        this.baseAttackSpeed = baseAttackSpeed;
        this.baseBulletDamage = baseBulletDamage;
        this.texture = texture;
    }

    public int getBaseMaxHealth() {
        return baseMaxHealth;
    }

    public int getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    public int getBaseAttackSpeed() {
        return baseAttackSpeed;
    }

    public int getBaseBulletDamage() {
        return baseBulletDamage;
    }

    public String getTexture() {
        return texture;
    }

}
