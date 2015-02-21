package fr.cla.rgb;

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
    public int ysize() {
            return ysize;
        }

    public Stream<Point> points() {
        return IntStream.range(0, xsize).mapToObj(
                x -> IntStream.range(0, ysize).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist
    }

    protected final int rgb(Point p, int size) {
        int x = p.x, y = p.y;
        return (r(x, y, size) << 8 | g(x, y, size)) << 8 | b(x, y, size);
    }
    protected abstract int r(int x, int y, int size);
    protected abstract int g(int x, int y, int size);
    protected abstract int b(int x, int y, int size);

}