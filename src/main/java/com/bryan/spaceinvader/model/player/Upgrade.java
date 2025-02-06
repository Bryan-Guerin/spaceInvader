package com.bryan.spaceinvader.model.player;

public class Upgrade {

    private final AttributeType attributeType;
    private double value;
    private int boostValue;

    public Upgrade(AttributeType attributeType, double value) {
        this.attributeType = attributeType;
        this.value = value;
        boostValue = 0;
    }

    public Upgrade upgrade(Upgrade upgrade) {
        value += upgrade.value;
        return this;
    }

    public Upgrade boost(Boost boost) {
        value += boost.getValue();
        boostValue += boost.getValue();
        return this;
    }

    public int getBoostValue() {
        return boostValue;
    }

    public void resetBoost() {
        value -= boostValue;
        boostValue = 0;
    }

    public AttributeType getAttribute() {
        return attributeType;
    }

    public double getValue() {
        return value;
    }

}
