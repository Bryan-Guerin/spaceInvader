package com.bryan.spaceinvader.model.invader;

import com.bryan.spaceinvader.model.game.Position;

import static java.util.Objects.isNull;

public class InvaderFactory {
    public static AbsInvader createInvader(Position position, InvaderType type) {
        if (isNull(type)) return null;

        return switch (type) {
            case SHOOTER, GUARDIAN_SHOOTER, BOSS -> new InvaderShooter(position, type);
            case HEALER -> new InvaderHealer(position, type);
            case COMMANDER -> new InvaderBooster(position, type);
            default -> new Invader(position, type);
        };
    }
}
