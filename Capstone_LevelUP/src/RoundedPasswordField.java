import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class RoundedPasswordField extends JPasswordField {
    private int radius;

    public RoundedPasswordField(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
        setBorder(new EmptyBorder(5, 15, 5, 15)); // Padding
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("SansSerif", Font.PLAIN, 18));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        // Draw border
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }
}