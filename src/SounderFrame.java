import javax.swing.*;

public class SounderFrame extends JFrame {
    public SounderFrame() {
        super("Sounder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setResizable(false);
        setVisible(true);
        setIconImage(new ImageIcon("Sounder.png").getImage());
        add(new SounderPanel(this));
    }
}
