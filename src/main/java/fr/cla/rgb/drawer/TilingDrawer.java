package fr.cla.rgb.drawer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;
import static java.lang.System.out;

public enum TilingDrawer implements Drawer {
    
    SEQUENTIAL {
        @Override protected Stream<Tile> tile(WholeDrawing drawing) {
            return drawing.orderedSplit();
        }
    },
    PARALLEL_READY {
        @Override protected Stream<Tile> tile(WholeDrawing drawing) {
            return drawing.unorderedSplit();
        }
    },
    ;
    
    @Override public final void draw(WholeDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);

        //1. Get paths of temp tiles
        Instant beforeComputeTempTilesPaths = Instant.now();
        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);
        Instant afterComputeTempTilesPaths = Instant.now();
        out.printf("%s/draw/there will be %d tiles, computeTempTilesPaths took %s%n",
                getClass().getSimpleName(),
                imagesPaths.length,
                Duration.between(beforeComputeTempTilesPaths, afterComputeTempTilesPaths)
        );

        //2. Write temp tiles without holding on to any BufferedImage
        out.printf("%s/draw/start rendering %d tiles%n", getClass().getSimpleName(), imagesPaths.length);
        Instant beforeWriteTiles = Instant.now();
        writeTiles(drawing, tempTilesPath);
        Instant afterWriteTiles = Instant.now();
        out.printf("%s/draw/done rendering %d tiles, it took %s%n",
                getClass().getSimpleName(),
                imagesPaths.length,
                Duration.between(beforeWriteTiles, afterWriteTiles)
        );

        //3. Use PNGJ (https://code.google.com/p/pngj/wiki/Snippets) to stitch the tiles together
        out.printf("%s/draw/stitching tiles together%n", getClass().getSimpleName());
        Instant beforeStitchTilesTogether = Instant.now();
        stitchTilesTogether(imagesPaths, drawing.name());
        Instant afterStitchTilesTogether = Instant.now();
        out.printf("%s/draw/done stitching %d tiles, it took %s%n",
                getClass().getSimpleName(),
                imagesPaths.length,
                Duration.between(beforeStitchTilesTogether, afterStitchTilesTogether)
        );
    }
    
    private String[] computeTempTilesPaths(WholeDrawing drawing, Path tempTilesPath) {
        return drawing
                .orderedSplit() //We'll have to stitch tiles together from first line to last line
                .map(Drawing::name)
                .map(t -> toPath(t, tempTilesPath))
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    private void writeTiles(WholeDrawing drawing, Path tempTilesPath) {
        AtomicInteger tilenb = new AtomicInteger(0);
        tile(drawing)
                .map(Drawing::render)
                .forEach(t -> {
                    System.out.printf("%s/writeTiles/tilenb: %d%n", getClass().getSimpleName(), tilenb.incrementAndGet());
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
    protected abstract Stream<Tile> tile(WholeDrawing drawing);

    private void stitchTilesTogether(String[] imagesPaths, String wholeImageName) {
        PNGJ.doTiling(
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