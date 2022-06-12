import javax.swing.*;
import java.awt.*;

public class SongInfo extends JLabel {
    public SongInfo(String title) {
        super(title);
        setFont(new Font("Arial", Font.PLAIN, 12));
        setForeground(Color.WHITE);
        setBounds(10, 10, 200, 25);
        setAlignmentX(LEFT_ALIGNMENT);
    }
}
