package fr.cla.rgb.drawer;

public class ParallelAsyncTilingDrawer extends TilingDrawer {

    public static final ParallelAsyncTilingDrawer INSTANCE = new ParallelAsyncTilingDrawer();
    private ParallelAsyncTilingDrawer() {}
    
    @Override protected Tiling tiling() {
            return Tiling.Tilings.DIVIDE_AND_CONQUER;
        }

    @Override protected Parallelism parallelism() {
        return Parallelism.Parallelisms.PARALLEL;
    }

    @Override protected RenderedTilesWriting renderedTilesWriting() {
        return RenderedTilesWriting.RenderedTilesWritings.ASYNC;
    }
    
}