package fr.cla.rgb.drawer;

import static fr.cla.rgb.drawer.Parallelism.Parallelisms.PARALLEL;
import static fr.cla.rgb.drawer.RenderedTilesWriting.RenderedTilesWritings.ASYNC;
import static fr.cla.rgb.drawer.Stitching.Stitchings.*;
import static fr.cla.rgb.drawer.Tiling.Tilings.DIVIDE_AND_CONQUER;

public class OpencvParallelAsyncTilingDrawer extends TilingDrawer {

    public static final OpencvParallelAsyncTilingDrawer INSTANCE = new OpencvParallelAsyncTilingDrawer();

    private OpencvParallelAsyncTilingDrawer() {}
    
    @Override protected Tiling tiling() {
        return DIVIDE_AND_CONQUER;
    }

    @Override protected Parallelism parallelism() {
        return PARALLEL;
    }

    @Override protected RenderedTilesWriting renderedTilesWriting() {
        return ASYNC;
    }
    
    @Override protected Stitching stitching() { return WITH_OPEN_CV; }
    
}