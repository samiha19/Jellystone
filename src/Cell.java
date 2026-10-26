class Cell {

    private int x;
    private int y;
    private Tree tree;
    private double windExposure;
    private double smokeConcentration;

    private int recoveryTimer = 0;
    private boolean burnedBefore = false;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.tree = null;
        this.smokeConcentration = 0.0;
    }

    // Get X coordinate
    public int getX() {
        return x;
    }

    // Get Y coordinate
    public int getY() {
        return y;
    }

    // Set X coordinate
    public void setX(int newX) {
        x = newX;
    }

    // Set Y coordinate
    public void setY(int newY) {
        y = newY;
    }

    // Plant a tree in this cell
    public void plantTree(Tree tree) {
        this.tree = tree;
    }

    // Check whether the cell has a tree
    public boolean hasTree() {
        if (this.tree == null) {
            return false;
        }
        return true;
    }

    // Get the tree
    public Tree getTree() {
        return this.tree;
    }

    // Remove the tree
    public Tree removeTree() {
        Tree removedTree = tree;
        tree = null;
        return removedTree;
    }

    // Check if the cell is empty
    public boolean isEmpty() {
        return !hasTree();
    }

    // Get wind exposure
    public double getWindExposure() {
        return windExposure;
    }

    // Set wind exposure
    public void setWindExposure(double windExposure) {
        this.windExposure = windExposure;
    }

    public double getSmokeConcentration() {
        return smokeConcentration;
    }

    // Set smoke concentration (0 to 100)
    public void setSmokeConcentration(double amount) {

        if (!Double.isFinite(amount)) {
            throw new IllegalArgumentException(
                "Smoke concentration must be finite"
            );
        }

        this.smokeConcentration =
            Math.max(0.0, Math.min(100.0, amount));
    }

public int getRecoveryTimer() {
    return recoveryTimer;
}

public void increaseRecoveryTimer() {
    recoveryTimer++;
}

public void resetRecoveryTimer() {
    recoveryTimer = 0;
}

public boolean wasBurned() {
    return burnedBefore;
}

public void setBurnedBefore(boolean burnedBefore) {
    this.burnedBefore = burnedBefore;
}

}
