
import javax.swing.*;
import java.awt.*;

public class ForestDashboard extends JPanel {

    private final Forest forest;

    private final JLabel livingLabel;
    private final JLabel burningLabel;
    private final JLabel smokeLabel;
    private final JLabel severityLabel;
    private final JLabel survivalLabel;
    private final JLabel windLabel;

    public ForestDashboard(Forest forest) {

        this.forest = forest;

        setLayout(new GridLayout(7, 1, 8, 8));
        setBackground(new Color(28, 35, 32));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        setPreferredSize(new Dimension(250, 0));

        JLabel title = new JLabel("FOREST MONITOR");
        title.setForeground(new Color(120, 220, 160));
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        add(title);

        livingLabel = createLabel();
        burningLabel = createLabel();
        smokeLabel = createLabel();
        severityLabel = createLabel();
        survivalLabel = createLabel();
        windLabel = createLabel();

        add(livingLabel);
        add(burningLabel);
        add(smokeLabel);
        add(severityLabel);
        add(survivalLabel);
        add(windLabel);

        updateStatistics();
    }

    private JLabel createLabel() {

        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));

        return label;
    }

    public void updateStatistics() {

        long living = forest.countLivingTrees();
        long burning = forest.countBurningTrees();
        double averageSmoke = forest.getAverageSmoke();

        Cell[][] grid = forest.getGrid();
        int totalCells = grid.length * grid[0].length;

        double survival = totalCells == 0
                ? 0.0
                : (living * 100.0) / totalCells;

        String severity;

        if (averageSmoke < 2.0) {
            severity = "LOW";
        } else if (averageSmoke < 5.0) {
            severity = "MODERATE";
        } else if (averageSmoke < 10.0) {
            severity = "HIGH";
        } else {
            severity = "CRITICAL";
        }

        livingLabel.setText("Living Trees: " + living);
        burningLabel.setText("Burning Trees: " + burning);
        smokeLabel.setText(
                String.format("Average Smoke: %.2f", averageSmoke)
        );
        severityLabel.setText("Smoke Severity: " + severity);
        survivalLabel.setText(
                String.format("Tree Coverage: %.1f%%", survival)
        );

        Wind wind = forest.getWind();

        if (wind != null) {
            windLabel.setText(
                    "Wind: " + wind.getDirection()
                    + " (" + wind.getSpeed() + ")"
            );
        } else {
            windLabel.setText("Wind: None");
        }

        if (severity.equals("LOW")) {
            severityLabel.setForeground(new Color(120, 220, 160));
        } else if (severity.equals("MODERATE")) {
            severityLabel.setForeground(Color.YELLOW);
        } else {
            severityLabel.setForeground(new Color(255, 120, 100));
        }
    }
}
