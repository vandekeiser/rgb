package fr.cla.rgb;

public class Tile extends Drawing {

    private final SquareDrawing whole;
    private final int xoffset, yoffset;

    public Tile(SquareDrawing whole, int xsize, int ysize, int xoffset, int yoffset) {
        super(xsize, ysize);
        this.whole = whole;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override protected String name() {
        return whole.getClass().getSimpleName()
                + "_"
                + xoffset
                + "_"
                + yoffset
                + "."
                + Drawing.IMG_TYPE;
    }

    @Override protected int R(int x, int y, int wholeDrawingSize) {
        return whole.R(x + xoffset, y + yoffset, wholeDrawingSize);
    }
    @Override protected int G(int x, int y, int wholeDrawingSize) {
        return whole.G(x + xoffset, y + yoffset, wholeDrawingSize);
    }
    @Override protected int B(int x, int y, int wholeDrawingSize) {
        return whole.B(x + xoffset, y + yoffset, wholeDrawingSize);
    }
    public final int wholeDrawingSize() {return whole.size();}

}
