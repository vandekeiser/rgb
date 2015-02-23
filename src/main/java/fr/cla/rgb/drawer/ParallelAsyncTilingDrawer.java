package fr.cla.rgb.drawer;

import static fr.cla.rgb.drawer.Parallelism.Parallelisms.PARALLEL;
import static fr.cla.rgb.drawer.RenderedTilesWriting.RenderedTilesWritings.ASYNC;
import static fr.cla.rgb.drawer.Stitching.Stitchings.*;
import static fr.cla.rgb.drawer.Tiling.Tilings.DIVIDE_AND_CONQUER;

public class ParallelAsyncTilingDrawer extends TilingDrawer {

    public static final ParallelAsyncTilingDrawer INSTANCE = new ParallelAsyncTilingDrawer();

    private ParallelAsyncTilingDrawer() {}
    
    @Override protected Tiling tiling() {
        return DIVIDE_AND_CONQUER;
    }

    @Override protected Parallelism parallelism() {
        return PARALLEL;
    }

    @Override protected RenderedTilesWriting renderedTilesWriting() {
        return ASYNC;
    }
    
    @Override protected Stitching stitching() { return WITH_PNGJ9; }
    
}