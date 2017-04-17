public class EyeShape extends TetrisPiece2D {

    public EyeShape(Block block) {
        super(new Block[][]{new Block[]{block},
                            new Block[]{block},
                            new Block[]{block},
                            new Block[]{block}});
    }
}
