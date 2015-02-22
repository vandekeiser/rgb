package fr.cla.rgb.drawer;

public class ParallelBlockingTilingDrawer extends TilingDrawer {
    
    public static final ParallelBlockingTilingDrawer INSTANCE = new ParallelBlockingTilingDrawer();

    protected ParallelBlockingTilingDrawer() {}

    @Override protected Tiling tiling() {
        return Tiling.Tilings.DIVIDE_AND_CONQUER;
    }

    @Override protected Parallelism parallelism() {
        return Parallelism.Parallelisms.PARALLEL;
    }

    @Override protected RenderedTilesWriting renderedTilesWriting() {
        return RenderedTilesWriting.RenderedTilesWritings.BLOCKING;
    }
}