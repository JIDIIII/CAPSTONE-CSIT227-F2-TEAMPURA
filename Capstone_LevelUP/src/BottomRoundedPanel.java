import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

class BottomRoundedPanel extends JPanel {
    private int radius;

    public BottomRoundedPanel(int radius) {
        super();
        this.radius = radius;
        setOpaque(false); // Essential for transparency
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int r = radius;

        // Create the custom path
        Path2D.Float path = new Path2D.Float();
        path.moveTo(0, 0);              // Start at Top-Left
        path.lineTo(w, 0);              // Draw to Top-Right
        path.lineTo(w, h - r);          // Draw down to start of Bottom-Right curve
        path.quadTo(w, h, w - r, h);    // Curve to Bottom-Right
        path.lineTo(r, h);              // Draw line to start of Bottom-Left curve
        path.quadTo(0, h, 0, h - r);    // Curve to Bottom-Left
        path.lineTo(0, 0);              // Draw up to Top-Left
        path.closePath();

        // Fill the shape with the background color
        g2.setColor(getBackground());
        g2.fill(path);

        super.paintComponent(g);
        g2.dispose();
    }
}