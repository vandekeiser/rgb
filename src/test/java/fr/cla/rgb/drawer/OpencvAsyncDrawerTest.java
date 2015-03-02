package fr.cla.rgb.drawer;

public class OpencvAsyncDrawerTest extends TilingDrawerTest {

    @Override protected Drawer createDrawer() {
        return OpencvAsyncDrawer.INSTANCE;
    }
}
