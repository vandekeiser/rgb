package fr.cla.rgb.drawer;

public class AsyncTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return AsyncTilingDrawer.INSTANCE;
    }
    
}
