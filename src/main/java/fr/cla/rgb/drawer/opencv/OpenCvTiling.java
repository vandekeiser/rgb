package fr.cla.rgb.drawer.opencv;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import fr.cla.rgb.drawer.WrittenImage;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

//ParallelAsyncTilingDrawer/draw/done stitching 64 tiles, it took PT24M17.853S
//encore plus long que pngj, swap?
public class OpenCvTiling {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void tile(String[] tilesPaths, String outPath) {
    //writtenImage.toPath(tempTilesPath)
    
        try {
            doTile(tilesPaths, outPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
        
    public static void doTile(String[] tilesPaths, String outPath) throws IOException {
        //1. Get info from 1st tile
        Mat firstTileMat = Highgui.imread(tilesPaths[0]);
        int tileRows = firstTileMat.rows(), //whole image / ntiles
            tileCols = firstTileMat.cols(), //same as whole image
            ntiles = tilesPaths.length;
        if(tileRows * ntiles != tileCols) throw new AssertionError();
        
        //2. Stream of all tiles
        Stream<String> tilesPathsStream = Arrays.stream(tilesPaths);
        Stream<Mat> tilesMatsStream = tilesPathsStream.map(Highgui::imread);
        
        //3. Init whole image
        Mat outMat = new Mat(tileCols, tileCols, firstTileMat.type());
        
        //4. Write all tiles to whole image
        AtomicInteger itile = new AtomicInteger(0);
        tilesMatsStream.forEach(tileMat -> {
            Rect roi = new Rect(
                0,
                itile.getAndIncrement() * tileRows,
                tileCols,
                tileRows
            );
            System.out.println("roi: " + roi);
            System.out.println("outMat: " + outMat);

            Mat outView = outMat.submat(roi);
            tileMat.copyTo(outView);
        });
        
        //5. Write whole image to disk
        Highgui.imwrite(outPath, outMat);
    }

    public static void tile(Stream<CompletableFuture<WrittenImage>> tiles, String outPath, String tile0, int ntiles) {
        Mat firstTileMat = Highgui.imread(tile0);
        int tileRows = firstTileMat.rows(), //whole image / ntiles
            tileCols = firstTileMat.cols(); //same as whole image
        if(tileRows * ntiles != tileCols) throw new AssertionError();
        
        Mat outMat = new Mat(tileCols, tileCols, firstTileMat.type());
    }
}