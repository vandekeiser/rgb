package fr.cla.rgb.drawer;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
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
            private final Executor ioExecutor = createIoExecutor();
            private Executor createIoExecutor() {
                return Executors.newCachedThreadPool();//Infinite pool for IO
            }
            
            @Override
            public void write(int nbOfTiles, Path tempTilesPath, Stream<NamedImage> renderedImages) {
                AtomicInteger currentTile = new AtomicInteger(0);

                //1. L'IO est en 2e donc ca va pe pas servir a gd chose.. chainer autrement?
                //3. NB: Methods that do not take an Executor as an argument but end with ...Async
                //       will use ForkJoinPool.commonPool
                CompletableFuture<?>[] writes = renderedImages.map(renderedImage ->
                    runAsync(() -> {
                        debug(ASYNC, currentTile.incrementAndGet(), nbOfTiles);
                        writeOne(renderedImage, tempTilesPath);
                    }, ioExecutor)
                ).toArray(i-> new CompletableFuture<?>[i]);
                CompletableFuture.allOf(writes).join();
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
