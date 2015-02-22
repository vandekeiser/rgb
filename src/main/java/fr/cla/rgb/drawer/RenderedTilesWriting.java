package fr.cla.rgb.drawer;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;

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
            @Override public void write(int nbOfTiles, Path tempTilesPath, Stream<NamedImage> renderedImages) {
                AtomicInteger currentTile = new AtomicInteger(0);

                List<CompletableFuture<Void>> futureRenders = renderedImages.map(renderedImage ->
                    CompletableFuture.supplyAsync(() -> {
                        debug(BLOCKING, currentTile.incrementAndGet(), nbOfTiles);
                        writeOne(renderedImage, tempTilesPath);
                        return (Void) null;
                    })
                )
                .collect(Collectors.toList());

                futureRenders.forEach(CompletableFuture::join);
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
