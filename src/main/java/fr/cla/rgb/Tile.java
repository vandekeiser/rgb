package fr.cla.rgb;

import java.awt.image.BufferedImage;

public class Tile extends Drawing {

    private final SquareDrawing whole;
    private final int xoffset, yoffset;

    public Tile(SquareDrawing drawing, int xsize, int ysize, int xoffset, int yoffset) {
        super(xsize, ysize);
        this.whole = drawing;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }
    public final int wholeDrawingSize() {return whole.size();}
    @Override protected String name() {
        return whole.getClass().getSimpleName()+"_"+xoffset+"_"+yoffset+Drawing.IMG_TYPE;
    }

    @Override protected int r(int x, int y, int wholeDrawingSize) {
        return whole.r(x+xoffset, y+yoffset, wholeDrawingSize);
    }
    @Override protected int g(int x, int y, int wholeDrawingSize) {
        return whole.g(x+xoffset, y+yoffset, wholeDrawingSize);
    }
    @Override protected int b(int x, int y, int wholeDrawingSize) {
        return whole.b(x+xoffset, y+yoffset, wholeDrawingSize);
    }

}
