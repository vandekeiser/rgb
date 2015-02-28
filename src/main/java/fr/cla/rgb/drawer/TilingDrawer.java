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
import static java.lang.System.out;

public abstract class TilingDrawer implements Drawer {
    
    protected abstract Tiling tiling();
    protected abstract Parallelism parallelism();
    protected abstract RenderedTilesWriting renderedTilesWriting();
    protected abstract Stitching stitching();

    @Override public final void draw(WholeDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);

        //1. Get paths of temp tiles
        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);
        int nbOfLines = imagesPaths.length;

        //2. Write temp tiles without holding on to any BufferedImage
        out.printf("%s/draw/start writing %d tiles%n", getClass().getSimpleName(), nbOfLines);
        Instant beforeWrites = Instant.now();
        writeTiles(drawing, tempTilesPath);
        Instant afterWrites = Instant.now();
        out.printf("%s/draw/done writing %d tiles, it took %s%n",
                getClass().getSimpleName(),
                nbOfLines,
                Duration.between(beforeWrites, afterWrites)
        );

        //3. Use PNGJ (https://code.google.com/p/pngj/wiki/Snippets) to stitch the tiles together
        out.printf("%s/draw/stitching tiles together%n", getClass().getSimpleName());
        Instant beforeStitch = Instant.now();
        stitching().stitch(imagesPaths, drawing.name());
        Instant afterStitch = Instant.now();
        out.printf("%s/draw/done stitching %d tiles, it took %s%n",
                getClass().getSimpleName(),
                nbOfLines,
                Duration.between(beforeStitch, afterStitch)
        );
    }
    
    protected String[] computeTempTilesPaths(WholeDrawing drawing, Path tempTilesPath) {
        return drawing
                .sequentialSplit() //We'll have to stitch tiles together from first line to last line
                .map(t -> t.toPath(tempTilesPath))
                .collect(Collectors.toList())
                .toArray(new String[drawing.nbOfLines()]);
    }

    protected void writeTiles(WholeDrawing drawing, Path tempTilesPath) {
        int nbOfLines = drawing.nbOfLines();

        renderedTilesWriting().write(
            nbOfLines, 
            tempTilesPath,
            parallelism().maybeParallel(
                tiling().tile(drawing)
            ).map(
                Drawing::render
            )
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