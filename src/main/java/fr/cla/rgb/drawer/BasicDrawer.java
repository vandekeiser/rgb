package fr.cla.rgb.drawer;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.WholeDrawing;
import static java.lang.System.out;

public class BasicDrawer implements Drawer {

    @Override public final void draw(WholeDrawing drawing) {
        try {
            doDraw(drawing);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void doDraw(WholeDrawing drawing) throws IOException {
        //1. Rendering
        out.println("Rendering...");
        Instant beforeRendering = Instant.now();
        BufferedImage img = drawing.render().image;
        Instant afterRendering = Instant.now();
        out.printf("Rendering took: %s%n", Duration.between(beforeRendering, afterRendering));

        //2. Writing to file
        out.println("Writing to file...");
        Instant beforeWrite = Instant.now();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(drawing.name()))) {
            ImageIO.write(img, Drawing.IMG_TYPE, out);
        }
        Instant afterWrite = Instant.now();
        out.printf("Writing to file took: %s%n", Duration.between(beforeWrite, afterWrite));
    }
}