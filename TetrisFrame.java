import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TetrisFrame extends JFrame implements GameBoard.ScoreListener {
    private static final String START = "Start";
    private static final String PAUSE = "Pause";
    
    private static final int DELAY = 1000;
    
    private JButton pauseButton;
    private boolean isPaused = true;

    private JLabel scoreLabel = new JLabel();
    private JLabel linesLabel = new JLabel();
    private JLabel levelLabel = new JLabel();

    private GameBoard board;
    private Timer timer;

    public TetrisFrame() {
        super("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new GameBoard();
        board.addScoreListener(this);
        timer = new Timer(DELAY, board);
        timer.setInitialDelay(0);
        JPanel boardPanel = new JPanel();
        boardPanel.add(board);

        pauseButton = new JButton(START);
        pauseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    isPaused = !isPaused;
                    pauseButton.setText(isPaused ? START : PAUSE);
                    board.setPlaying(!isPaused);
                    if(isPaused) {
                        timer.stop();
                    } else {
                        timer.start();
                        board.requestFocusInWindow();
                    }
                }
            });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(pauseButton);

        PreviewPanel preview = new PreviewPanel();
        board.addShapeListener(preview);
        JPanel previewPanel = new JPanel();
        previewPanel.setBorder(BorderFactory.createTitledBorder("Next Piece"));
        previewPanel.add(preview);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
        scorePanel.setBorder(BorderFactory.createEtchedBorder());
        scorePanel.add(scoreLabel);
        scorePanel.add(linesLabel);
        scorePanel.add(levelLabel);
        updateScore(0,0,0);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(previewPanel, BorderLayout.PAGE_START);
        rightPanel.add(scorePanel, BorderLayout.PAGE_END);

        getContentPane().add(boardPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        getContentPane().add(rightPanel, BorderLayout.LINE_END);
        pack();
    }

    public void updateScore(GameBoard.ScoreEvent ev) {
        updateScore(ev.score, ev.lines, ev.level);
        if(ev.isGameOver) {
            isPaused = true;
            board.setPlaying(false);
            pauseButton.setText("GAME OVER");
            pauseButton.setEnabled(false);
        }
    }

    private void updateScore(final int score, final int lines, final int level) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    scoreLabel.setText("Score: " + score);
                    linesLabel.setText("Lines: " + lines);
                    levelLabel.setText("Level: " + level);
                }
            });
    }
}