package fr.cla.rgb.drawer.pngj;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;
import ar.com.hjg.pngj.chunks.ChunkLoadBehaviour;
import fr.cla.rgb.drawing.Drawing;

/**
 */
public class PNGJ8A {

    public static void doTiling(String tiles[], String dest) {
        int ntiles = tiles.length;
        PngwImi1Imi2 info = info2(tiles[0], tiles.length, dest);
        ImageLineInt line2 = new ImageLineInt(info.imi2);
        
        for (int ty = 0; ty < ntiles; ty++) {
            System.out.printf("PNGJ8/doTiling/tile %d of %d%n", ty, ntiles);
            PngReader reader = new PngReader(new File(tiles[ty]));
            
            try {
                reader.setChunkLoadBehaviour(ChunkLoadBehaviour.LOAD_CHUNK_NEVER);
                if (!reader.imgInfo.equals(info.imi1)) throw new RuntimeException("different tile ? " + reader.imgInfo);

                PngWriter pngw = info.pngwf.make();
                try {
                    for (int row1 = 0; row1 < info.imi1.rows; row1++) {
                        int row2 = ty * info.imi1.rows + row1;
                        ImageLineInt line1 = (ImageLineInt) reader.readRow(row1); // read line
                        System.arraycopy(line1.getScanline(), 0, line2.getScanline(), 0, line1.getScanline().length);
                        //System.out.printf("PNGJ8/doTiling/writing row %d of %d%n", row1, info.imi1.rows-1);
                        pngw.writeRow(line2, row2); // write to full image
                        //System.out.printf("PNGJ8/doTiling/written row %d of %d%n", row1, info.imi1.rows-1);
                    }
                } finally { pngw.end(); }

            } finally { reader.end(); }
        }
    }

    private static PngwImi1Imi2 info2(String tile0, int nbTiles, String dest) {
        PngReader pngr = new PngReader(new File(tile0));
        try {
            return info(pngr, nbTiles, dest);
        }
        catch(Throwable initFailed) {throw new RuntimeException(initFailed);}
        finally { pngr.end(); }
    }

    private static PngwImi1Imi2 info(PngReader pngr, int ntiles, String dest) {
        ImageInfo imi1 = pngr.imgInfo;
        //ImageInfo imi2 = new ImageInfo(imi1.cols, imi1.rows * ntiles, imi1.bitDepth, imi1.alpha, imi1.greyscale, imi1.indexed);
        ImageInfo imi2 = new ImageInfo(imi1.cols, imi1.rows, imi1.bitDepth, imi1.alpha, imi1.greyscale, imi1.indexed);

        PngWriterFactory pngwf = new PngWriterFactory(dest, imi2, pngr);
        return new PngwImi1Imi2(pngwf, imi1, imi2);
    }

    static class PngwImi1Imi2 {
        final PngWriterFactory pngwf;
        final ImageInfo imi1, imi2;

        PngwImi1Imi2(PngWriterFactory pngwf, ImageInfo imi1, ImageInfo imi2) {
            this.pngwf = pngwf;
            this.imi1 = imi1;
            this.imi2 = imi2;
        }
        
        PngWriter make() {
            return pngwf.make();
        }        
    }
}
