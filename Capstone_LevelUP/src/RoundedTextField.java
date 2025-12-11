import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class RoundedTextField extends JTextField {
    private int radius;

    public RoundedTextField(int radius) {
        super();
        this.radius = radius;
        setOpaque(false); // Transparent background so corners show

        // Add padding (Top, Left, Bottom, Right) so text doesn't touch the curve
        setBorder(new EmptyBorder(5, 15, 5, 15));

        // Default style
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("SansSerif", Font.PLAIN, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw the rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // 2. Draw the rounded border (optional, makes it visible on white backgrounds)
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // 3. Draw the text (super handles the actual text rendering)
        super.paintComponent(g);
        g2.dispose();
    }
}