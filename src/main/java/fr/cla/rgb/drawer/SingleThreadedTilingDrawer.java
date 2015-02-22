package fr.cla.rgb.drawer;

import static fr.cla.rgb.drawer.Parallelism.Parallelisms.SINGLE_THREADED;
import static fr.cla.rgb.drawer.RenderedTilesWriting.RenderedTilesWritings.BLOCKING;
import static fr.cla.rgb.drawer.Tiling.Tilings.SEQUENTIAL;

public class SingleThreadedTilingDrawer extends TilingDrawer {
    
    public static final SingleThreadedTilingDrawer INSTANCE = new SingleThreadedTilingDrawer();
    private SingleThreadedTilingDrawer() {}

    @Override protected Tiling tiling() {
        return SEQUENTIAL;
    }

    @Override protected Parallelism parallelism() {
        return SINGLE_THREADED;
    }

    @Override protected RenderedTilesWriting renderedTilesWriting() {
        return BLOCKING;
    }
}