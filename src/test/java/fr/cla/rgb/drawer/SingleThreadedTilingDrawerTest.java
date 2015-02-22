package fr.cla.rgb.drawer;

public class SingleThreadedTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return SingleThreadedTilingDrawer.INSTANCE;
    }
    
}
