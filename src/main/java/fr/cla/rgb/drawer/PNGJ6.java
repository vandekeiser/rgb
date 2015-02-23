package fr.cla.rgb.drawer;

import java.io.File;
import java.util.Arrays;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;

/**
 * ImageLineInt ds la boucle
 */
public class PNGJ6 {

    public static void doTiling(String tiles[], String dest) {
        int ntiles = tiles.length;
        PngwImi1Imi2 info = null;
        
        PngReader pngr = new PngReader(new File(tiles[0]));
        try {
            info = info(pngr, ntiles, dest);
        }
        catch(Throwable initFailed) {throw new RuntimeException(initFailed);}
        finally {pngr.end(); /*close, we'll reopen it again soon*/}

        int row2 = 0;
        for (int ty = 0; ty < ntiles; ty++) {
            ImageLineInt line2 = new ImageLineInt(info.imi2);
            PngReader reader = new PngReader(new File(tiles[ty]));
            try {
                reader.setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
                if (!reader.imgInfo.equals(info.imi1))
                    throw new RuntimeException("different tile ? " + reader.imgInfo);

                for (int row1 = 0; row1 < info.imi1.rows; row1++, row2++) {
                    ImageLineInt line1 = (ImageLineInt) reader.readRow(row1); // read line
                    System.arraycopy(line1.getScanline(), 0, line2.getScanline(), 0, line1.getScanline().length);
                    info.pngw.writeRow(line2, row2); // write to full image
                }
            } finally { reader.end(); }
        }
        info.pngw.end(); // close writer
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

    
    static class PngwImi1Imi2 {
        final PngWriter pngw; 
        final ImageInfo imi1, imi2;

        PngwImi1Imi2(PngWriter pngw, ImageInfo imi1, ImageInfo imi2) {
            this.pngw = pngw;
            this.imi1 = imi1;
            this.imi2 = imi2;
        }
    }
}
