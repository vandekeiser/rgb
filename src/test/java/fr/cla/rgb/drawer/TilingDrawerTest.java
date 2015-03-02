package fr.cla.rgb.drawer;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.IntStream;
import fr.cla.rgb.drawing.examples.FirstTry;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.testng.annotations.Test;
import static java.util.function.Function.identity;
import static org.testng.Assert.assertTrue;

public abstract class TilingDrawerTest extends AbstractDrawerTest {

    @Test public void drawS_tiling() throws Exception { 
        drawer().draw(drawing_S);
        assertDrawingIsOk();
    }

    @Test/*(enabled = false)*/ public void drawM_tiling() throws Exception { 
        drawer().draw(drawing_M);
        assertDrawingIsOk();
    }
    
    @Test/*(enabled = false)*/ public void drawL_tiling() throws Exception { 
        drawer().draw(drawing_L);
        assertDrawingIsOk();
    }
    
    static void assertDrawingIsOk() {
        assertTrue(Paths.get(drawingPath).toFile().exists(), "The whole pic was not written at all");
        Mat wholeImage = Highgui.imread(drawingPath);
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
                    .anyMatch(TilingDrawerTest::isNotZero),
            "The whole pic is black"
        );
    }
    
    static boolean isNotZero(double d) { return d != 0.0; }

}
