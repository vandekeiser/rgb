package fr.cla.rgb.drawer;

import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawer.opencv.OpenCvTiling;
import fr.cla.rgb.drawer.pngj.PngjForAsyncDrawer;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;
import static fr.cla.rgb.drawer.Parallelism.Parallelisms.PARALLEL;
import static java.lang.System.out;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class OpencvAsyncDrawer implements Drawer {

    public static final OpencvAsyncDrawer INSTANCE = new OpencvAsyncDrawer();
    private OpencvAsyncDrawer() {}

    @Override public final void draw(WholeDrawing drawing) throws Exception {
        Path tempTilesPath = createTempTilesPath();
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);

        ExecutorService ioExecutor = Executors.newCachedThreadPool();
        try {
            Stream<CompletableFuture<WrittenImage>> writtenTiles = asyncWrittenImages(drawing, tempTilesPath, ioExecutor);
            OpenCvTiling.tile(writtenTiles, drawing.name(), drawing.xsize(), drawing.nbOfLines(), tempTilesPath);
        } finally {
            ioExecutor.shutdown();
        }
    }

    protected Stream<CompletableFuture<WrittenImage>> asyncWrittenImages(WholeDrawing drawing, Path tempTilesPath, Executor ioExecutor) {
        Stream<Tile> tiles = Tiling.Tilings.DIVIDE_AND_CONQUER.tile(drawing);
        Stream<NamedImage> renderedTiles = renderTiles(tiles);
        Stream<CompletableFuture<WrittenImage>> writtenTiles = writeTilesAsync (renderedTiles, tempTilesPath, ioExecutor);
        return writtenTiles;
    }

    protected Stream<NamedImage> renderTiles(Stream<Tile> tiles) {
        return PARALLEL.maybeParallel(tiles).map(Tile::render);
    }
    
    protected Stream<CompletableFuture<WrittenImage>> writeTilesAsync(Stream<NamedImage> tiles, Path tempTilesPath, Executor ioExecutor) {
        return tiles.map(renderedImage ->  supplyAsync(
            () -> writeOne(renderedImage, tempTilesPath),
            ioExecutor
        ));
    }

    //TODO use opencv?
    private static WrittenImage writeOne(NamedImage image, Path tempTilesPath) {
        try (OutputStream out = outputStreamFor(image, tempTilesPath)) {
            ImageIO.write(image.image, Drawing.IMG_TYPE, out);
            return new WrittenImage(image);
        } catch (IOException e) {
            throw new UncheckedIOException(e);//Stop all processing if one tile fails
        }
        
        //OpenCvTiling.writeOne(image, tempTilesPath);
        //return new WrittenImage(image);
    }
    
    private static OutputStream outputStreamFor(NamedImage t, Path tempTilesPath) throws IOException {
        return new BufferedOutputStream(new FileOutputStream(
            t.toPath(tempTilesPath))
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