
public class MatureTreeState implements TreeGrowthState {

    @Override
    public void grow(Tree tree) {
        tree.increaseGrowthAge();
    }

    @Override
    public String getStageName() {
        return "MATURE";
    }
}
