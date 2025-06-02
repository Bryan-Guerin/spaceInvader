package com.bryan.spaceinvader.model.game.level.generator.wfc;

import com.bryan.spaceinvader.model.invader.InvaderType;
import java.util.List;
import java.util.Map;

public class WFCInvaderConfig {

    private final InvaderType invaderType;
    private final double weight;
    private final Map<Direction, List<InvaderType>> adjacencyRules;

    public WFCInvaderConfig(InvaderType invaderType, double weight, Map<Direction, List<InvaderType>> adjacencyRules) {
        this.invaderType = invaderType;
        this.weight = weight;
        this.adjacencyRules = adjacencyRules;
    }

    public InvaderType getInvaderType() {
        return invaderType;
    }

    public double getWeight() {
        return weight;
    }

    public Map<Direction, List<InvaderType>> getAdjacencyRules() {
        return adjacencyRules;
    }
}
