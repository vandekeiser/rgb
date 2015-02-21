package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import javax.imageio.ImageIO;
import static java.lang.System.out;

public class BasicDrawer {

    public static String imageFileName(Drawing drawing) {
        return drawing.getClass().getSimpleName() + "." + Drawing.IMG_TYPE;
    }

    public final void draw(SquareDrawing drawing) {
        try {
            doDraw(drawing);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void doDraw(SquareDrawing drawing) throws IOException {
        //1. Rendering
        out.println("Rendering...");
        Instant beforeRendering = Instant.now();
        BufferedImage img = drawing.render().getImage();
        Instant afterRendering = Instant.now();
        out.printf("Rendering took: %s%n", Duration.between(beforeRendering, afterRendering));

        //2. Writing to file
        out.println("Writing to file...");
        Instant beforeWrite = Instant.now();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFileName(drawing)))) {
            ImageIO.write(img, Drawing.IMG_TYPE, out);
        }
        Instant afterWrite = Instant.now();
        out.printf("Writing to file took: %s%n", Duration.between(beforeWrite, afterWrite));
    }
}