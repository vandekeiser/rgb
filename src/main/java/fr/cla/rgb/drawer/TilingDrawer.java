package fr.cla.rgb.drawer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.WholeDrawing;
import static fr.cla.rgb.drawer.RenderedTilesWriting.RenderedTilesWritings.toPath;
import static java.lang.System.out;

public abstract class TilingDrawer implements Drawer {
    
    protected abstract Tiling tiling();
    protected abstract Parallelism parallelism();
    protected abstract RenderedTilesWriting renderedTilesWriting();

    @Override public final void draw(WholeDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);

        //1. Get paths of temp tiles
        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);
        int nbOfLines = imagesPaths.length;

        //2. Write temp tiles without holding on to any BufferedImage
        out.printf("%s/draw/start rendering %d tiles%n", getClass().getSimpleName(), nbOfLines);
        Instant beforeWriteTiles = Instant.now();
        writeTiles(drawing, tempTilesPath);
        Instant afterWriteTiles = Instant.now();
        out.printf("%s/draw/done rendering %d tiles, it took %s%n",
                getClass().getSimpleName(),
                nbOfLines,
                Duration.between(beforeWriteTiles, afterWriteTiles)
        );

        //3. Use PNGJ (https://code.google.com/p/pngj/wiki/Snippets) to stitch the tiles together
        out.printf("%s/draw/stitching tiles together%n", getClass().getSimpleName());
        Instant beforeStitchTilesTogether = Instant.now();
        stitchTilesTogether(imagesPaths, drawing.name());
        Instant afterStitchTilesTogether = Instant.now();
        out.printf("%s/draw/done stitching %d tiles, it took %s%n",
                getClass().getSimpleName(),
                nbOfLines,
                Duration.between(beforeStitchTilesTogether, afterStitchTilesTogether)
        );
    }
    
    protected String[] computeTempTilesPaths(WholeDrawing drawing, Path tempTilesPath) {
        return drawing
                .sequentialSplit() //We'll have to stitch tiles together from first line to last line
                .map(Drawing::name)
                .map(t -> toPath(t, tempTilesPath))
                .collect(Collectors.toList())
                .toArray(new String[drawing.nbOfLines()]);
    }

    protected void writeTiles(WholeDrawing drawing, Path tempTilesPath) {
        int nbOfLines = drawing.nbOfLines();

        renderedTilesWriting().write(nbOfLines, tempTilesPath,
                parallelism().makeSingleThreadedOrParallel(
                        tiling().tile(drawing)
                ).map(
                        Drawing::render
                )
        );
    }

    protected void stitchTilesTogether(String[] imagesPaths, String wholeImageName) {
        PNGJ.doTiling(
                imagesPaths,
                wholeImageName,
                1 //Image is only split into lines, so 1 image per row
        );
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

}