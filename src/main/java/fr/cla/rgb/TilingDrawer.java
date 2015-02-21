package fr.cla.rgb;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import static java.lang.System.out;

public abstract class TilingDrawer {

    public final void draw(SquareDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();
        out.printf("ParallelReadyTilingDrawer/draw/will store tiles in temp directory: %s%n", tempTilesPath);

        //1. Get paths of temp tiles
        Instant beforeComputeTempTilesPaths = Instant.now();
        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);
        Instant afterComputeTempTilesPaths = Instant.now();
        out.printf("ParallelReadyTilingDrawer/draw/there will be %d tiles, computeTempTilesPaths took %s%n",
                imagesPaths.length,
                Duration.between(beforeComputeTempTilesPaths, afterComputeTempTilesPaths)
        );

        //2. Write temp tiles without holding on to any BufferedImage
        out.printf("ParallelReadyTilingDrawer/draw/start rendering %d tiles%n", imagesPaths.length);
        Instant beforeWriteTiles = Instant.now();
        writeTiles(drawing, tempTilesPath);
        Instant afterWriteTiles = Instant.now();
        out.printf("ParallelReadyTilingDrawer/draw/done rendering %d tiles, it took %s%n",
                imagesPaths.length,
                Duration.between(beforeWriteTiles, afterWriteTiles)
        );

        //3. Use PNGJ (https://code.google.com/p/pngj/wiki/Snippets) to stitch the tiles together
        out.printf("ParallelReadyTilingDrawer/draw/stitching tiles together%n");
        Instant beforeStitchTilesTogether = Instant.now();
        stitchTilesTogether(imagesPaths, drawing.name());
        Instant afterStitchTilesTogether = Instant.now();
        out.printf("ParallelReadyTilingDrawer/draw/done stitching %d tiles, it took %s%n",
                imagesPaths.length,
                Duration.between(beforeStitchTilesTogether, afterStitchTilesTogether)
        );
    }
    
    private String[] computeTempTilesPaths(SquareDrawing drawing, Path tempTilesPath) {
        return drawing
                .orderedSplit() //We'll have to stitch tiles together from first line to last line
                .map(Drawing::name)
                .map(t -> toPath(t, tempTilesPath))
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    private void writeTiles(SquareDrawing drawing, Path tempTilesPath) {
        tile(drawing)
                .map(Drawing::render)
                .forEach(t -> {
                    try (OutputStream out = outputStreamFor(t, tempTilesPath)) {
                        ImageIO.write(t.image, Drawing.IMG_TYPE, out);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }

    /**
     * @param drawing The drawing to split
     * @return An ordered Stream in sequential case, and an unordered Stream in the parallel-ready case
     */
    protected abstract Stream<Tile> tile(SquareDrawing drawing);

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