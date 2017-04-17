public class TeeShape extends TetrisPiece2D {

    public TeeShape(Block block) {
        super(new Block[][]{new Block[]{block, null},
                            new Block[]{block, block},
                            new Block[]{block, null}});
    }
}