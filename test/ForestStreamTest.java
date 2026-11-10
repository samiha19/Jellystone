public class ForestStreamTest {

    public static void main(String[] args) {

        Forest forest = new Forest(10, 10);

        System.out.println(
                "Living trees: " + forest.countLivingTrees()
        );

        System.out.println(
                "Burning trees: " + forest.countBurningTrees()
        );

        System.out.println(
                "Average smoke: " + forest.getAverageSmoke()
        );
    }
}
