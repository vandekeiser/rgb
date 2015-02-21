package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.function.Function.identity;

public abstract class Drawing {

    public static final String IMG_TYPE = "png";

    private final int xsize, ysize;
    protected Drawing(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
    }
    public int xsize() { return xsize; }
    public int ysize() { return ysize; }

    public Stream<Point> points() {
        return IntStream.range(0, xsize).mapToObj(
                x -> IntStream.range(0, ysize).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist
    }

    public NamedImage render() {
        int wholeDrawingSize = wholeDrawingSize();
        BufferedImage img = new BufferedImage(xsize, ysize, BufferedImage.TYPE_INT_RGB);

        //__Should__ be threadsafe since we write to different pixels, so could use parallel()
        //The size of the whole drawing is used in calculating the RGB values of each pixel
        points().forEach(p -> img.setRGB(p.x, p.y, rgb(p, wholeDrawingSize)));

        String name = name();
        return new NamedImage(img, name);
    }
    protected abstract String name();

    protected final int rgb(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        return (r(x, y, s) << 8 | g(x, y, s)) << 8 | b(x, y, s);
    }
    protected abstract int r(int x, int y, int wholeDrawingsize);
    protected abstract int g(int x, int y, int wholeDrawingsize);
    protected abstract int b(int x, int y, int wholeDrawingsize);
    protected abstract int wholeDrawingSize();

}