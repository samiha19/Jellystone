
public class Tree implements Burnable {

    private boolean burning;
    private double spreadability;
    private double burnIntensity;
    private boolean alive;
    private int growthAge;
    private double health;
    private double maxHealth;
    private double burnIntensityFactor;

    private TreeGrowthState growthState;

    public Tree(double spreadability) {
        this(spreadability, 100.0, 1.0);
    }

    protected Tree(
            double spreadability,
            double maxHealth,
            double burnIntensityFactor) {

        validateSpreadability(spreadability);
        validateMaxHealth(maxHealth);
        validateBurnIntensityFactor(burnIntensityFactor);

        this.burning = false;
        this.spreadability = spreadability;
        this.burnIntensity = 0.0;
        this.burnIntensityFactor = burnIntensityFactor;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.alive = true;
        this.growthAge = 100;

        this.growthState = new MatureTreeState();
    }

    @Override
    public void ignite() {
        if (!alive) {
            return;
        }

        burning = true;
        burnIntensity = burnIntensityFactor;
    }

    // Overloaded version
    public void ignite(double severity) {
        if (alive && severity >= 0.5) {
            burning = true;
            burnIntensity = Math.max(
                    burnIntensity,
                    severity * burnIntensityFactor
            );
        }
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void damage(double amount) {
        if (!Double.isFinite(amount) || amount < 0.0) {
            throw new IllegalArgumentException(
                    "Damage must be a finite, non-negative value"
            );
        }

        if (!alive) {
            return;
        }

        health = Math.max(0.0, health - amount);

        if (health == 0.0) {
            kill();
        }
    }

    @Override
    public boolean isBurning() {
        return burning;
    }

    public boolean isAlive() {
        return alive;
    }

    public double getSpreadability() {
        return spreadability;
    }

    public double getBurnIntensity() {
        return burnIntensity;
    }

    public void extinguish() {
        burning = false;
        burnIntensity = 0.0;
    }

    public void kill() {
        alive = false;
        burning = false;
        burnIntensity = 0.0;
    }

    public void advanceBurning() {
        if (!burning || !alive) {
            return;
        }

        damage(30.0 * burnIntensity);
    }

    @Override
    public String toString() {
        return "[health=" + health + "/" + maxHealth
                + ", spreadability=" + spreadability
                + ", burnIntensity=" + burnIntensity
                + ", burning=" + burning
                + ", alive=" + alive
                + "]";
    }

    public String getTreeType() {
        if (getClass() == Tree.class) {
            return "OAK";
        }

        return getClass().getSimpleName().toUpperCase();
    }

    private static void validateSpreadability(double spreadability) {
        if (!Double.isFinite(spreadability)
                || spreadability < 0.0
                || spreadability > 1.0) {
            throw new IllegalArgumentException(
                    "Spreadability must be between 0.0 and 1.0"
            );
        }
    }

    private static void validateMaxHealth(double maxHealth) {
        if (!Double.isFinite(maxHealth) || maxHealth <= 0.0) {
            throw new IllegalArgumentException(
                    "Maximum health must be positive"
            );
        }
    }

    private static void validateBurnIntensityFactor(
            double burnIntensityFactor) {

        if (!Double.isFinite(burnIntensityFactor)
                || burnIntensityFactor < 0.0) {
            throw new IllegalArgumentException(
                    "Burn intensity factor must be non-negative"
            );
        }
    }

    // A newly planted tree begins as a seedling
    public void startGrowth() {
        growthAge = 0;
        growthState = new SeedlingState();
    }

    // The current state controls how the tree grows
    public void grow() {
        if (alive && !burning && growthAge < 100) {
            growthState.grow(this);
        }
    }

    public void increaseGrowthAge() {
        if (growthAge < 100) {
            growthAge = Math.min(100, growthAge + 4);
        }
    }

    public int getGrowthAge() {
        return growthAge;
    }

    public void setGrowthState(TreeGrowthState state) {
        if (state == null) {
            throw new IllegalArgumentException(
                    "Growth state cannot be null"
            );
        }

        this.growthState = state;
    }

    public String getGrowthStage() {
        return growthState.getStageName();
    }
}
