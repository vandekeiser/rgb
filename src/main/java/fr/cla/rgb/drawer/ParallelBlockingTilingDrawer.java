package fr.cla.rgb.drawer;

import static fr.cla.rgb.drawer.Parallelism.Parallelisms.PARALLEL;
import static fr.cla.rgb.drawer.RenderedTilesWriting.*;
import static fr.cla.rgb.drawer.RenderedTilesWriting.RenderedTilesWritings.*;
import static fr.cla.rgb.drawer.Tiling.Tilings.DIVIDE_AND_CONQUER;

public class ParallelBlockingTilingDrawer extends TilingDrawer {
    
    public static final ParallelBlockingTilingDrawer INSTANCE = new ParallelBlockingTilingDrawer();

    protected ParallelBlockingTilingDrawer() {}

    @Override protected Tiling tiling() {
        return DIVIDE_AND_CONQUER;
    }

    @Override protected Parallelism parallelism() {
        return PARALLEL;
    }

    @Override protected RenderedTilesWriting renderedTilesWriting() {
        return BLOCKING;
    }
}