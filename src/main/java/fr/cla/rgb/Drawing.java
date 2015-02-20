package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

public abstract class Drawing {

    static final String IMG_TYPE = "png";

    public BufferedImage render() {
        int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            //__Should__ be threadsafe since we write to different pixels, so could use parallel()
        points().forEach(p -> img.setRGB(p.x, p.y, rgb(p, size)));
        return img;
    }

    protected abstract int size();
    private Stream<Point> points() {
//        //DiagonalSierpinsky(8192): Rendering took: PT6.194S
//        //JuliaSet(8192): Rendering took: PT4M19.659S
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist

        //DiagonalSierpinsky(8192): Rendering took: PT8.551S
        //JuliaSet(8192): Rendering took: PT2M29.091S
        return IntStream.range(0, size()).mapToObj(
                x -> IntStream.range(0, size()).mapToObj(
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

    private int rgb(Point p, int size) {
        int x = p.x, y = p.y;
        return (r(x, y, size) << 8 | g(x, y, size)) << 8 | b(x, y, size);
    }
    protected abstract int r(int x, int y, int size);
    protected abstract int g(int x, int y, int size);
    protected abstract int b(int x, int y, int size);

    @Override public String toString() {
        return String.format("%s {size:%d}", getClass().getSimpleName(), size());
    }

//    Spliterator<PngDrawing> spliterator() {
//        return new PngDrawingSpliterator(this);
//    }
}