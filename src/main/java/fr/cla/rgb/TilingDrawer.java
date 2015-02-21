package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import static java.lang.System.out;
import static java.util.stream.Collectors.*;

public class TilingDrawer {

    public static String imageFileName(Drawing drawing) {
        return drawing.getClass().getSimpleName() + "." + Drawing.IMG_TYPE;
    }

    public final void draw(Drawing drawing) {
        //out.println("Rendering...");
        //Instant beforeRendering = Instant.now();
        List<NamedImage> collectedNamedImages = drawing.split()
                .map(Tile::renderTile)
                .collect(toList());

        collectedNamedImages.stream().forEach(t -> {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(t.getName()))) {
                ImageIO.write(t.getImage(), Drawing.IMG_TYPE, out);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        String[] collectedImagesNames = collectedNamedImages.stream()
                .map(NamedImage::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        PngjSamples.doTiling (
            collectedImagesNames,
            "toto.png",
            2
        );
    }

}