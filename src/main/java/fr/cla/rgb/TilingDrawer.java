package fr.cla.rgb;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import static java.lang.System.out;

public class TilingDrawer {

    public final void draw(SquareDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();
        out.printf("TilingDrawer/draw/will store tiles in temp directory: %s%n", tempTilesPath);

        //1. Get paths of temp tiles
        Instant beforeComputeTempTilesPaths = Instant.now();
        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);
        Instant afterComputeTempTilesPaths = Instant.now();
        out.printf("TilingDrawer/draw/there will be %d tiles, computeTempTilesPaths took %s%n",
                imagesPaths.length,
                Duration.between(beforeComputeTempTilesPaths, afterComputeTempTilesPaths)
        );

        //2. Write temp tiles without holding on to any BufferedImage
        out.printf("TilingDrawer/draw/start rendering %d tiles%n", imagesPaths.length);
        Instant beforeWriteTiles = Instant.now();
        writeTiles(drawing, tempTilesPath);
        Instant afterWriteTiles = Instant.now();
        out.printf("TilingDrawer/draw/done rendering %d tiles, it took %s%n",
                imagesPaths.length,
                Duration.between(beforeWriteTiles, afterWriteTiles)
        );

        //3. Use PNGJ (https://code.google.com/p/pngj/wiki/Snippets) to stitch the tiles together
        out.printf("TilingDrawer/draw/stitching tiles together%n");
        Instant beforeStitchTilesTogether = Instant.now();
        stitchTilesTogether(imagesPaths, drawing.name());
        Instant afterStitchTilesTogether = Instant.now();
        out.printf("TilingDrawer/draw/done stitching %d tiles, it took %s%n",
                imagesPaths.length,
                Duration.between(beforeStitchTilesTogether, afterStitchTilesTogether)
        );
    }

    private String[] computeTempTilesPaths(SquareDrawing drawing, Path tempTilesPath) {
        return drawing
                .split()
                .map(Drawing::name)
                .map(t -> toPath(t, tempTilesPath))
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    private void writeTiles(SquareDrawing drawing, Path tempTilesPath) {
        drawing.split()
                .map(Drawing::render)
                .forEach(t -> {
                    try (OutputStream out = outputStreamFor(t, tempTilesPath)) {
                        ImageIO.write(t.image, Drawing.IMG_TYPE, out);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }

    private void stitchTilesTogether(String[] imagesPaths, String wholeImageName) {
        PngjSamples.doTiling (
                    imagesPaths,
                    wholeImageName,
                    1 //Image is split into lines, so 1 image per row
                );
    }


    private String toPath(String tileName, Path tempTilesPath) {
        return tempTilesPath.resolve(tileName).toString();
    }

    private Path createTempTilesPath() {
        try {
            return Files.createTempDirectory(
                    Paths.get(System.getProperty("java.io.tmpdir")),
                    "tiles_"
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private OutputStream outputStreamFor(NamedImage t, Path tempTilesPath) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(
                toPath(t.name, tempTilesPath))
        );
    }

}