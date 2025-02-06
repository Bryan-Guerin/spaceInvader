package com.bryan.spaceinvader.model.player;

public enum AttributeType {
    ATTACK_SPEED,
    BULLET_DAMAGE,
    BULLET_SPEED,
    MOVEMENT_SPEED,
    SHIELD, // TODO A considérer comme un effect et non un attribut. C'est pas de la stat mais du bool qui modifie un comportement. Un peu comme le tire en diagonale.
    MAX_HEALTH,
    VESSEL
}
