package fr.cla.rgb.drawer;

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
        int ntiles = drawing.nbOfLines(); //need to add param
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);


        Stream<CompletableFuture<WrittenImage>> writtenTilesExample = asyncWrittenImages(drawing, tempTilesPath, null).limit(1);
        Stream<WrittenImage> writtenTilesExample001 = writtenTilesExample.map(it-> {
            try {return it.get();}
            catch (Exception e) {throw new RuntimeException(e);}
        });
        WrittenImage writtenTilesExample0001 = writtenTilesExample001.findFirst().get();
        String tile0 = writtenTilesExample0001.toPath(tempTilesPath);
        
        
        ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
        try {
            Stream<CompletableFuture<WrittenImage>> writtenTiles = asyncWrittenImages(drawing, tempTilesPath, ioExecutor);
            OpenCvTiling.tile(writtenTiles, drawing.name(), tile0, ntiles, tempTilesPath);
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
            () -> writeOne(renderedImage, tempTilesPath)
            ,ioExecutor==null ? ForkJoinPool.commonPool() : ioExecutor
        ));
    }

    private static WrittenImage writeOne(NamedImage image, Path tempTilesPath) {
        try (OutputStream out = outputStreamFor(image, tempTilesPath)) {
            ImageIO.write(image.image, Drawing.IMG_TYPE, out);
            return new WrittenImage(image);
        } catch (IOException e) {
            throw new UncheckedIOException(e);//Stop all processing if one tile fails
        }
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