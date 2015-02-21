package fr.cla.rgb.drawer;

import fr.cla.rgb.drawing.examples.FirstTry;
import org.testng.annotations.Test;

public abstract class TilingDrawerTest extends AbstractDrawerTest {

    @Test public void drawS_tiling() { drawer().draw(new FirstTry(1024)); }
    
    @Test public void drawM_tiling() { drawer().draw(new FirstTry(2048)); }
    
    @Test public void drawL_tiling() { drawer().draw(new FirstTry(8192)); }
    
    //@Test public void drawXL_tiling() { new TilingDrawer().draw(new FirstTry(16384)); }
}
