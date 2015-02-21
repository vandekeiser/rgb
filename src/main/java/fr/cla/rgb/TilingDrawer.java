package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import static java.lang.System.out;

public class TilingDrawer {

    public static String imageFileName(Drawing drawing) {
        return drawing.getClass().getSimpleName() + "." + Drawing.IMG_TYPE;
    }

    public final void draw(Drawing drawing) {
        //out.println("Rendering...");
        //Instant beforeRendering = Instant.now();
        Stream<Tile> tiling = drawing.split();

        Stream<NamedImage> namedImages = tiling.map(Tile::renderTile);

        namedImages.forEach( t -> {
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(t.getName()))) {
                ImageIO.write(t.getImage(), Drawing.IMG_TYPE, out);
            }catch (IOException e) { throw new UncheckedIOException(e); };
        });
    }

}