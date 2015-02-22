package fr.cla.rgb.drawing;

public class Tile extends Drawing {

    private final WholeDrawing whole;
    private final int xoffset, yoffset;

    private Tile(WholeDrawing whole, int xsize, int ysize, int xoffset, int yoffset) {
        super(xsize, ysize);
        this.whole = whole;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override public String name() {
        return whole.getClass().getSimpleName()
                + "_"
                + xoffset
                + "_"
                + yoffset
                + "."
                + IMG_TYPE;
    }

    //Necessary for the whole drawing to be able to override RGB and not only R, G, and B
    @Override protected int RGB(Point p, int wholeDrawingsize) {
        return whole.RGB(p.offset(xoffset, yoffset), wholeDrawingsize);
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

    public static Builder of(WholeDrawing whole) {
        return new Builder(whole);
    }

    public static class Builder {
        private final WholeDrawing whole;
        private int xsize, ysize, xoffset, yoffset;

        Builder(WholeDrawing whole) {
            this.whole = whole;
        }

        public Builder sized(int xsize, int ysize) {
            this.xsize = xsize;
            this.ysize = ysize;
            return this;
        }

        public Builder offset(int xoffset, int yoffset) {
            this.xoffset = xoffset;
            this.yoffset = yoffset;
            return this;
        }

        public Tile build() {
            return new Tile(whole, xsize, ysize, xoffset, yoffset);
        }
    }
}
