package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.function.Function.identity;

public class Tile extends Drawing {

    private final Drawing whole;
    private final int xoffset, yoffset;

    public Tile(Drawing whole) {
        this(whole, 0, 0);
    }

    public Tile(Drawing pngDrawing, int xoffset, int yoffset) {
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

    public Stream<Point> points() {
//        //DiagonalSierpinsky(8192): Rendering took: PT6.194S
//        //JuliaSet(8192): Rendering took: PT4M19.659S
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist

        //DiagonalSierpinsky(8192): Rendering took: PT8.551S
        //JuliaSet(8192): Rendering took: PT2M29.091S
        return IntStream.range(xoffset, xoffset+size()).mapToObj(
                x -> IntStream.range(yoffset, yoffset+size()).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()).parallel(); //Workaround: IntStream.flatMapToObj doesn't exist

        //OOME
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()).collect(toList()).stream(); //Workaround: IntStream.flatMapToObj doesn't exist
    }

    public BufferedImage render() {
        int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            //__Should__ be threadsafe since we write to different pixels, so could use parallel()
        points().forEach(p -> img.setRGB(p.x, p.y, rgb(p, size)));
        return img;
    }
}
