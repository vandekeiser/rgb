package fr.cla;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import static java.util.function.Function.identity;
import static java.lang.System.out;

public abstract class RgbDrawing {

    static final String IMG_TYPE = "png";
    private String imageFileName() {
        return getClass().getSimpleName() + "." + IMG_TYPE;
    }

    static class Point {
        final int x, y;
        Point(int x, int y) {this.x = x; this.y = y;}
    }
    protected final void draw() throws IOException {
        final int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        Instant beforeRendering = Instant.now();
        //__Should__ be threadsafe since we write to different pixels
        points().parallel().forEach(p -> img.setRGB(p.x, p.y, rgb(p, size)));
        Instant afterRendering = Instant.now();
        out.printf("Rendering took: %s%n", Duration.between(beforeRendering, afterRendering));

        Instant beforeWrite = Instant.now();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFileName()))) {
            ImageIO.write(img, IMG_TYPE, out);
        }
        Instant afterWrite = Instant.now();
        out.printf("Writing took: %s%n", Duration.between(beforeWrite, afterWrite));
    }

    protected abstract int size();
    private Stream<Point> points() {
        return IntStream.range(0, size()).mapToObj(
                x -> IntStream.range(0, size()).mapToObj(
                        y -> new Point(x, y)
                )
        )
        .flatMap(identity()); //workaround: IntStream.flatMapToObj doesn't exist
    }

    private int rgb(Point p, int size) {
        int x = p.x, y = p.y;
        return (r(x, y, size) << 8 | g(x, y, size)) << 8 | b(x, y, size);
    }
    protected abstract int r(int x, int y, int size);
    protected abstract int g(int x, int y, int size);
    protected abstract int b(int x, int y, int size);

}