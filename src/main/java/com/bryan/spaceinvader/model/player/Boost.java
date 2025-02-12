package com.bryan.spaceinvader.model.player;

/**
 * A boost represents a temporary increase in a player's attribute. <br>
 * The boost is removed at the end of the level.
 * @param attributeType The attribute to increase
 * @param value The amount of the attribute to increase
 */
public record Boost(AttributeType attributeType, int value) {

    public int getValue() {
        return value;
    }

    public AttributeType getStatistic() {
        return attributeType;
    }
}
