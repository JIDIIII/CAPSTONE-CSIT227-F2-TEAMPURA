import javax.swing.*;
import java.awt.*;

class RoundedPanel extends JPanel {
    private int cornerRadius = 15;

    public RoundedPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        setBackground(bgColor);
        setOpaque(false); // Make transparent so corners appear round
    }

    // Default constructor for simple usage
    public RoundedPanel(int radius) {
        this(radius, Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded panel
        if (getBackground() != null) {
            graphics.setColor(getBackground());
            graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
        }
    }
}