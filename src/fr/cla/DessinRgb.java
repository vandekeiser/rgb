package fr.cla;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.smartcardio.Card;

public class DessinRgb {

    static final String IMG_FILE = "DessinRgb.png", IMG_TYPE = "png";

    static final int X_SIZE = 256, Y_SIZE = 128;
    static class Point {
        final int x, y;
        Point(int x, int y) {this.x = x; this.y = y;}
    }

    public static void main(String[] args) throws IOException {
        new DessinRgb().dessine();
    }

    private void dessine() throws IOException {
        BufferedImage img = new BufferedImage(X_SIZE, Y_SIZE, BufferedImage.TYPE_INT_RGB);

        Stream<Point> points = pointsStream();
        points.forEach( (Point p) ->  img.setRGB(p.x, p.y, rgb(p)));

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(IMG_FILE))) {
            ImageIO.write(img, IMG_TYPE, out);
        }
    }

    private int rgb(Point p) {
        return (red(p) << 8 | green(p)) << 8 | blue(p);
    }

    private int red(Point p) {
        return p.x + p.y;
    }
    private int green(Point p) {
        return p.x - p.y;
    }
    private int blue(Point p) {
        return p.x * p.y;
    }

    private static Stream<Point> pointsStream() {
        return IntStream.rangeClosed(0, X_SIZE - 1).mapToObj(
                x -> IntStream.rangeClosed(0, Y_SIZE - 1).mapToObj(
                        y -> new Point(x, y)
                )
        )
                .flatMap(Function.identity()); //workaround: pas de Stream.flatMapToObj..
    }
}