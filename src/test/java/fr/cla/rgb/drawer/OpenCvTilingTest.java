package fr.cla.rgb.drawer;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import fr.cla.rgb.drawer.opencv.OpenCvTiling;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertFalse;

public class OpenCvTilingTest {

    @Test public void tileWithOpenCv_XS() throws Exception {
        OpenCvTiling.tile(tilesPaths, tiled);
        TilingDrawerTest.assertDrawingIsOk();
    }

    private static String tiled = "tileWithOpenCv_XS.png";
    private String[] tilesPaths;
    @BeforeMethod public void before() throws URISyntaxException {
        String temp = "/XS/",
               tile1 = temp + "JuliaSet3_0_0.png",
               tile2 = temp + "JuliaSet3_0_512.png";
        String tile1Path = cpPath(tile1), tile2Path = cpPath(tile2);
        tilesPaths = new String[]{tile1Path, tile2Path};
        
        //Start from blank slate every time
        Paths.get(tiled).toFile().delete();
        assertFalse(Paths.get(tiled).toFile().exists());
    }
    
    //works properly on win/*nix
    private String cpPath(String tile1) throws URISyntaxException {
        return Paths.get(getClass().getResource(tile1).toURI()).toString();
    }

}
