
import java.util.Random;

public class Forest {

    private Cell[][] grid;

    private double temperature;
    private double rainfall;
    private double aridity;

    private Wind wind;

    private Random random = new Random();

    public Forest(int rows, int columns) {

        grid = new Cell[rows][columns];

        temperature = 25.0;
        rainfall = 0.2;
        aridity = 0.5;

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < columns; col++) {

                grid[row][col] = new Cell(row, col);

                if (random.nextDouble() < 0.8) {

                    double spreadability =
                            0.3 + random.nextDouble() * 0.7;

                    grid[row][col].plantTree(
                            new Tree(spreadability)
                    );
                }
            }
        }
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
        wind.applyWind(this);
    }

    public Wind getWind() {
        return wind;
    }

    public void update() {

        spreadFire();

        generateSmoke();

        moveSmoke();

        dissipateSmoke();

        advanceBurningTrees();

        if (!hasActiveFire()) {
            regrowTrees();
        }

        growTrees();
    }

    private void generateSmoke() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (cell.hasTree() &&
                    cell.getTree().isBurning()) {

                    double currentSmoke =
                            cell.getSmokeConcentration();

                    cell.setSmokeConcentration(
                            currentSmoke + 5.0
                    );
                }
            }
        }
    }

    private void moveSmoke() {

        if (wind == null || wind.getSpeed() <= 0) {
            return;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        double[][] smokeChanges = new double[rows][cols];

        int dx = 0;
        int dy = 0;

        switch (wind.getDirection()) {

            case "NORTH":
                dy = -1;
                break;

            case "SOUTH":
                dy = 1;
                break;

            case "EAST":
                dx = 1;
                break;

            case "WEST":
                dx = -1;
                break;

            default:
                return;
        }

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < cols; col++) {

                Cell cell = grid[row][col];

                double smoke = cell.getSmokeConcentration();

                if (smoke <= 0) {
                    continue;
                }

                int nextRow = row + dy;
                int nextCol = col + dx;

                if (nextRow < 0 || nextRow >= rows ||
                    nextCol < 0 || nextCol >= cols) {
                    continue;
                }

                double exposure =
                        wind.calculateExposure(col, row);

                double movementFraction =
                        Math.min(0.30, exposure * 0.30);

                double smokeToMove = smoke * movementFraction;

                smokeChanges[row][col] -= smokeToMove;
                smokeChanges[nextRow][nextCol] += smokeToMove;
            }
        }

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < cols; col++) {

                double updatedSmoke =
                        grid[row][col].getSmokeConcentration()
                        + smokeChanges[row][col];

                grid[row][col].setSmokeConcentration(updatedSmoke);
            }
        }
    }

    private void dissipateSmoke() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                double currentSmoke =
                        cell.getSmokeConcentration();

                double newSmoke = currentSmoke - 2.0;

                cell.setSmokeConcentration(newSmoke);
            }
        }
    }

    private void advanceBurningTrees() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (!cell.hasTree()) {
                    continue;
                }

                Tree tree = cell.getTree();

                if (tree.isBurning()) {
                    tree.advanceBurning();
                }

                if (!tree.isAlive()) {

                    cell.removeTree();

                    cell.setBurnedBefore(true);

                    cell.resetRecoveryTimer();
                }
            }
        }
    }

    private boolean hasActiveFire() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (cell.hasTree() &&
                    cell.getTree().isBurning()) {

                    return true;
                }
            }
        }

        return false;
    }

    private void regrowTrees() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (!cell.wasBurned() || cell.hasTree()) {
                    continue;
                }

                cell.increaseRecoveryTimer();

                if (cell.getRecoveryTimer() >= 3) {

                    double spreadability =
                            0.3 + random.nextDouble() * 0.7;

                    Tree newTree = new Tree(spreadability);

                    newTree.startGrowth();

                    cell.plantTree(newTree);

                    cell.setBurnedBefore(false);

                    cell.resetRecoveryTimer();
                }
            }
        }
    }

    private void growTrees() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (cell.hasTree()) {

                    cell.getTree().grow();
                }
            }
        }
    }

    private void spreadFire() {

        boolean[][] igniteNext =
                new boolean[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                if (cell.hasTree() &&
                    cell.getTree().isBurning()) {

                    trySpread(row, col, row - 1, col, igniteNext);
                    trySpread(row, col, row + 1, col, igniteNext);
                    trySpread(row, col, row, col - 1, igniteNext);
                    trySpread(row, col, row, col + 1, igniteNext);
                    trySpread(row, col, row - 1, col - 1, igniteNext);
                    trySpread(row, col, row - 1, col + 1, igniteNext);
                    trySpread(row, col, row + 1, col - 1, igniteNext);
                    trySpread(row, col, row + 1, col + 1, igniteNext);
                }
            }
        }

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                if (igniteNext[row][col]) {
                    grid[row][col].getTree().ignite();
                }
            }
        }
    }

    private void trySpread(
            int sourceRow,
            int sourceCol,
            int row,
            int col,
            boolean[][] igniteNext) {

        if (row < 0 ||
            row >= grid.length ||
            col < 0 ||
            col >= grid[0].length) {

            return;
        }

        Cell target = grid[row][col];

        if (!target.hasTree()) {
            return;
        }

        if (target.getTree().isBurning()) {
            return;
        }

        double chance =
                target.getTree().getSpreadability();

        if (wind != null) {

            chance += wind.getSpreadModifier(
                    sourceCol,
                    sourceRow,
                    col,
                    row
            );
        }

        chance += aridity * 0.2;

        chance += (temperature / 100.0) * 0.2;

        chance -= rainfall * 0.2;

        chance = Math.max(0.0, Math.min(1.0, chance));

        if (random.nextDouble() < chance) {
            igniteNext[row][col] = true;
        }
    }

    public void display() {

        for (Cell[] row : grid) {

            for (Cell cell : row) {

                if (!cell.hasTree()) {
                    System.out.print(". ");

                } else if (cell.getTree().isBurning()) {
                    System.out.print("X ");

                } else {
                    System.out.print("T ");
                }
            }

            System.out.println();
        }
    }
}
