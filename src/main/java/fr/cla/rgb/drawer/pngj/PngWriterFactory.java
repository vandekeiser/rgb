package fr.cla.rgb.drawer.pngj;

import java.io.File;
import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.PngWriter;
import ar.com.hjg.pngj.chunks.ChunkCopyBehaviour;

public class PngWriterFactory {
    
    private final String dest;
    private final ImageInfo imi2;
    private final PngReader pngr;

    public PngWriterFactory(String dest, ImageInfo imi2, PngReader pngr) {
        this.dest = dest;
        this.imi2 = imi2;
        this.pngr = pngr;
    }

    public PngWriter make() {
        PngWriter pngw = new PngWriter(new File(dest), imi2, true);
        pngw.copyChunksFrom(pngr.getChunksList(), ChunkCopyBehaviour.COPY_PALETTE | ChunkCopyBehaviour.COPY_TRANSPARENCY);
        // copy palette and transparency if necessary (more chunks?)
        pngr.readSkippingAllRows(); // reads only metadata
        return pngw;
    }
}
