import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class GameBoard extends BlockPanel implements ActionListener {
    
    public static class ScoreEvent {
        public final int score;
        public final int lines;
        public final int level;
        public final boolean isGameOver;
        public ScoreEvent(int score, int lines, int level, boolean isGameOver) {
            this.score = score;
            this.lines = lines;
            this.level = level;
            this.isGameOver = isGameOver;
        }
    }

    public static interface ScoreListener {
        void updateScore(ScoreEvent ev);
    }

    public static class ShapeEvent {
        private final Object2D next;
        private final Object2D current;

        public ShapeEvent(Object2D next, Object2D current) {
            this.next = next;
            this.current = current;
        }
        Object2D getNextShape() { return next; }
        Object2D getCurrentShape() { return current; }
    }

    public static interface ShapeListener {
        void updateShape(ShapeEvent ev);
    }

    private static final int ROWS = 20;
    private static final int COLS = 10;
    private static final int HIDDEN = 2;
    private static final int LINES_PER_LEVEL = 10;

    private static final Random rand = new Random();

    private boolean isPlaying = false;
    private int score;
    private int lines;
    private int level;
    private boolean gameOver = false;

    private TetrisPiece2D[] shapes;
    private TetrisPiece2D nextShape;
    private TetrisPiece2D currentShape;
    private int row;
    private int col;

    private Set<ScoreListener> scoreListeners = new HashSet<ScoreListener>();
    private Set<ShapeListener> shapeListeners = new HashSet<ShapeListener>();

    public GameBoard(int numRows, int numCols, int cellSize,
                     Color backgroundColor, int hidden) {
        super(numRows, numCols, cellSize, backgroundColor, hidden);
        shapes = createShapes();
        addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent ev) {
                    if(isPlaying) {
                        switch (ev.getKeyCode()) {
                        case KeyEvent.VK_DOWN:
                            moveCurrentDown(false);
                            break;
                        case KeyEvent.VK_LEFT:
                            moveCurrentLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            moveCurrentRight();
                            break;
                        case KeyEvent.VK_UP:
                            rotateCurrent();
                            break;
                        case KeyEvent.VK_SPACE:
                            moveCurrentDown(true);
                            break;
                        }
                    }
                }
            });            
    }

    public GameBoard() {
        this(ROWS+HIDDEN, COLS, CELL_SIZE, DEFAULT_BACKGROUND, HIDDEN);
    }

    public void actionPerformed(ActionEvent ev) {
        if(isPlaying) {
            moveCurrentDown(false);
        }
    }

    private Block makeBlock(Color color) {
        return new Block(color, backgroundColor);
    }

    private TetrisPiece2D[] createShapes() {
        TetrisPiece2D[] shapes = {
            new EllShape(makeBlock(Color.ORANGE)),
            new JayShape(makeBlock(Color.BLUE)),
            new EyeShape(makeBlock(Color.CYAN)),
            new OhShape(makeBlock(Color.YELLOW)),
            new EssShape(makeBlock(Color.GREEN)),
            new TeeShape(makeBlock(Color.MAGENTA)),
            new ZeeShape(makeBlock(Color.RED))
        };
        return shapes;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if(isPlaying) {
            while(currentShape == null) {
                chooseNextShape();
            }
        }
    }

    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }
 
    public void removeScoreListener(ScoreListener listener) {
        scoreListeners.remove(listener);
    }

    private void updateScoreListeners() {
        ScoreEvent event = new ScoreEvent(score, lines, level, gameOver);
        for(ScoreListener listener : scoreListeners) {
            listener.updateScore(event);
        }
    }

    public void addShapeListener(ShapeListener listener) {
        shapeListeners.add(listener);
    }

    public void removeShapeListener(ShapeListener listener) {
        shapeListeners.remove(listener);
    }

    private void updateShapeListeners() {
        ShapeEvent event = new ShapeEvent(nextShape, currentShape);
        for(ShapeListener listener : shapeListeners) {
            listener.updateShape(event);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(currentShape != null) {
            Object2D.Dimension2D dim = currentShape.getDimension();
            for(int i = 0; i < dim.getHeight(); ++i) {
                for (int j = 0; j < dim.getWidth(); ++j) {
                    Block b = currentShape.getBlockAt(i, j);
                    if(b != null) {
                        paintBlock(g, b, row+i, col+j);
                    }
                }
            }
        }
    }

    private void chooseNextShape() {
        int index = rand.nextInt(shapes.length);
        currentShape = nextShape == null ? null : nextShape.clone();
        nextShape = shapes[index]; 
        int shapeWidth = currentShape == null ? 0 : 
            currentShape.getDimension().getWidth();
        row = 0;
        col = (numCols - shapeWidth) / 2;

        if(currentShape != null && isOverlappingBottom()) {
            gameOver = true;
            updateScoreListeners();
        } else {
            updateShapeListeners();
        }
    }

    private boolean placeIfTouchingBottom() {
        if(isTouchingBottom()) {
            placeObject(currentShape, row, col);
            updateLines();
            chooseNextShape();
            return true;
        }
        return false;
    }

    private void updateLines() {
        int currLines = 0;
        int shapeHeight = currentShape.getDimension().getHeight();
        for(int i = row + shapeHeight - 1; i >= 0; --i) {
            boolean isLine = true;
            for(int j = 0; j < numCols && isLine; ++j) {
                isLine = grid[i][j] != null;
            }
            if(isLine) {
                currLines++;
                for(int j = i; j > 0; --j) {
                    grid[j] = grid[j-1];
                }
                grid[0] = new Block[numCols];
                ++i;
            }
        }
        
        if(currLines > 0) {
            int oldMod = lines % LINES_PER_LEVEL;
            lines += currLines;
            int newMod = lines % LINES_PER_LEVEL;
            if(oldMod > newMod) {
                level++;
            }
            int linePoints = 0;
            switch(currLines) {
            case 1: linePoints = 40; break;
            case 2: linePoints = 100; break;
            case 3: linePoints = 300; break;
            case 4: linePoints = 1200; break;
            }
            score += linePoints*(level+1);
            updateScoreListeners();
        }
    }

    private void moveCurrentDown(boolean dropPiece) {
        boolean placed = false;
        do {
        row++;
        placed = placeIfTouchingBottom();
        } while(dropPiece && !placed);
        repaint();
    }

    private void moveCurrentLeft() {
        if(!isTouchingLeft()) {
            col--;
            placeIfTouchingBottom();
            repaint();
        }
    }

    private void moveCurrentRight() {
        if(!isTouchingRight()) {
            col++;
            placeIfTouchingBottom();
            repaint();
        }
    }

    private void rotateCurrent() {
        currentShape.rotate();
        adjustPosition();
        placeIfTouchingBottom();
        repaint();
    }

    private void adjustPosition() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        if(col + dim.getWidth() > numCols) {
            col = numCols-dim.getWidth();
        }
        if(row + dim.getHeight() > numRows) {
            row = numRows - dim.getHeight();
        }
        while(isOverlappingBottom() && row > 0) {
            row--;
        }
    }

    private boolean isOverlappingBottom() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = dim.getHeight()-1; i >= 0; --i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i,j) != null) {
                    if(row + i + 1 >= numRows ||
                       grid[row + i][col + j] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isTouchingBottom() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = dim.getHeight()-1; i >= 0; --i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i,j) != null) {
                    if(row + i + 1 >= numRows ||
                       grid[row + i + 1][col + j] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isTouchingLeft() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = 0; i < dim.getHeight(); ++i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i, j) != null) {
                    if(col + j <= 0 ||
                       grid[row + i][col + j - 1] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isTouchingRight() {
        Object2D.Dimension2D dim = currentShape.getDimension();
        for(int i = 0; i < dim.getHeight(); ++i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                if(currentShape.getBlockAt(i, j) != null) {
                    if(col + j + 1 >= numCols ||
                       grid[row + i][col + j + 1] != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}