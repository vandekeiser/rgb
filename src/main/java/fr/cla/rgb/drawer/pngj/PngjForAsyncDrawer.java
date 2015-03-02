package fr.cla.rgb.drawer.pngj;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import fr.cla.rgb.drawer.WrittenImage;
import static java.util.concurrent.CompletableFuture.runAsync;

/**
 * info2 prend directement le path de tile0
 */
public class PngjForAsyncDrawer {

    public static void doTiling(PngwImi1Imi2 info, Path tempTilesPath, Stream<CompletableFuture<WrittenImage>> tiles) {
        Stream<CompletableFuture<Void>> fff = tiles.map(
                cf->cf.thenAccept(wi -> doTileOne(wi, info, tempTilesPath))
        );
        CompletableFuture<?>[] ffffff = fff.toArray(i-> new CompletableFuture<?>[i]);
        CompletableFuture.allOf(ffffff).join();
        
        info.pngw.end(); // close writer
    }

    private static void doTileOne(WrittenImage writtenImage, PngwImi1Imi2 info, Path tempTilesPath) {
        PngReader reader = new PngReader(new File(writtenImage.toPath(tempTilesPath)));  //KO: name->path
        
        try {
            ImageLineInt line2 = new ImageLineInt(info.imi2);
            reader.setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
            if (!reader.imgInfo.equals(info.imi1)) throw new RuntimeException("different tile ? " + reader.imgInfo);

            for (int row1 = 0; row1 < info.imi1.rows; row1++) {
                int row2 = writtenImage.number() * info.imi1.rows + row1;
                ImageLineInt line1 = (ImageLineInt) reader.readRow(row1); // read line
                System.arraycopy(line1.getScanline(), 0, line2.getScanline(), 0, line1.getScanline().length);
                
                //PngjOutputException: rows must be written in order: expected:34 passed:33
                info.pngw.writeRow(line2, row2); // write to full image
            }
        } finally { reader.end(); }
    }

    public static PngwImi1Imi2 info2(String tile0, int nbTiles, String dest) {
        PngReader pngr = new PngReader(new File(tile0));
        try {
            return info(pngr, nbTiles, dest);
        }
        catch(Throwable initFailed) {throw new RuntimeException(initFailed);}
        finally {pngr.end(); /*close, we'll reopen it again soon*/}
    }

    private static PngwImi1Imi2 info(PngReader pngr, int ntiles, String dest) {
        ImageInfo imi1 = pngr.imgInfo;
        ImageInfo imi2 = new ImageInfo(imi1.cols * 1, imi1.rows * ntiles, imi1.bitDepth, imi1.alpha, imi1.greyscale, imi1.indexed);

        PngWriter pngw = new PngWriter(new File(dest), imi2, true);
        // copy palette and transparency if necessary (more chunks?)
        pngw.copyChunksFrom(pngr.getChunksList(), ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);
        pngr.readSkippingAllRows(); // reads only metadata
        return new PngwImi1Imi2(pngw, imi1, imi2);
    }

    public static class PngwImi1Imi2 {
        final PngWriter pngw; 
        final ImageInfo imi1, imi2;

        PngwImi1Imi2(PngWriter pngw, ImageInfo imi1, ImageInfo imi2) {
            this.pngw = pngw;
            this.imi1 = imi1;
            this.imi2 = imi2;
        }
    }
}
