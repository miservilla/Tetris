public class JayShape extends TetrisPiece2D {

    public JayShape(Block block) {
        super(new Block[][]{new Block[]{null, block},
                            new Block[]{null, block},
                            new Block[]{block, block}});
    }
}