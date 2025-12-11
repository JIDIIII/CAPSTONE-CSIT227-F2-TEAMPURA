import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

class TopRoundedPanel extends JPanel {
    private int radius;

    public TopRoundedPanel(int radius) {
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
        path.moveTo(0, h);              // Start at Bottom-Left
        path.lineTo(0, r);              // Draw up to Top-Left curve start
        path.quadTo(0, 0, r, 0);        // Curve to Top-Left
        path.lineTo(w - r, 0);          // Draw Line across Top
        path.quadTo(w, 0, w, r);        // Curve to Top-Right
        path.lineTo(w, h);              // Draw down to Bottom-Right
        path.lineTo(0, h);              // Draw line back to Bottom-Left
        path.closePath();

        // Fill the shape with the background color set in Designer
        g2.setColor(getBackground());
        g2.fill(path);

        super.paintComponent(g);
        g2.dispose();
    }
}