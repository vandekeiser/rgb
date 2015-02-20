package fr.cla.rgb;

public class Tile extends PngDrawing {

    private final PngDrawing whole;
    private final int xoffset, yoffset;

    public Tile(PngDrawing whole) {
        this(whole, 0, 0);
    }

    public Tile(PngDrawing pngDrawing, int xoffset, int yoffset) {
        this.whole = pngDrawing;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override protected int size() {
        return 0;
    }

    @Override protected int r(int x, int y, int size) {
        return whole.r(x+xoffset, y+yoffset, size);
    }

    @Override protected int g(int x, int y, int size) {
        return whole.g(x+xoffset, y+yoffset, size);
    }

    @Override protected int b(int x, int y, int size) {
        return whole.b(x+xoffset, y+yoffset, size);
    }
}
