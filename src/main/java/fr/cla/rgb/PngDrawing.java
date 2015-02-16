package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.imageio.ImageIO;
import static java.lang.System.out;
import static java.util.function.Function.identity;

/**
 * Heavily inspired by:
 *  -https://codegolf.stackexchange.com/questions/35569/tweetable-mathematical-art
 *  -https://codegolf.stackexchange.com/questions/22144/images-with-all-colors
 *
 * The goal is to:
 *  -make the code easier to extend to any drawing by factoring points generation and rendering,
 *   so that concrete classes only need to implement the RGB computation
 *  -use [parallel] streams
 *
 * Times are measured on an old 2-cores machine
 * NOT SET: -Djava.util.concurrent.ForkJoinPool.common.parallelism=2
 */
public abstract class PngDrawing {

    static final String IMG_TYPE = "png";
    String imageFileName() {
        return getClass().getSimpleName() + "." + IMG_TYPE;
    }

    final int xmin, xmax, ymin, ymax;
    protected PngDrawing(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }
    protected PngDrawing() {
        this(-1, -1, -1, -1);
    }

    protected final void draw() throws IOException {
        //1. Rendering
        out.println("Rendering...");
        Instant beforeRendering = Instant.now();
        BufferedImage img = render();
        Instant afterRendering = Instant.now();
        out.printf("Rendering took: %s%n", Duration.between(beforeRendering, afterRendering));

        //2. Writing to file
        out.println("Writing to file...");
        Instant beforeWrite = Instant.now();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFileName()))) {
            ImageIO.write(img, IMG_TYPE, out);
        }
        Instant afterWrite = Instant.now();
        out.printf("Writing to file took: %s%n", Duration.between(beforeWrite, afterWrite));
    }

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
        return IntStream.range(xmin(), xmax()).mapToObj(
                x -> IntStream.range(xmin(), xmax()).mapToObj(
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

    private int xmin() { return xmin!=-1 ? xmin : 0; }
    private int xmax() { return xmin!=-1 ? xmin : size(); }
    private int ymin() { return ymin!=-1 ? ymin : 0; }
    private int ymax() { return ymin!=-1 ? ymin : size(); }

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

    public boolean isSmallEnough() {
        return size()<=128; //For tests; OOME at 4096*4
    }

    public Stream<PngDrawing> split() {
        Spliterator<PngDrawing> thisSpliterator = new PngDrawingSpliterator(this);
        return StreamSupport.stream(thisSpliterator, true);
    }

//    public Pair<PngDrawing> splitIntoTwo() {
//        return new Pair<>(
//            new PngDrawing(xmin(), xmin()+xmax)/2);
//        );
//    }

//    Spliterator<PngDrawing> spliterator() {
//        return new PngDrawingSpliterator(this);
//    }
}