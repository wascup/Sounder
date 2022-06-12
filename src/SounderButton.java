import javax.swing.*;
import java.awt.*;

public class SounderButton extends JButton {
    public SounderButton(String text) {
        super(text);
        setBackground(Color.decode("#606060"));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
    }
}
