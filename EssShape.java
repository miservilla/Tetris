public class EssShape extends TetrisPiece2D {

    public EssShape(Block block) {
        super(new Block[][]{new Block[]{null, block},
                            new Block[]{block, block},
                            new Block[]{block, null}});
    }
}