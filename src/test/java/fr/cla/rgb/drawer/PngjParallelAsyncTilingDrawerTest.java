package fr.cla.rgb.drawer;

public class PngjParallelAsyncTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return PngjParallelAsyncTilingDrawer.INSTANCE;
    }
    
}
