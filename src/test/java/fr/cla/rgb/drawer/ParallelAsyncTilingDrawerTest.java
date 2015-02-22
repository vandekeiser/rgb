package fr.cla.rgb.drawer;

public class ParallelAsyncTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return ParallelAsyncTilingDrawer.INSTANCE;
    }
    
}
