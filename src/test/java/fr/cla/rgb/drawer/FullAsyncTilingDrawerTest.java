package fr.cla.rgb.drawer;

public class FullAsyncTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return AsyncDrawer.INSTANCE;
    }
    
}
