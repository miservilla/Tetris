import javax.swing.SwingUtilities;

public class Tetris {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    TetrisFrame frame = new TetrisFrame();
                    frame.setVisible(true);
                }
            });
    }
}