
public class ForestStreamTest {

    public static void main(String[] args) {

        Forest forest = new Forest(10, 10);
        Cell[][] grid = forest.getGrid();

        // Find a cell containing a tree
        Cell treeCell = null;

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.hasTree()) {
                    treeCell = cell;
                    break;
                }
            }

            if (treeCell != null) {
                break;
            }
        }

        if (treeCell == null) {
            throw new AssertionError("No tree found for testing");
        }

        // Test 1: Living tree count
        long expectedLiving = 0;

        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.hasTree() && cell.getTree().isAlive()) {
                    expectedLiving++;
                }
            }
        }

        check(
            "Living tree count",
            forest.countLivingTrees() == expectedLiving
        );

        // Test 2: Burning tree count
        long beforeBurning = forest.countBurningTrees();

        treeCell.getTree().ignite();

        check(
            "Burning tree count",
            forest.countBurningTrees() == beforeBurning + 1
        );

        // Test 3: Average smoke concentration
        treeCell.setSmokeConcentration(100.0);

        double expectedAverage = 100.0 / 100.0;

        check(
            "Average smoke concentration",
            Math.abs(forest.getAverageSmoke() - expectedAverage) < 0.0001
        );

        // Test 4: Burning tree extinguished
        treeCell.getTree().extinguish();

        check(
            "Extinguishing tree",
            forest.countBurningTrees() == beforeBurning
        );

        System.out.println();
        System.out.println("All Forest Stream tests passed!");
    }

    private static void check(String name, boolean condition) {

        if (!condition) {
            throw new AssertionError("FAIL: " + name);
        }

        System.out.println("PASS: " + name);
    }
}
