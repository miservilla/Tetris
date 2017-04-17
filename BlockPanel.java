import java.awt.*;
import javax.swing.*;

/*
 * A panel that draws a grid of Blocks.
 */
public class BlockPanel extends JPanel {
    protected static final int CELL_SIZE = 30;
    protected static final Color DEFAULT_BACKGROUND = Color.BLACK;

    protected final int numRows;
    protected final int numCols;
    protected final int cellSize;
    protected final Color backgroundColor;
    protected final int hiddenRows;

    protected Block[][] grid;

    protected BlockPanel(int numRows, int numCols, int cellSize, Color backgroundColor,
                         int hiddenRows) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.cellSize = cellSize;
        this.backgroundColor = backgroundColor;
        this.hiddenRows = hiddenRows;
        grid = new Block[numRows][numCols];
        setBackground(backgroundColor);
        setPreferredSize(new Dimension(numCols*cellSize, (numRows-hiddenRows)*cellSize));
    }

    public BlockPanel(int numRows, int numCols, int cellSize, Color backgroundColor) {
        this(numRows, numCols, cellSize, backgroundColor, 0);
    }

    public BlockPanel(int numRows, int numCols) {
        this(numRows, numCols, CELL_SIZE, DEFAULT_BACKGROUND);
    }

    public void clear() {
        for(int i = 0; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                grid[i][j] = null;
            }
        }
        repaint();
    }

    /**
     * Place an object on the board.
     * @param obj The object to place.
     * @param row The row position
     * @param col The column position
     */
    public void placeObject(Object2D obj, int row, int col) {
        Object2D.Dimension2D dim = obj.getDimension();
        for(int i = 0; i < dim.getHeight(); ++i) {
            for(int j = 0; j < dim.getWidth(); ++j) {
                Block b = obj.getBlockAt(i, j);
                if(b != null) {
                    grid[row + i][col + j] = b;
                }
            }
        }
    }

    protected void paintBlock(Graphics g, Block b, int row, int col) {
        if(row >= hiddenRows) {
            b.paint(g, col*cellSize, (row-hiddenRows)*cellSize, cellSize);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        for(int i = hiddenRows; i < numRows; ++i) {
            for(int j = 0; j < numCols; ++j) {
                if(grid[i][j] != null) {
                    paintBlock(g, grid[i][j], i, j);
                }
            }
        }        
    }
}