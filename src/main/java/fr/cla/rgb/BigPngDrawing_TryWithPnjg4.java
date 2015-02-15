package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import fr.cla.rgb.examples.FirstTry;
import static fr.cla.rgb.PngDrawing.IMG_TYPE;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;

/**
 */
public class BigPngDrawing_TryWithPnjg4 {

    private final PngDrawing big;
    public BigPngDrawing_TryWithPnjg4(PngDrawing big) {
        this.big = big;
    }
    private String imageFileName() {
        return big.imageFileName();
    }

    final void draw() throws IOException {
        List<String> tiles = IntStream.rangeClosed(1, 4).mapToObj(tileNumber -> {
            //1.1. Render tile
            out.printf("Rendering tile %s n°%d ...%n", big, tileNumber);
            BufferedImage tile = big.render();

            //1.2. Write tile to disk
            String tileFileName = imageFileName() + tileNumber;
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
                imageFileName(),
                2
        );
    }

    public static void main(String[] args) throws IOException {
        new BigPngDrawing_TryWithPnjg4(new FirstTry(128)).draw();
    }
}