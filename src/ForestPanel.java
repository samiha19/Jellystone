
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

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

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

                int fullWidth = Math.max(1, cellWidth - 1);
                int fullHeight = Math.max(1, cellHeight - 1);

                boolean showLightning =
                        lightning != null &&
                        cell == currentLightningCell &&
                        System.currentTimeMillis()
                                - lightningStartTime < 3000;

                if (showLightning) {

                    g.drawImage(
                            lightningImage,
                            x,
                            y,
                            fullWidth,
                            fullHeight,
                            this
                    );

                } else if (cell.hasTree()) {

                    Tree tree = cell.getTree();

                    if (tree.isBurning()) {

                        g.drawImage(
                                fireImage,
                                x,
                                y,
                                fullWidth,
                                fullHeight,
                                this
                        );

                    } else {

                        int age = tree.getGrowthAge();

                        Image currentImage;

                        if (age < 30) {

                            currentImage = seedlingImage;

                        } else if (age < 70) {

                            currentImage = youngTreeImage;

                        } else {

                            currentImage = treeImage;
                        }

                        double progress =
                                Math.max(
                                        0.0,
                                        Math.min(1.0, age / 100.0)
                                );

                        double scale =
                                0.75 + progress * 0.25;

                        int imageWidth = Math.max(
                                1,
                                (int) Math.round(fullWidth * scale)
                        );

                        int imageHeight = Math.max(
                                1,
                                (int) Math.round(fullHeight * scale)
                        );

                        int imageX =
                                x + (cellWidth - imageWidth) / 2;

                        int imageY =
                                y + cellHeight - imageHeight;

                        g.drawImage(
                                currentImage,
                                imageX,
                                imageY,
                                imageWidth,
                                imageHeight,
                                this
                        );
                    }
                }

                drawSmoke(
                        g,
                        cell,
                        x,
                        y,
                        cellWidth,
                        cellHeight
                );
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

        int alpha = (int) Math.min(
                210,
                45 + smoke * 3.0
        );

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(
                new Color(210, 210, 210, alpha)
        );

        g2d.fillOval(
                x,
                y,
                Math.max(1, cellWidth),
                Math.max(1, cellHeight)
        );

        g2d.dispose();
    }
}
