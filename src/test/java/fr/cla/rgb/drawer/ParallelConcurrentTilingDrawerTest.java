package fr.cla.rgb.drawer;

public class ParallelConcurrentTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return ParallelConcurrentTilingDrawer.INSTANCE;
    }
    
}
