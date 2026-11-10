
public class YoungTreeState implements TreeGrowthState {

    @Override
    public void grow(Tree tree) {

        tree.increaseGrowthAge();

        if (tree.getGrowthAge() >= 70) {
            tree.setGrowthState(new MatureTreeState());
        }
    }

    @Override
    public String getStageName() {
        return "YOUNG";
    }
}
