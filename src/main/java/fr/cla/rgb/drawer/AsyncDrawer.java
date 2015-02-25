package fr.cla.rgb.drawer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;
import static fr.cla.rgb.drawer.Parallelism.*;
import static fr.cla.rgb.drawer.Parallelism.Parallelisms.*;
import static fr.cla.rgb.drawer.RenderedTilesWriting.RenderedTilesWritings.toPath;
import static java.lang.System.out;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AsyncDrawer implements Drawer {
    
    @Override public final void draw(WholeDrawing drawing) {
        Path tempTilesPath = createTempTilesPath();
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);

        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);
        //et nbTiles necessaire?
        
        Stream<CompletableFuture<WrittenImage>> writtenTiles = asyncWrittenImages(drawing, tempTilesPath);
        Stream<CompletableFuture<WrittenImage>> writtenTilesExample = asyncWrittenImages(drawing, tempTilesPath).limit(1);
        //stitching().stitch(imagesPaths, drawing.name());
    }

    protected Stream<CompletableFuture<WrittenImage>> asyncWrittenImages(WholeDrawing drawing, Path tempTilesPath) {
        Stream<Tile> tiles = Tiling.Tilings.DIVIDE_AND_CONQUER.tile(drawing);
        Stream<NamedImage> renderedTiles = renderTiles(tiles);
        Stream<CompletableFuture<WrittenImage>> writtenTiles = writeTilesAsync (renderedTiles, tempTilesPath);
        return writtenTiles;
    }

    protected String[] computeTempTilesPaths(WholeDrawing drawing, Path tempTilesPath) {
        return drawing.sequentialSplit() //We'll have to stitch tiles together from first line to last line
            .map(Drawing::name)
            .map(t -> toPath(t, tempTilesPath))
            .collect(Collectors.toList())
            .toArray(new String[drawing.nbOfLines()]);
    }

    protected Stream<NamedImage> renderTiles(Stream<Tile> tiles) {
        return PARALLEL.maybeParallel(tiles).map(Drawing::render);
    }
    
    protected Stream<CompletableFuture<WrittenImage>> writeTilesAsync(Stream<NamedImage> tiles, Path tempTilesPath) {
        ExecutorService ioExecutor = Executors.newCachedThreadPool();
        try {
            return tiles.map(renderedImage ->  supplyAsync(
                () -> writeOne(renderedImage, tempTilesPath),
                ioExecutor
            ));
        } finally {ioExecutor.shutdownNow();}//Otherwise doesn't exit immediately
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
            toPath(t.name, tempTilesPath))
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