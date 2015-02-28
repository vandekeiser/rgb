package fr.cla.rgb.drawing;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.function.Function.identity;

public abstract class Drawing implements Named {

    public static final String IMG_TYPE = "png";

    private final int xsize, ysize;
    protected Drawing(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
    }
    public final int xsize() { return xsize; }
    public final int ysize() { return ysize; }

    public NamedImage render() {
        BufferedImage img = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_RGB);

        //__Should__ be threadsafe since we write to different pixels, so could use parallel()
        //..BUT it's already not so great to have side-effect from a lambda..

        //Sometime the drawing's size is used in calculating the RGB values of each pixel.
        //The size of the whole drawing, not the size of the tile, should be used.
        points().forEach(p ->
                img.setRGB(
                        p.x,
                        p.y,
                        RGB(p, wholeDrawingSize())
                )
        );

        return new NamedImage(img, name());
    }

    protected Stream<Point> points() {
        return IntStream.range(0, xsize).mapToObj(
                x -> IntStream.range(0, ysize).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist
    }

    protected int RGB(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        return (R(x, y, s) << 8 | G(x, y, s)) << 8 | B(x, y, s);
    }
    protected abstract int R(int x, int y, int wholeDrawingsize);
    protected abstract int G(int x, int y, int wholeDrawingsize);
    protected abstract int B(int x, int y, int wholeDrawingsize);
    /**
     * Used by some drawings, in addition to the (x, y) pixel coordinate,
     * to compute the pixel's RGB.
     * @return
     */
    protected abstract int wholeDrawingSize();

}