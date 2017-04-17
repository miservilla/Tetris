public class EllShape extends TetrisPiece2D {

    public EllShape(Block block) {
        super(new Block[][]{new Block[]{block, null},
                            new Block[]{block, null},
                            new Block[]{block, block}});
    }
}