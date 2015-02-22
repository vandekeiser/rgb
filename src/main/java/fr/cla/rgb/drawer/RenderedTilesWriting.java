package fr.cla.rgb.drawer;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.*;

public interface RenderedTilesWriting {
    
    void write(int nbOfTiles, Path tempTilesPath, Stream<NamedImage> renderedImages);
    
    public enum RenderedTilesWritings implements RenderedTilesWriting {
        BLOCKING {
            @Override public void write(int nbOfTiles, Path tempTilesPath, Stream<NamedImage> renderedImages) {
                AtomicInteger currentTile = new AtomicInteger(0);

                renderedImages.forEach(renderedImage -> {
                    debug(BLOCKING, currentTile.incrementAndGet(), nbOfTiles);
                    writeOne(renderedImage, tempTilesPath);
                });
            }
        },
        ASYNC {
            @Override
            public void write(int nbOfTiles, Path tempTilesPath, Stream<NamedImage> renderedImages) {
                AtomicInteger currentTile = new AtomicInteger(0);
                
                //Infinite pool for IO (doesn't change much.. maybe with more cores?)
                ExecutorService ioExecutor = Executors.newCachedThreadPool();
                try {
                    CompletableFuture<?>[] writes = renderedImages.map(renderedImage ->
                        runAsync(() -> {
                            debug(ASYNC, currentTile.incrementAndGet(), nbOfTiles);
                            writeOne(renderedImage, tempTilesPath);
                        }, ioExecutor)
                    ).toArray(i-> new CompletableFuture<?>[i]);

                    CompletableFuture.allOf(writes).join();
                } finally {ioExecutor.shutdownNow();}//Otherwise doesn't exit immediately
            }
        },
        ;

        private static void writeOne(NamedImage image, Path tempTilesPath) {
            try (OutputStream out = outputStreamFor(image, tempTilesPath)) {
                ImageIO.write(image.image, Drawing.IMG_TYPE, out);
            } catch (IOException e) {
                throw new UncheckedIOException(e);//Stop all processing if one tile fails
            }
        }

        private static void debug(RenderedTilesWritings that, int currentTile, int totalTiles) {
            System.out.printf("%s/writeTiles/writing tile %d of %d%n",
                    that, currentTile, totalTiles
            );
        }

        private static OutputStream outputStreamFor(NamedImage t, Path tempTilesPath) throws IOException {
            return new BufferedOutputStream(new FileOutputStream(
                    toPath(t.name, tempTilesPath))
            );
        }

        static String toPath(String tileName, Path tempTilesPath) {
            return tempTilesPath.resolve(tileName).toString();
        }
    }
}
