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
        final int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        points().forEach((Point p) -> img.setRGB(p.x, p.y, rgb(p, size)));

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(
                getClass().getSimpleName() + "." + IMG_TYPE
        ))) {
            ImageIO.write(img, IMG_TYPE, out);
        }
    }

    protected abstract int size();
    private Stream<Point> points() {
        return IntStream.range(0, size()).mapToObj(
                x -> IntStream.range(0, size()).mapToObj(
                        y -> new Point(x, y)
                )
        )
        .flatMap(Function.identity()); //workaround: pas de Stream.flatMapToObj..
    }

    private int rgb(Point p, int size) {
        int x = p.x, y = p.y;
        return (r(x, y, size) << 8 | g(x, y, size)) << 8 | b(x, y, size);
    }
    protected abstract int r(int x, int y, int size);
    protected abstract int g(int x, int y, int size);
    protected abstract int b(int x, int y, int size);
}