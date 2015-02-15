package fr.cla;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;

public abstract class AbstractDessinRgb {

    static final String IMG_TYPE = "png";

    static class Point {
        final int x, y;
        Point(int x, int y) {this.x = x; this.y = y;}
    }

    protected final void dessine() throws IOException {
        BufferedImage img = new BufferedImage(xsize(), ysize(), BufferedImage.TYPE_INT_RGB);

        points().forEach((Point p) -> img.setRGB(p.x, p.y, rgb(p)));

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(
                getClass().getSimpleName() + "." + IMG_TYPE
        ))) {
            ImageIO.write(img, IMG_TYPE, out);
        }
    }

    protected abstract int xsize();
    protected abstract int ysize();
    private Stream<Point> points() {
        return IntStream.rangeClosed(0, xsize() - 1).mapToObj(
                x -> IntStream.rangeClosed(0, ysize() - 1).mapToObj(
                        y -> new Point(x, y)
                )
        )
        .flatMap(Function.identity()); //workaround: pas de Stream.flatMapToObj..
    }

    private int rgb(Point p) {
            return (red(p) << 8 | green(p)) << 8 | blue(p);
        }
    protected abstract int red(Point p);
    protected abstract int green(Point p);
    protected abstract int blue(Point p);
}