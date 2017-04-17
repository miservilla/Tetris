public class ZeeShape extends TetrisPiece2D {
    
    public ZeeShape(Block block) {
        super(new Block[][]{new Block[]{block, null},
                            new Block[]{block, block},
                            new Block[]{null, block}});
    }
}
