package com.bryan.spaceinvader.model.shop;

import com.bryan.spaceinvader.model.player.Upgrade;

public class ShopItem {

    private final ShopItemType type;

    private int currentLevel;
    private int cost;
    private boolean locked;

    public ShopItem(ShopItemType type) {
        this.type = type;
        this.cost = type.getInitialCost();
    }

    public Upgrade handlePurchase() {
        if (locked) return new Upgrade(type.getAttribute(), 0);
        this.cost = type.getInitialCost() * (int) Math.pow(type.getCostFactor(), currentLevel);
        this.currentLevel++;
        if (currentLevel == type.getMaxLevel()) locked = true;
        return new Upgrade(type.getAttribute(), type.getAttributeGain());
    }

    public int getCost() {
        return cost;
    }

    public boolean isLocked() {
        return locked;
    }

    public ShopItemType getType() {
        return type;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public String getDescription() {
        return "Augmenter l'attribut " +
                type.getAttribute().name() +
                " de " +
                type.getAttributeGain() +
                " points\n" +
                "Niveau actuel : " +
                currentLevel +
                " / "
                + type.getMaxLevel();
    }
}
