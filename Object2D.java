/**
 * This interface describes methods that can be performed on a two
 * dimensional object constructed out of Blocks (such as a Tetris piece).
 */
public interface Object2D {

    /**
     * Representation of a two dimensional dimension. This class is
     * fully implemented and needs no further modifications for this
     * assignment.
     */
    public static class Dimension2D {
        protected int height;
        protected int width;

        /**
         * Create a new Dimension2D object with a width, height.
         * @param height Number of rows in the object
         * @param width Number of columns in the object
         */
        public Dimension2D(int height, int width) {
            this.height = height;
            this.width = width;
        }

        /**
         * Get height of this dimension object.
         * @return height of object
         */

        public int getHeight() {
            return height;
        }

        /**
         * Get width in this dimension object.
         * @return width of object
         */
        public int getWidth() {
            return width;
        }

        /**
         * Get the string representation of this object as a pair of
         * numbers surrounded by brackets.
         * @return String representation of the dimension.
         */
        public String toString() {
            return "[" + height + "," + width + "]";
        }
    }

    /** 
     * Rotates this object 90 degrees clockwise.
     */
    void rotate();
    
    /**
     * Get the dimensions of this object.
     * @return This object's dimensions.
     */
    Dimension2D getDimension();

    /**
     * Get a string representation of this object.
     * @return String representation of this object.
     */
    String toString();

    /**
     * Get the Block at the given row and column of this object.
     * @param row Row of interest.
     * @param col Column of interest.
     * @return Block at given coordinates, or null if no block there.
     */
    Block getBlockAt(int row, int col);
}