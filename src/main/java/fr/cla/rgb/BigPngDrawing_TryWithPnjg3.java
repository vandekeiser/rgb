package fr.cla.rgb;

import fr.cla.rgb.examples.FirstTry;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

import static fr.cla.rgb.Drawing.IMG_TYPE;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

/**
 */
public class BigPngDrawing_TryWithPnjg3 {

    private final Drawing big;
    private final String imageFileName;

    public BigPngDrawing_TryWithPnjg3(Drawing big) {
        this.big = big;
        this.imageFileName = Drawer.imageFileName(this.big);
    }

    final void draw() throws IOException {
        List<String> tiles = IntStream.rangeClosed(1, 4).mapToObj(tileNumber -> {
            //1.1. Render tile
            out.printf("Rendering tile %s n°%d ...%n", big, tileNumber);
            BufferedImage tile = big.render();

            //1.2. Write tile to disk
            String tileFileName = imageFileName + tileNumber;
            out.printf("Writing tile %s n°%d to file %s ...%n", big, tileNumber, tileFileName);
            try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tileFileName))) {
                ImageIO.write(tile, IMG_TYPE, out);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return tileFileName;
        }).collect(toList());

        //2. Stick tiles together
        PngjSamples.doTiling(
                tiles.toArray(new String[tiles.size()]),
                imageFileName,
                2
        );
    }

    public static void main(String[] args) throws IOException {
        new BigPngDrawing_TryWithPnjg3(new FirstTry(128)).draw();
    }
}