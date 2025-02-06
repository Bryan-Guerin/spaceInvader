package com.bryan.spaceinvader.model.shop;

public class ShopItem {

    private final String name;
    private final String texture;
    private int cost;
    private boolean unlocked;
    private int currentLevel;

    public ShopItem(String name, int cost, String texture) {
        this.name = name;
        this.cost = cost;
        this.texture = texture;
    }

    public void handlePurchase() {
        // TODO utiliser une suite géométrique pour gérer l'augmentation du prix.
        //  La raison est le costFactor de l'enum et u0 est la valeur initiale.
        cost = 0;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getTexture() {
        return texture;
    }
}
