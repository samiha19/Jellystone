
public class TreeGrowthStateTest {

    public static void main(String[] args) {

        Tree tree = new Tree(0.5);

        System.out.println(tree.getGrowthStage());

        tree.startGrowth();
        System.out.println(tree.getGrowthStage());

        for (int i = 0; i < 8; i++) {
            tree.grow();
        }
        System.out.println(tree.getGrowthStage());

        for (int i = 0; i < 10; i++) {
            tree.grow();
        }
        System.out.println(tree.getGrowthStage());
    }
}
