
public class ForestParallelTest {

    public static void main(String[] args) {

        Forest forest = new Forest(40, 60);

        // Compare sequential and parallel tree counts
        long livingSequential = forest.countLivingTrees();
        long livingParallel = forest.countLivingTreesParallel();

        check(
            "Living tree count",
            livingSequential == livingParallel
        );

        long burningSequential = forest.countBurningTrees();
        long burningParallel = forest.countBurningTreesParallel();

        check(
            "Burning tree count",
            burningSequential == burningParallel
        );

        // Compare average smoke calculations
        double smokeSequential = forest.getAverageSmoke();
        double smokeParallel = forest.getAverageSmokeParallel();

        check(
            "Average smoke",
            Math.abs(smokeSequential - smokeParallel) < 0.0001
        );

        // Test again after changing forest state
        Cell treeCell = null;

        for (Cell[] row : forest.getGrid()) {
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
            throw new AssertionError("No tree found for test");
        }

        treeCell.getTree().ignite();
        treeCell.setSmokeConcentration(12.0);

        check(
            "Burning count after ignition",
            forest.countBurningTrees()
                == forest.countBurningTreesParallel()
        );

        check(
            "Average smoke after ignition",
            Math.abs(
                forest.getAverageSmoke()
                - forest.getAverageSmokeParallel()
            ) < 0.0001
        );

        System.out.println();
        System.out.println("All Parallel Stream tests passed!");
    }

    private static void check(String name, boolean condition) {

        if (!condition) {
            throw new AssertionError("FAIL: " + name);
        }

        System.out.println("PASS: " + name);
    }
}
