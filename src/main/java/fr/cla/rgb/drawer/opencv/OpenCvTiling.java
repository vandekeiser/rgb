package fr.cla.rgb.drawer.opencv;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void tile(Stream<CompletableFuture<WrittenImage>> tiles, String outPath, String tile0, int ntiles, Path tempTilesPath) {
        Mat firstTileMat = Highgui.imread(tile0);
        int tileRows = firstTileMat.rows(), //whole image / ntiles
            tileCols = firstTileMat.cols(); //same as whole image
        if(tileRows * ntiles != tileCols) throw new AssertionError();
        
        Mat outMat = new Mat(tileCols, tileCols, firstTileMat.type());
        
         //2. Stream of all tiles
        //Stream<String> tilesPathsStream = Arrays.stream(tilesPaths);
        Stream<CompletableFuture<WrittenImageAndMat>> tilesMatsStream = tiles.map(cf->
            cf.thenApply(wi->
                new WrittenImageAndMat(wi, Highgui.imread(wi.toPath(tempTilesPath)))
            )
        );
        
        CompletableFuture<?>[] done = tilesMatsStream.map(wimcf ->
            wimcf.thenAccept(wim -> {
                Rect roi = new Rect(
                        0,
                        wim.wi.number * tileRows,
                        tileCols,
                        tileRows
                );
                System.out.println("roi: " + roi);
                System.out.println("outMat: " + outMat);

                synchronized (outMat) {
                    Mat outView = outMat.submat(roi);
                    wim.mat.copyTo(outView);
                }
            })
        ).toArray(i -> new CompletableFuture<?>[i]);
        CompletableFuture.allOf(done).join();
        
        synchronized (outMat) {
            Highgui.imwrite(outPath, outMat);
        }
    }
    
    static class WrittenImageAndMat {
        final WrittenImage wi;
        final Mat mat;
        WrittenImageAndMat(WrittenImage wi, Mat mat) {
            this.wi = wi;
            this.mat = mat;
        }
    }
}