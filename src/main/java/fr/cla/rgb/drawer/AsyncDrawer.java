package fr.cla.rgb.drawer;

import java.awt.font.GraphicAttribute;
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
import fr.cla.rgb.drawer.pngj.PNGJ10;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.NamedImage;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;
import static fr.cla.rgb.drawer.Parallelism.Parallelisms.*;
import static java.lang.System.out;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AsyncDrawer implements Drawer {

    public static final AsyncDrawer INSTANCE = new AsyncDrawer();
    private AsyncDrawer() {}

    @Override public final void draw(WholeDrawing drawing) throws Exception {
        Path tempTilesPath = createTempTilesPath();
        int ntiles = drawing.nbOfLines(); //need to add param
        out.printf("%s/draw/will store tiles in temp directory: %s%n", getClass().getSimpleName(), tempTilesPath);
        String[] imagesPaths = computeTempTilesPaths(drawing, tempTilesPath);

        Stream<CompletableFuture<WrittenImage>> writtenTiles = asyncWrittenImages(drawing, tempTilesPath);
        //Stream<CompletableFuture<String>> writtenTilesPath = writtenTiles.map(cf->cf.thenApply(NamedImage::name));
        Stream<CompletableFuture<WrittenImage>> writtenTilesExample = asyncWrittenImages(drawing, tempTilesPath).limit(1);

        Stream<CompletableFuture<WrittenImage>> writtenTilesExample01 = writtenTilesExample.limit(1);
        Stream<WrittenImage> writtenTilesExample001 = writtenTilesExample01.map(it-> {
            try {return it.get();}
            catch (Exception e) {throw new RuntimeException(e);}
        });
        WrittenImage writtenTilesExample0001 = writtenTilesExample001.findFirst().get();
        String tile0 = writtenTilesExample0001.toPath(tempTilesPath);
        
        PNGJ10.PngwImi1Imi2 info = PNGJ10.info2(tile0, ntiles, drawing.name());
        PNGJ10.doTiling(
                info,
                tempTilesPath,
                writtenTiles
        );
    }

    protected Stream<CompletableFuture<WrittenImage>> asyncWrittenImages(WholeDrawing drawing, Path tempTilesPath) {
        Stream<Tile> tiles = Tiling.Tilings.DIVIDE_AND_CONQUER.tile(drawing);
        Stream<NamedImage> renderedTiles = renderTiles(tiles);
        Stream<CompletableFuture<WrittenImage>> writtenTiles = writeTilesAsync (renderedTiles, tempTilesPath);
        return writtenTiles;
    }

    protected String[] computeTempTilesPaths(WholeDrawing drawing, Path tempTilesPath) {
        return drawing.sequentialSplit() //We'll have to stitch tiles together from first line to last line
            .map(t -> t.toPath(tempTilesPath))
            .collect(Collectors.toList())
            .toArray(new String[drawing.nbOfLines()]);
    }

    protected Stream<NamedImage> renderTiles(Stream<Tile> tiles) {
        return PARALLEL.maybeParallel(tiles).map(Tile::render);
    }
    
    protected Stream<CompletableFuture<WrittenImage>> writeTilesAsync(Stream<NamedImage> tiles, Path tempTilesPath) {
        ExecutorService ioExecutor = Executors.newCachedThreadPool();
        try {
            return tiles.map(renderedImage ->  supplyAsync(
                () -> writeOne(renderedImage, tempTilesPath),
                ioExecutor
            ));
        } finally {/*ioExecutor.shutdown();*/}
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