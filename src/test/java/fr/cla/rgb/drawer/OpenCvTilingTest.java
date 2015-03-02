package fr.cla.rgb.drawer;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import fr.cla.rgb.drawer.opencv.OpenCvTiling;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static java.util.function.Function.identity;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class OpenCvTilingTest {

    @Test public void tileWithOpenCv_XS() throws Exception {
        //1. Do it
        OpenCvTiling.tile(tilesPaths, tiled);
        
        //2. Basic checks
        assertTrue(Paths.get(tiled).toFile().exists(), "The whole pic was not written at all");
        Mat wholeImage = Highgui.imread(tiled);
        int cols = wholeImage.cols(), rows = wholeImage.rows();
        assertTrue(cols>0, "The whole pic has no cols");
        assertTrue(rows>0, "The whole pic has no rows");
        
        //3. At least some pixels must be non-black (all-black happens if tile dims are incorrect, etc..)
        assertTrue(
            IntStream.range(0, cols).mapToObj(
                x -> IntStream.range(0, rows).mapToObj(
                    y -> wholeImage.get(x, y)
                )
            ).flatMap(identity())
                    .flatMapToDouble(Arrays::stream)
                    .anyMatch(OpenCvTilingTest::isNotZero),
            "The whole pic is black"
        );
//        byte buff[] = new byte[m.total() * m.channels()];
//        m.get(0, 0, buff);
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

    private static boolean isNotZero(double d) { return d != 0.0; }
}
