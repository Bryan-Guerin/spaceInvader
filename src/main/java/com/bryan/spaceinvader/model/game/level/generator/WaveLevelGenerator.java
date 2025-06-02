package com.bryan.spaceinvader.model.game.level.generator;

import com.bryan.spaceinvader.model.game.GameConfig;
import com.bryan.spaceinvader.model.game.Rect;
import com.bryan.spaceinvader.model.invader.AbsInvader;
import com.bryan.spaceinvader.model.invader.InvaderType; // Added import
import com.bryan.spaceinvader.model.game.level.generator.wfc.WFCLevelConfig; // Added import
import com.bryan.spaceinvader.model.game.level.generator.wfc.WFCInvaderConfig; // Added import
import com.bryan.spaceinvader.model.game.level.generator.wfc.Direction; // Added import
import com.bryan.spaceinvader.model.invader.InvaderFactory; // Added import
import com.bryan.spaceinvader.model.game.Position; // Added import

import java.util.ArrayList;
import java.util.List; // Added import
import java.util.Random; // Added import
import java.util.Stack; // Added import

public class WaveLevelGenerator implements LevelGenerator {

    private final int currentLevel;
    private final GameConfig gameConfig;
    private final Rect rect;
    private WFCLevelConfig wfcConfig;
    private int gridWidth;
    private int gridHeight;
    private Cell[][] grid;
    private final Random random = new Random();

    // Inner Cell class
    private static class Cell {
        int x, y;
        List<InvaderType> possibleInvaders;
        boolean collapsed = false;
        InvaderType finalInvaderType = null;

        Cell(int x, int y, List<InvaderType> initialPossibilities) {
            this.x = x;
            this.y = y;
            this.possibleInvaders = new ArrayList<>(initialPossibilities);
        }

        int getEntropy() {
            return possibleInvaders.size();
        }

        void collapseCell(InvaderType chosenType) {
            this.finalInvaderType = chosenType;
            this.collapsed = true;
            this.possibleInvaders.clear();
            this.possibleInvaders.add(chosenType);
        }
    }

    public WaveLevelGenerator(int currentLevel, GameConfig gameConfig, Rect rect) {
        this.currentLevel = currentLevel;
        this.gameConfig = gameConfig;
        this.rect = rect;

        // Calculate grid dimensions
        this.gridWidth = (int) (rect.getWidth() / AbsInvader.SIZE);
        this.gridHeight = (int) (rect.getHeight() / AbsInvader.SIZE);

        // Ensure grid dimensions are at least 1
        this.gridWidth = Math.max(1, this.gridWidth);
        this.gridHeight = Math.max(1, this.gridHeight);
        
        this.wfcConfig = WFCLevelConfig.createDefaultConfig(this.gridWidth, this.gridHeight);
        this.grid = new Cell[this.gridHeight][this.gridWidth];

        List<InvaderType> allTypes = new ArrayList<>();
        if (this.wfcConfig != null && this.wfcConfig.getInvaderConfigs() != null) {
            for (WFCInvaderConfig invaderConf : this.wfcConfig.getInvaderConfigs()) {
                if (invaderConf != null) {
                    allTypes.add(invaderConf.getInvaderType());
                }
            }
        }

        for (int y = 0; y < this.gridHeight; y++) {
            for (int x = 0; x < this.gridWidth; x++) {
                grid[y][x] = new Cell(x, y, allTypes);
            }
        }
    }

    @Override
    public ArrayList<ArrayList<AbsInvader>> generate() {
        // Grid initialization is handled by the constructor. Potentially add reset logic if generate() can be called multiple times on the same instance.
        int maxIterations = gridWidth * gridHeight * 10; // Safety break for unexpected issues
        int iterations = 0;
        while (iterations < maxIterations) {
            Cell cellToCollapse = findLowestEntropyCell();
            if (cellToCollapse == null) {
                // Check if all cells are collapsed or if it was a contradiction
                boolean allCollapsed = true;
                for (int y = 0; y < gridHeight; y++) {
                    for (int x = 0; x < gridWidth; x++) {
                        if (!grid[y][x].collapsed) {
                            allCollapsed = false;
                            break;
                        }
                    }
                    if (!allCollapsed) break;
                }
                if (!allCollapsed) {
                    System.err.println("WFC Error: Contradiction reached or no cell to collapse.");
                }
                break; // Exit loop if all collapsed or contradiction
            }

            List<InvaderType> currentPossibilities = cellToCollapse.possibleInvaders;
            // currentPossibilities should not be empty here due to findLowestEntropyCell check for entropy == 0
            InvaderType chosenType = selectWeightedRandom(currentPossibilities, this.wfcConfig);

            if (chosenType == null) {
                System.err.println("WFC Error: Could not select a type for cell " + cellToCollapse.x + "," + cellToCollapse.y);
                break;
            }
            // cellToCollapse.collapseCell(chosenType); // Corrected: remove duplicate
            cellToCollapse.collapseCell(chosenType); // Keep one call

            boolean propagationOk = propagate(cellToCollapse);
            if (!propagationOk) {
                System.err.println("WFC Error: Contradiction during propagation for cell (" + cellToCollapse.x + "," + cellToCollapse.y + ").");
                break; // Exit main WFC loop
            }

            iterations++;
        }
        if (iterations >= maxIterations) {
            System.err.println("WFC Error: Max iterations reached.");
            return new ArrayList<>(); // Return empty list on max iterations
        }

        // Check if WFC completed successfully (all cells collapsed)
        boolean allCellsSuccessfullyCollapsed = true;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (!grid[y][x].collapsed || grid[y][x].finalInvaderType == null) {
                    allCellsSuccessfullyCollapsed = false;
                    break;
                }
            }
            if (!allCellsSuccessfullyCollapsed) break;
        }

        if (allCellsSuccessfullyCollapsed) {
            System.out.println("WFC generation complete. Converting to invader list.");
            ArrayList<ArrayList<AbsInvader>> resultLevel = new ArrayList<>();
            for (int y = 0; y < gridHeight; y++) {
                ArrayList<AbsInvader> currentRow = new ArrayList<>();
                for (int x = 0; x < gridWidth; x++) {
                    Cell cell = grid[y][x];
                    if (cell.finalInvaderType != InvaderType.NONE) {
                        // Assuming rect.getX() and rect.getY() are the top-left corner of the generation area.
                        float actualX = (float) (rect.getX() + x * AbsInvader.SIZE);
                        float actualY = (float) (rect.getY() + y * AbsInvader.SIZE);
                        Position invaderPosition = new Position(actualX, actualY);
                        AbsInvader invader = InvaderFactory.createInvader(invaderPosition, cell.finalInvaderType);
                        if (invader != null) {
                            currentRow.add(invader);
                        }
                    }
                }
                resultLevel.add(currentRow);
            }
            return resultLevel;
        } else {
            // This path is taken if the loop exited due to contradiction or cellToCollapse being null
            // when not all cells were actually collapsed.
            System.err.println("WFC Error: Not all cells were successfully collapsed post-loop or error previously occurred. Returning empty level.");
            return new ArrayList<>();
        }
    }

    private Cell findLowestEntropyCell() {
        List<Cell> lowestEntropyCells = new ArrayList<>();
        int minEntropy = Integer.MAX_VALUE;
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                if (!grid[y][x].collapsed) {
                    int entropy = grid[y][x].getEntropy();
                    if (entropy == 0) return null; // Contradiction
                    if (entropy < minEntropy) {
                        minEntropy = entropy;
                        lowestEntropyCells.clear();
                        lowestEntropyCells.add(grid[y][x]);
                    } else if (entropy == minEntropy) {
                        lowestEntropyCells.add(grid[y][x]);
                    }
                }
            }
        }
        if (lowestEntropyCells.isEmpty()) return null; // All collapsed
        return lowestEntropyCells.get(random.nextInt(lowestEntropyCells.size()));
    }

    private InvaderType selectWeightedRandom(List<InvaderType> possibilities, WFCLevelConfig config) {
        if (possibilities == null || possibilities.isEmpty()) return null;

        double totalWeight = 0;
        for (InvaderType type : possibilities) {
            WFCInvaderConfig invConf = config.getConfig(type);
            if (invConf != null) totalWeight += invConf.getWeight();
        }

        if (totalWeight <= 0) {
             // Fallback if no weights or all zero: unweighted random choice
            return possibilities.get(random.nextInt(possibilities.size()));
        }

        double r = random.nextDouble() * totalWeight;
        double currentWeight = 0;
        for (InvaderType type : possibilities) {
            WFCInvaderConfig invConf = config.getConfig(type);
            double weight = (invConf != null) ? invConf.getWeight() : 0;
            
            // Consider only positive weights in the weighted selection part
            // If a type has 0 or negative weight, it shouldn't be chosen unless it's the only option (covered by totalWeight <= 0)
            if (weight <= 0) continue; 

            currentWeight += weight;
            if (r <= currentWeight) return type;
        }
        
        // Fallback: This might be reached if all positive-weight items were skipped,
        // or due to floating point inaccuracies. Choose the last type with positive weight,
        // or the last type in the list if none had positive weight (though covered by totalWeight <=0 check).
        // A robust fallback would be to iterate again and pick the first non-zero weighted item or last item.
        // For simplicity here, returning the last item from the original list if loop completes.
        // However, given the totalWeight > 0 check, this part should ideally only be reached
        // if all remaining items have weights that sum up very little and r is at the very end of the range.
        // A simple safe fallback is to pick the last element that had a positive weight.
        // Let's refine the fallback for robustness if all actual weights are zero or negative.
        for (int i = possibilities.size() - 1; i >= 0; i--) {
            WFCInvaderConfig invConf = config.getConfig(possibilities.get(i));
            if (invConf != null && invConf.getWeight() > 0) {
                return possibilities.get(i);
            }
        }
        // If truly no positive weights, pick last from original list (already covered by totalWeight <=0).
        return possibilities.get(possibilities.size() - 1);
    }

    private boolean propagate(Cell justCollapsedCell) {
        Stack<Cell> propagationStack = new Stack<>();
        propagationStack.push(justCollapsedCell);
        // Safety break: max number of cells * max number of invader types (potential states per cell)
        int safetyBreak = gridWidth * gridHeight * (wfcConfig.getInvaderConfigs() != null ? wfcConfig.getInvaderConfigs().size() : 10);
        if (safetyBreak == 0) safetyBreak = 1000; // Ensure safetyBreak is positive
        int count = 0;

        while (!propagationStack.isEmpty() && count < safetyBreak) {
            count++;
            Cell currentPropagationCell = propagationStack.pop();

            // This cell must be collapsed and have a final type to propagate its influence
            if (currentPropagationCell.finalInvaderType == null) continue;

            for (Direction directionToNeighbor : Direction.values()) {
                int nx = currentPropagationCell.x;
                int ny = currentPropagationCell.y;

                if (directionToNeighbor == Direction.UP) ny--;
                else if (directionToNeighbor == Direction.DOWN) ny++;
                else if (directionToNeighbor == Direction.LEFT) nx--;
                else if (directionToNeighbor == Direction.RIGHT) nx++;

                if (nx >= 0 && nx < gridWidth && ny >= 0 && ny < gridHeight) {
                    Cell neighbor = grid[ny][nx];
                    if (neighbor.collapsed) continue;

                    List<InvaderType> typesToRemove = new ArrayList<>();

                    // Get rules based on the current cell's chosen type
                    // These are the types allowed to be 'directionToNeighbor' of currentPropagationCell.finalInvaderType
                    List<InvaderType> allowedTypesByCurrentCell = this.wfcConfig.getAllowedNeighbors(currentPropagationCell.finalInvaderType, directionToNeighbor);

                    for (InvaderType possibleNeighborType : neighbor.possibleInvaders) {
                        boolean currentAllowsNeighbor = allowedTypesByCurrentCell.isEmpty() || allowedTypesByCurrentCell.contains(possibleNeighborType);
                        
                        // Get rules based on the potential neighbor's type
                        // These are the types allowed to be 'opposite of directionToNeighbor' of possibleNeighborType
                        List<InvaderType> neighborAllowsCurrentTypes = this.wfcConfig.getAllowedNeighbors(possibleNeighborType, directionToNeighbor.getOpposite());
                        boolean neighborAllowsCurrentFlag = neighborAllowsCurrentTypes.isEmpty() || neighborAllowsCurrentTypes.contains(currentPropagationCell.finalInvaderType);

                        if (!currentAllowsNeighbor || !neighborAllowsCurrentFlag) {
                            typesToRemove.add(possibleNeighborType);
                        }
                    }

                    if (!typesToRemove.isEmpty()) {
                        neighbor.possibleInvaders.removeAll(typesToRemove);
                        if (neighbor.possibleInvaders.isEmpty()) {
                            // System.err.println("WFC Propagation: Contradiction at (" + neighbor.x + "," + neighbor.y + ")");
                            return false; // Contradiction
                        }
                        // Only add to stack if its possibilities changed and it's not already there
                        // (Stack contains check is O(n), could optimize with a Set if performance becomes an issue)
                        if (!propagationStack.contains(neighbor)) {
                            propagationStack.push(neighbor);
                        }
                    }
                }
            }
        }
        if (count >= safetyBreak) System.err.println("WFC Propagation: Safety break triggered. Processed " + count + " cells.");
        return true;
    }
}
