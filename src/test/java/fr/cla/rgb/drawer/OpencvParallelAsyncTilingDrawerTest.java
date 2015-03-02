package fr.cla.rgb.drawer;

public class OpencvParallelAsyncTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return OpencvParallelAsyncTilingDrawer.INSTANCE;
    }
    
}
