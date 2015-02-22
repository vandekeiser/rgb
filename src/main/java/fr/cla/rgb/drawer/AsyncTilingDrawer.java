package fr.cla.rgb.drawer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;
import fr.cla.rgb.drawing.WholeDrawing;

public class AsyncTilingDrawer extends ParallelTilingDrawer {

    public static final AsyncTilingDrawer INSTANCE = new AsyncTilingDrawer();
    private AsyncTilingDrawer() {}
    
    @Override protected void writeTiles(WholeDrawing drawing, Path tempTilesPath) {
        int nbOfLines = drawing.nbOfLines();
        AtomicInteger currentTile = new AtomicInteger(0);

        List<CompletableFuture<Void>> futureRenders = tile(drawing)
                .map(Drawing::render)
                .map(namedImage ->
                    CompletableFuture.supplyAsync( () -> {
                        System.out.printf("%s/writeTiles/writing tile %d of %d%n",
                            getClass().getSimpleName(), currentTile.incrementAndGet(), nbOfLines
                        );
                        try (OutputStream out = outputStreamFor(namedImage, tempTilesPath)) {
                            ImageIO.write(namedImage.image, Drawing.IMG_TYPE, out);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);//Stop all processing if one tile fails
                        }
                        return (Void)null;
                    })
                )
                .collect(Collectors.toList());
        
        futureRenders.forEach(CompletableFuture::join);
    }
    
}