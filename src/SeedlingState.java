
public class SeedlingState implements TreeGrowthState {

    @Override
    public void grow(Tree tree) {

        tree.increaseGrowthAge();

        if (tree.getGrowthAge() >= 30) {
            tree.setGrowthState(new YoungTreeState());
        }
    }

    @Override
    public String getStageName() {
        return "SEEDLING";
    }
}
