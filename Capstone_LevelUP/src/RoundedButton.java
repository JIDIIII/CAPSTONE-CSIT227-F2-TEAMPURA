import javax.swing.*;
import java.awt.*;

class RoundedButton extends JButton {
    private int radius;

    public RoundedButton(int radius) {
        super();
        this.radius = radius;

        // Disable the default square background and border
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Get the background color set in properties/GUI Designer
        g2.setColor(getBackground());

        // 2. Draw the rounded shape
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // 3. Let Swing draw the text and icon normally
        super.paintComponent(g);
    }
}