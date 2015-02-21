package fr.cla.rgb;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import static java.util.stream.Collectors.toList;

public class TilingDrawer {

    public static String imageFileName(SquareDrawing drawing) {
        return drawing.getClass().getSimpleName() + "." + Drawing.IMG_TYPE;
    }

    public final void draw(SquareDrawing drawing) {
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
                .toArray(new String[collectedNamedImages.size()]);

        PngjSamples.doTiling (
            collectedImagesNames,
            imageFileName(drawing),
            1 //Image is split into lines, so 1 image per row
        );
    }

}