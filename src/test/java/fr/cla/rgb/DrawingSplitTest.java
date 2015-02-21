package fr.cla.rgb;

import java.util.Set;
import java.util.stream.Stream;
import fr.cla.rgb.examples.FirstTry;
import org.junit.Test;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

public class DrawingSplitTest {

	@Test public void neutralSplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE / 2);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
	}

    @Test public void neutralSplitLimit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
    }

    @Test public void twoWaySplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE * 2);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(2, tiles.size());
    }

    @Test public void fourWaySplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE * 4);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(4, tiles.size());
    }

    @Test(expected = UnsupportedOperationException.class) public void illegalSplit() {
        SquareDrawing smallDrawing = new FirstTry(SquareDrawing.MAX_DRAWING_SIZE * 4 + 1);
        smallDrawing.split();
    }

    @Test public void drawX_basic() { new BasicDrawer().draw(new FirstTry(128)); }
    @Test public void drawS_basic() { new BasicDrawer().draw(new FirstTry(1024)); }
    @Test public void drawM_basic() { new BasicDrawer().draw(new FirstTry(2048)); }

    @Test public void drawX_tile() { new TilingDrawer().draw(new FirstTry(128)); }
    @Test public void drawS_tile() { new TilingDrawer().draw(new FirstTry(1024)); }
    @Test public void drawM_tile() {new TilingDrawer().draw(new FirstTry(2048));}

    @Test public void drawS_tileAndMerge() { new TilingDrawer().draw(new FirstTry(1024)); }
    @Test public void drawM_tileAndMerge() { new TilingDrawer().draw(new FirstTry(2048)); }
    @Test public void drawL_tileAndMerge() { new TilingDrawer().draw(new FirstTry(8192)); }
    @Test public void drawXL_tileAndMerge() { new TilingDrawer().draw(new FirstTry(16384)); }

}
