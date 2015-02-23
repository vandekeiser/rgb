package fr.cla.rgb.drawer.pngj;

import java.io.File;
import java.util.Arrays;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;

/**
 * try/finally,  Debut simplification: pas de x (une seule colonne)
 */
public class PNGJ2 {

    public static void doTiling(String tiles[], String dest) {
        int ntiles = tiles.length;
        int nTilesY = ntiles; // integer ceil
        ImageInfo imi1 = null, imi2 = null; // 1:small tile   2:big image
        PngReader[] readers = new PngReader[1];

        PngWriter pngw = null;
        PngReader pngr = new PngReader(new File(tiles[0]));
        try {
            imi1 = pngr.imgInfo;
            imi2 = new ImageInfo(imi1.cols * 1, imi1.rows * nTilesY, imi1.bitDepth, imi1.alpha, imi1.greyscale, imi1.indexed);

            pngw = new PngWriter(new File(dest), imi2, true);
            // copy palette and transparency if necessary (more chunks?)
            pngw.copyChunksFrom(pngr.getChunksList(), ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);
            pngr.readSkippingAllRows(); // reads only metadata
        }
        catch(Throwable initFailed) {throw new RuntimeException(initFailed);}
        finally {pngr.end(); /*close, we'll reopen it again soon*/}

        
        ImageLineInt line2 = new ImageLineInt(imi2);
        int row2 = 0;
        for (int ty = 0; ty < nTilesY; ty++) {
            int nTilesXcur = ty < nTilesY - 1 ? 1 : ntiles - (nTilesY - 1);
            Arrays.fill(line2.getScanline(), 0); //utile??
            
            for (int tx = 0; tx < nTilesXcur; tx++) { // open several readers
                readers[tx] = new PngReader(new File(tiles[tx + ty]));
                readers[tx].setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
                if (!readers[tx].imgInfo.equals(imi1))
                    throw new RuntimeException("different tile ? " + readers[tx].imgInfo);
            }
            
            for (int row1 = 0; row1 < imi1.rows; row1++, row2++) {
                for (int tx = 0; tx < nTilesXcur; tx++) {
                    ImageLineInt line1 = (ImageLineInt) readers[tx].readRow(row1); // read line
                    System.arraycopy(line1.getScanline(), 0, line2.getScanline(), line1.getScanline().length * tx, line1.getScanline().length);
                }
                pngw.writeRow(line2, row2); // write to full image
            }
            
            for (int tx = 0; tx < nTilesXcur; tx++) readers[tx].end(); // close readers
        }
        pngw.end(); // close writer
    }

}
