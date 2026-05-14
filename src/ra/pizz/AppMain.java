package ra.pizz;

import javax.swing.SwingUtilities;
import ra.pizz.ui.MainFrame;

public class AppMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
