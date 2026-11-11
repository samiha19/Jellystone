
public class IgnitionSourceTest {

    public static void main(String[] args) {

        testArsonDefaults();
        testArsonIgnitesTargetCell();
        testArsonDoesNotIgniteLowSpreadabilityTree();

        testLightningDefaults();
        testLightningIgnitesRandomCell();

        testBackburningDefaults();
        testBackburningIgnitesWaveColumns();

        testRejectsInvalidSeverity();
        testRejectsInvalidSpreadability();

        testIgnitionSourcePolymorphism();

        System.out.println();
        System.out.println("All IgnitionSource tests passed.");
    }

    private static void testArsonDefaults() {

        Arson arson = new Arson(0, 0);

        check(
                arson.severity == 0.55,
                "Arson should use severity 0.55"
        );

        check(
                arson.spreadability == 0.5,
                "Arson should use spreadability 0.5"
        );
    }

    private static void testArsonIgnitesTargetCell() {

        Forest forest = new Forest(2, 2);

        forest.getGrid()[0][0].plantTree(new Tree(0.9));

        Arson arson = new Arson(0, 0);
        arson.ignite(forest);

        check(
                forest.getGrid()[0][0].getTree().isBurning(),
                "Arson should ignite the target tree"
        );
    }

    private static void testArsonDoesNotIgniteLowSpreadabilityTree() {

        Forest forest = new Forest(2, 2);

        forest.getGrid()[0][0].plantTree(new Tree(0.1));

        Arson arson = new Arson(0, 0);
        arson.ignite(forest);

        check(
                !forest.getGrid()[0][0].getTree().isBurning(),
                "Low spreadability tree should not ignite"
        );
    }

    private static void testLightningDefaults() {

        Lightning lightning = new Lightning();

        check(
                lightning.severity == 0.8,
                "Lightning should use severity 0.8"
        );

        check(
                lightning.spreadability == 0.7,
                "Lightning should use spreadability 0.7"
        );
    }

    private static void testLightningIgnitesRandomCell() {

        Forest forest = new Forest(1, 1);

        forest.getGrid()[0][0].plantTree(new Tree(0.9));

        Lightning lightning = new Lightning();
        lightning.ignite(forest);

        check(
                forest.getGrid()[0][0].getTree().isBurning(),
                "Lightning should ignite a tree"
        );
    }

    private static void testBackburningDefaults() {

        Backburning backburning = new Backburning(0);

        check(
                backburning.severity == 0.7,
                "Backburning should use severity 0.7"
        );

        check(
                backburning.spreadability == 0.4,
                "Backburning should use spreadability 0.4"
        );
    }

    private static void testBackburningIgnitesWaveColumns() {

        Forest forest = new Forest(1, 6);

        for (int col = 0; col < 6; col++) {
            forest.getGrid()[0][col].plantTree(new Tree(0.9));
        }

        Backburning backburning = new Backburning(0);
        backburning.ignite(forest);

        int[] burningColumns = {2, 5};
        int[] safeColumns = {0, 1, 3, 4};

        for (int col : burningColumns) {

            check(
                    forest.getGrid()[0][col].getTree().isBurning(),
                    "Backburning should ignite wave trough column " + col
            );
        }

        for (int col : safeColumns) {

            check(
                    !forest.getGrid()[0][col].getTree().isBurning(),
                    "Backburning should not ignite wave crest column " + col
            );
        }
    }

    private static void testIgnitionSourcePolymorphism() {

        IgnitionSource[] sources = {
                new Arson(0, 0),
                new Lightning(),
                new Backburning(0)
        };

        for (IgnitionSource source : sources) {

            boolean isBackburning =
                    source instanceof Backburning;

            Forest forest;

            if (isBackburning) {

                forest = new Forest(1, 3);

                forest.getGrid()[0][2]
                        .plantTree(new Tree(0.9));

            } else {

                forest = new Forest(1, 1);

                forest.getGrid()[0][0]
                        .plantTree(new Tree(0.9));
            }

            source.ignite(forest);

            int targetColumn = isBackburning ? 2 : 0;

            check(
                    forest.getGrid()[0][targetColumn]
                            .getTree().isBurning(),
                    source.getClass().getSimpleName()
                            + " should ignite through an IgnitionSource reference"
            );
        }
    }

    private static void testRejectsInvalidSeverity() {

        boolean exceptionThrown = false;

        try {
            new TestIgnitionSource(1.1, 0.5);
        } catch (IllegalArgumentException exception) {
            exceptionThrown = true;
        }

        check(
                exceptionThrown,
                "Invalid severity should throw an exception"
        );
    }

    private static void testRejectsInvalidSpreadability() {

        boolean exceptionThrown = false;

        try {
            new TestIgnitionSource(0.5, -0.1);
        } catch (IllegalArgumentException exception) {
            exceptionThrown = true;
        }

        check(
                exceptionThrown,
                "Invalid spreadability should throw an exception"
        );
    }

    private static class TestIgnitionSource extends IgnitionSource {

        TestIgnitionSource(double severity, double spreadability) {
            super(severity, spreadability);
        }

        @Override
        public void ignite(Forest forest) {
            // Used only for testing constructor validation.
        }
    }

    private static void check(boolean condition, String message) {

        if (!condition) {
            throw new AssertionError("FAIL: " + message);
        }

        System.out.println("PASS: " + message);
    }
}
