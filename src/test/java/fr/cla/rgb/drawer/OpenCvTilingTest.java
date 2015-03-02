package fr.cla.rgb.drawer;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Instant;
import fr.cla.rgb.drawer.opencv.OpenCvTiling;
import fr.cla.rgb.drawing.examples.FirstTry;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OpenCvTilingTest {

    @Test public void drawXS_basic() throws Exception { 
        OpenCvTiling.tile(tilesPaths, "OpenCvTilingTest.png");
    }

    
    private String[] tilesPaths;
    
    @BeforeMethod public void before() throws URISyntaxException {
        String temp = "/tiles_3455344334670877700",
               tile1 = temp + "/JuliaSet3_0_0.png",
               tile2 = temp + "/JuliaSet3_0_512.png";
        String tile1Path = cpPath(tile1), tile2Path = cpPath(tile2);
        tilesPaths = new String[]{tile1Path, tile2Path};
    }
    
    private String cpPath(String tile1) throws URISyntaxException {
        return Paths.get(getClass().getResource(tile1).toURI()).toString();
    }

}
