public abstract class TetrisPiece2D implements Object2D, Cloneable {
    private Block[][] shape;

    protected TetrisPiece2D(Block[][] shape) {
        this.shape = shape;
    }
    
    public void rotate() {
        Dimension2D dim = getDimension();
        int height = dim.getHeight();
        int width = dim.getWidth();
        Block[][] rotated = new Block[width][height];
        for ( int row = 0; row < width; row++ ) {
            for ( int col = 0; col < height; col++ ) {
                rotated[row][col] = shape[height-1-col][row];
            }
        }
        shape = rotated;
    }

    public String toString() {
        Dimension2D dim = getDimension();
        String s = "";
        for ( int row = 0; row < dim.getHeight(); row++ ) {
            for ( int col = 0; col < dim.getWidth(); col++ ) {
                s += shape[row][col] == null ? " " : "*";
            }
            s += "\n";
        }
        return s;            
    }

    public Dimension2D getDimension () {
        if ( shape != null ) {
            return new Dimension2D ( shape.length,
                                     shape.length > 0 ? shape[0].length : 0);
        } else {
            return new Dimension2D ( 0, 0 );
        }
    }

    public Block getBlockAt(int row, int col) {
        return shape[row][col];
    }

    public TetrisPiece2D clone() {
        try{
            return (TetrisPiece2D) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
}
    