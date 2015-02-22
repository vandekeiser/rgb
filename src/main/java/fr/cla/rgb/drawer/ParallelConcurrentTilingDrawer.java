package fr.cla.rgb.drawer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;

public class ParallelConcurrentTilingDrawer extends ParallelTilingDrawer {

    public static final ParallelConcurrentTilingDrawer INSTANCE = new ParallelConcurrentTilingDrawer();
    private ParallelConcurrentTilingDrawer() {}
    
    @Override protected void writeTiles(WholeDrawing drawing, Path tempTilesPath) {
        int nbOfLines = drawing.nbOfLines();
        AtomicInteger currentTile = new AtomicInteger(0);

        tile(drawing)
                .parallel()
                .map(Drawing::render)
                .forEach(t -> {
                    System.out.printf("%s/writeTiles/writing tile %d of %d%n",
                            getClass().getSimpleName(), currentTile.incrementAndGet(), nbOfLines
                    );
                    try (OutputStream out = outputStreamFor(t, tempTilesPath)) {
                        ImageIO.write(t.image, Drawing.IMG_TYPE, out);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);//Stop all processing if one tile fails
                    }
                });
    }
    
}