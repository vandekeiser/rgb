package fr.cla.rgb.drawer.opencv;

import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import fr.cla.rgb.drawer.WrittenImage;
import fr.cla.rgb.drawing.NamedImage;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

//ParallelAsyncTilingDrawer/draw/done stitching 64 tiles, it took PT24M17.853S
//encore plus long que pngj, swap?
public class OpenCvTiling {

    static { OpenCv.loadOpenCvLibrary(); }

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

    public static void tile(Stream<CompletableFuture<WrittenImage>> tiles, String outPath, int wholeSize, int ntiles, Path tempTilesPath) {
        //Mat firstTileMat = Highgui.imread(tile0);
        int tileRows = /*firstTileMat.rows()*/wholeSize / ntiles,
            tileCols = /*firstTileMat.cols()*/wholeSize;
        int type = /*firstTileMat.type()*/16;
        Mat outMat = new Mat(tileCols, tileCols, type);
        
        Instant a = Instant.now();
        
        CompletableFuture<?>[] done = tiles.map(cf->
            cf.thenApply(wi->
                new WrittenImageAndMat(
                    wi,
                    Highgui.imread(wi.toPath(tempTilesPath))
                )
            )
        ).map(wimcf ->
            wimcf.thenAcceptAsync(wim -> { //no IO block
                Rect roi = new Rect(
                    0,
                    wim.wi.number() * tileRows,
                    tileCols,
                    tileRows
                );
                Mat outView = outMat.submat(roi);
                wim.mat.copyTo(outView);
                wim.mat.release();
            })
        )
        .toArray(i -> new CompletableFuture<?>[i]);
        System.out.printf("CompletableFuture[] done in %s%n", Duration.between(a, Instant.now()));
        
        Instant b = Instant.now();
        CompletableFuture.allOf(done).join();
        System.out.printf("CompletableFuture.allOf(done).join()[] done in %s%n", Duration.between(b, Instant.now()));
        
        Instant c = Instant.now();
        Highgui.imwrite(outPath, outMat);
        System.out.printf("Highgui.imwrite done in %s%n", Duration.between(c, Instant.now()));
        
        Instant d = Instant.now();
        outMat.release();
        System.out.printf("outMat.release done in %s%n", Duration.between(d, Instant.now()));
    }

//    public static void writeOne(NamedImage image, Path tempTilesPath) {
//        int[] pixels = ((DataBufferInt) image.image.getRaster().getDataBuffer()).getData();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(pixels.length * 4);
//        IntBuffer intBuffer = byteBuffer.asIntBuffer();
//        intBuffer.put(pixels);
//
//        //                rows           cols
//        Mat mat = new Mat(image.ysize(), image.xsize(), 16);
//        mat.put(0, 0, byteBuffer.array());
//
//        Highgui.imwrite(image.toPath(tempTilesPath), mat);
//    }

    static class WrittenImageAndMat {
        final WrittenImage wi;
        final Mat mat;
        WrittenImageAndMat(WrittenImage wi, Mat mat) {
            this.wi = wi;
            this.mat = mat;
        }
    }
}