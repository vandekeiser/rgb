package fr.cla.rgb.drawer;

public class ParallelBlockingTilingDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return ParallelBlockingTilingDrawer.INSTANCE;
    }
    
}
