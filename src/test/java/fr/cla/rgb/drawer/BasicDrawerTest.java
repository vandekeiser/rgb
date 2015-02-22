package fr.cla.rgb.drawer;

import fr.cla.rgb.drawing.examples.FirstTry;
import org.testng.annotations.Test;

public class BasicDrawerTest extends AbstractDrawerTest {

    @Test public void drawXS_basic() { drawer().draw(new FirstTry(128)); }
    
    @Test public void drawS_basic() { BasicDrawer.INSTANCE.draw(new FirstTry(1024)); }
    
    @Test public void drawM_basic() { BasicDrawer.INSTANCE.draw(new FirstTry(2048)); }

    @Override protected Drawer createDrawer() {
        return BasicDrawer.INSTANCE;
    }
}
