package fr.cla.rgb;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import static java.util.stream.Collectors.toList;

public class TilingDrawer {

    public final void draw(SquareDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();

        //1. Get paths of temp tiles
        String[] collectedImagesPaths = drawing.split()
                .map(Drawing::name)
                .map(t -> toPath(t, tempTilesPath))
                .collect(Collectors.toList())
                .toArray(new String[0]);

        //2. Write temp tiles without holding on to any BufferedImage
        drawing.split().map(Drawing::render).forEach(t -> {
            try (OutputStream out = outputStreamFor(t, tempTilesPath)) {
                ImageIO.write(t.getImage(), Drawing.IMG_TYPE, out);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        //3. Use PNGJ (https://code.google.com/p/pngj/wiki/Snippets) to stick the tiles together
        PngjSamples.doTiling (
            collectedImagesPaths,
            drawing.name(),
            1 //Image is split into lines, so 1 image per row
        );
    }

    private String toPath(String tileName, Path tempTilesPath) {
        return tempTilesPath.resolve(tileName).toString();
    }

    private Path createTempTilesPath() {
        try {
            Path temp = Files.createTempDirectory(
                    Paths.get(System.getProperty("java.io.tmpdir")),
                    "tiles_"
            );
            System.out.println("TilingDrawer using temp: " + temp);
            return temp;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private OutputStream outputStreamFor(NamedImage t, Path tempTilesPath) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(
                toPath(t.getName(), tempTilesPath))
        );
    }

}