package fr.cla.rgb;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Stream;
import fr.cla.rgb.examples.FirstTry;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.toSet;
import static org.testng.Assert.assertEquals;

public class DrawingSplitTest {

	@Test
    public void neutralSplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE / 2);
        Stream<Tile> smallDrawingSplit = smallDrawing.orderedSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
	}

    @Test public void neutralSplitLimit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE);
        Stream<Tile> smallDrawingSplit = smallDrawing.orderedSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
    }

    @Test public void twoWaySplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE * 2);
        Stream<Tile> smallDrawingSplit = smallDrawing.orderedSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(2, tiles.size());
    }

    @Test public void fourWaySplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE * 4);
        Stream<Tile> smallDrawingSplit = smallDrawing.orderedSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(4, tiles.size());
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void illegalSplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE * 4 + 1);
        smallDrawing.orderedSplit();
    }

    @Test public void drawXS_basic() { new BasicDrawer().draw(new FirstTry(128)); }
    @Test public void drawS_basic() { new BasicDrawer().draw(new FirstTry(1024)); }
    @Test public void drawM_basic() { new BasicDrawer().draw(new FirstTry(2048)); }

    @Test public void drawS_tiling() { new TilingDrawer().draw(new FirstTry(1024)); }
    @Test public void drawM_tiling() { new TilingDrawer().draw(new FirstTry(2048)); }
    @Test public void drawL_tiling() { new TilingDrawer().draw(new FirstTry(8192)); }
    //@Test public void drawXL_tiling() { new TilingDrawer().draw(new FirstTry(16384)); }


    //---------Test setup stuff VVVVVVVVVVV
    private Instant whenTestMethodStarted;
    @BeforeMethod public void before() {
        this.whenTestMethodStarted = Instant.now();
    }
    @AfterMethod public void after(ITestResult result) {
        System.out.printf(
            "TEST %s took: %s%n",
            result.getMethod().getMethodName(),
            Duration.between(whenTestMethodStarted, Instant.now())
        );
    }
    //---------Test setup stuff ^^^^^^^^^^^
}
