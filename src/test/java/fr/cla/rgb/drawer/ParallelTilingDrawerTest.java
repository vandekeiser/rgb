package fr.cla.rgb.drawer;

public class ParallelTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return ParallelTilingDrawer.INSTANCE;
    }
    
}
