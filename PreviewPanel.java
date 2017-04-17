import java.awt.*;
import javax.swing.*;

public class PreviewPanel extends BlockPanel implements GameBoard.ShapeListener {

    private static final int ROWS = 6;
    private static final int COLS = 4;

    public PreviewPanel(int numRows, int numCols, int cellSize, Color background) {
        super(numRows, numCols, cellSize, background);
    }        

    public PreviewPanel() {
        this(ROWS, COLS, CELL_SIZE, DEFAULT_BACKGROUND);
    }

    public void updateShape(GameBoard.ShapeEvent ev) {
        Object2D obj = ev.getNextShape();
        clear();
        if(obj != null) {
            Object2D.Dimension2D dim = obj.getDimension();
            int row = (numRows - dim.getHeight()) / 2;
            int col = (numCols - dim.getWidth()) / 2;
            placeObject(obj, row, col);
        }
    }
}