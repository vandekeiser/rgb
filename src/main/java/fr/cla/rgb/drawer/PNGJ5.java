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
 * try/finally writer
 */
public class PNGJ5 {

    public static void doTiling(String tiles[], String dest) {
        int ntiles = tiles.length;
        ImageInfo imi1 = null, imi2 = null; // 1:small tile   2:big image

        PngWriter pngw = null;
        PngReader pngr = new PngReader(new File(tiles[0]));
        try {
            imi1 = pngr.imgInfo;
            imi2 = new ImageInfo(imi1.cols * 1, imi1.rows * ntiles, imi1.bitDepth, imi1.alpha, imi1.greyscale, imi1.indexed);

            pngw = new PngWriter(new File(dest), imi2, true);
            // copy palette and transparency if necessary (more chunks?)
            pngw.copyChunksFrom(pngr.getChunksList(), ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);
            pngr.readSkippingAllRows(); // reads only metadata
        }
        catch(Throwable initFailed) {throw new RuntimeException(initFailed);}
        finally {pngr.end(); /*close, we'll reopen it again soon*/}

        ImageLineInt line2 = new ImageLineInt(imi2);
        int row2 = 0;
        //NEED PNGW
        for (int ty = 0; ty < ntiles; ty++) {
            Arrays.fill(line2.getScanline(), 0); //utile??
            
            PngReader reader = new PngReader(new File(tiles[ty]));
            try {
                reader.setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
                if (!reader.imgInfo.equals(imi1))
                    throw new RuntimeException("different tile ? " + reader.imgInfo);

                for (int row1 = 0; row1 < imi1.rows; row1++, row2++) {
                    ImageLineInt line1 = (ImageLineInt) reader.readRow(row1); // read line
                    System.arraycopy(line1.getScanline(), 0, line2.getScanline(), 0, line1.getScanline().length);
                    pngw.writeRow(line2, row2); // write to full image
                }
            } finally { reader.end(); }
        }
        pngw.end(); // close writer
    }

}
