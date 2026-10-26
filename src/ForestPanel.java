
import java.awt.*;
import javax.swing.*;

public class ForestPanel extends JPanel {

    private Forest forest;

    private Image treeImage;
    private Image seedlingImage;
    private Image youngTreeImage;
    private Image fireImage;

    private Lightning lightning;
    private Image lightningImage;

    private Cell lastLightningCell;
    private long lightningStartTime;

    public ForestPanel(Forest forest, Lightning lightning) {

        this.forest = forest;
        this.lightning = lightning;

        System.out.println(new java.io.File("images/tree.png").exists());
        System.out.println(new java.io.File("images/seedling.png").exists());
        System.out.println(new java.io.File("images/young_tree.png").exists());
        System.out.println(new java.io.File("images/fire.png").exists());
        System.out.println(new java.io.File("images/lightning.png").exists());

        treeImage = new ImageIcon("images/tree.png").getImage();
        seedlingImage = new ImageIcon("images/seedling.png").getImage();
        youngTreeImage = new ImageIcon("images/young_tree.png").getImage();
        fireImage = new ImageIcon("images/fire.png").getImage();
        lightningImage = new ImageIcon("images/lightning.png").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Cell[][] grid = forest.getGrid();

        int cellWidth = getWidth() / grid[0].length;
        int cellHeight = getHeight() / grid.length;

        Cell currentLightningCell = null;

        if (lightning != null) {

            currentLightningCell = lightning.getLastStruckCell();

            if (currentLightningCell != lastLightningCell) {

                lastLightningCell = currentLightningCell;
                lightningStartTime = System.currentTimeMillis();
            }
        }

        for (int row = 0; row < grid.length; row++) {

            for (int col = 0; col < grid[row].length; col++) {

                Cell cell = grid[row][col];

                int x = col * cellWidth;
                int y = row * cellHeight;

                g.setColor(Color.BLACK);
                g.fillRect(x, y, cellWidth, cellHeight);

                if (lightning != null &&
                    cell == currentLightningCell &&
                    System.currentTimeMillis() - lightningStartTime < 3000) {

                    g.drawImage(
                        lightningImage,
                        x + 2,
                        y + 2,
                        cellWidth - 4,
                        cellHeight - 4,
                        this
                    );
                }
                else if (cell.hasTree()) {

                    Tree tree = cell.getTree();

                    if (tree.isBurning()) {

                        g.drawImage(
                            fireImage,
                            x + 2,
                            y + 2,
                            cellWidth - 4,
                            cellHeight - 4,
                            this
                        );
                    }
                    else {

                        Image currentImage;

                        if (tree.getGrowthAge() < 30) {

                            currentImage = seedlingImage;

                        }
                        else if (tree.getGrowthAge() < 70) {

                            currentImage = youngTreeImage;

                        }
                        else {

                            currentImage = treeImage;
                        }

                        g.drawImage(
                            currentImage,
                            x + 2,
                            y + 2,
                            cellWidth - 4,
                            cellHeight - 4,
                            this
                        );
                    }
                }

                drawSmoke(g, cell, x, y, cellWidth, cellHeight);
            }
        }
    }

    private void drawSmoke(
            Graphics g,
            Cell cell,
            int x,
            int y,
            int cellWidth,
            int cellHeight) {

        double smoke = cell.getSmokeConcentration();

        if (smoke <= 0.0) {
            return;
        }

        int alpha = (int) Math.min(210, 45 + smoke * 3.0);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(new Color(210, 210, 210, alpha));

        int smokeWidth = Math.max(1, cellWidth);
        int smokeHeight = Math.max(1, cellHeight);

        g2d.fillOval(
            x,
            y,
            smokeWidth,
            smokeHeight
        );

        g2d.dispose();
    }
}
