package com.bryan.spaceinvader.model.game.level.generator.wfc;

import com.bryan.spaceinvader.model.invader.InvaderType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WFCLevelConfig {

    private final List<WFCInvaderConfig> invaderConfigs;
    private final int gridWidth;
    private final int gridHeight;
    private final Map<InvaderType, WFCInvaderConfig> invaderConfigMap = new HashMap<>();

    public WFCLevelConfig(List<WFCInvaderConfig> invaderConfigs, int gridWidth, int gridHeight) {
        this.invaderConfigs = invaderConfigs;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        if (invaderConfigs != null) {
            for (WFCInvaderConfig config : invaderConfigs) {
                if (config != null) {
                    this.invaderConfigMap.put(config.getInvaderType(), config);
                }
            }
        }
    }

    public List<WFCInvaderConfig> getInvaderConfigs() {
        return invaderConfigs;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public WFCInvaderConfig getConfig(InvaderType type) {
        return invaderConfigMap.get(type);
    }

    public List<InvaderType> getAllowedNeighbors(InvaderType type, Direction direction) {
        WFCInvaderConfig config = invaderConfigMap.get(type);
        if (config != null) {
            Map<Direction, List<InvaderType>> rules = config.getAdjacencyRules();
            if (rules != null && rules.containsKey(direction)) {
                return rules.get(direction);
            }
        }
        return Collections.emptyList(); // Return an empty list if no rule is found
    }

    public static WFCLevelConfig createDefaultConfig(int gridWidth, int gridHeight) {
        List<WFCInvaderConfig> configs = new ArrayList<>();

        // InvaderType.NONE
        Map<Direction, List<InvaderType>> noneRules = new HashMap<>();
        noneRules.put(Direction.UP, Arrays.asList(InvaderType.NONE, InvaderType.SHOOTER));
        noneRules.put(Direction.DOWN, Arrays.asList(InvaderType.NONE, InvaderType.SOLDIER));
        noneRules.put(Direction.LEFT, Arrays.asList(InvaderType.NONE, InvaderType.SOLDIER, InvaderType.SHOOTER));
        noneRules.put(Direction.RIGHT, Arrays.asList(InvaderType.NONE, InvaderType.SOLDIER, InvaderType.SHOOTER));
        WFCInvaderConfig noneConfig = new WFCInvaderConfig(InvaderType.NONE, 10.0, noneRules);
        configs.add(noneConfig);

        // InvaderType.SOLDIER
        Map<Direction, List<InvaderType>> soldierRules = new HashMap<>();
        soldierRules.put(Direction.UP, Arrays.asList(InvaderType.NONE, InvaderType.SHOOTER));
        soldierRules.put(Direction.DOWN, Arrays.asList(InvaderType.NONE));
        soldierRules.put(Direction.LEFT, Arrays.asList(InvaderType.SOLDIER, InvaderType.NONE));
        soldierRules.put(Direction.RIGHT, Arrays.asList(InvaderType.SOLDIER, InvaderType.NONE));
        WFCInvaderConfig soldierConfig = new WFCInvaderConfig(InvaderType.SOLDIER, 5.0, soldierRules);
        configs.add(soldierConfig);

        // InvaderType.SHOOTER
        Map<Direction, List<InvaderType>> shooterRules = new HashMap<>();
        shooterRules.put(Direction.UP, Arrays.asList(InvaderType.NONE));
        shooterRules.put(Direction.DOWN, Arrays.asList(InvaderType.NONE, InvaderType.SOLDIER));
        shooterRules.put(Direction.LEFT, Arrays.asList(InvaderType.SHOOTER, InvaderType.NONE));
        shooterRules.put(Direction.RIGHT, Arrays.asList(InvaderType.SHOOTER, InvaderType.NONE));
        WFCInvaderConfig shooterConfig = new WFCInvaderConfig(InvaderType.SHOOTER, 3.0, shooterRules);
        configs.add(shooterConfig);
        
        return new WFCLevelConfig(configs, gridWidth, gridHeight);
    }
}
