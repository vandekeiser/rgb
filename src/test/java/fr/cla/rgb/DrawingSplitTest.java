package fr.cla.rgb;

import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

import fr.cla.rgb.examples.FirstTry;
import fr.cla.rgb.examples.JuliaSet;
import org.junit.Test;

public class DrawingSplitTest {

    //PngDrawingSpliterator.MAX_DRAWING_SIZE = 1024;

	@Test public void neutralSplit() {
        Drawing smallDrawing = new FirstTry(128);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
	}

    @Test public void neutralSplitLimit() {
        Drawing smallDrawing = new FirstTry(1024);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
    }

    @Test public void twoWaySplit() {
        Drawing smallDrawing = new FirstTry(2048);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(2, tiles.size());
    }

    @Test public void fourWaySplit() {
        Drawing smallDrawing = new FirstTry(4096);
        Stream<Tile> smallDrawingSplit = smallDrawing.split();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(4, tiles.size());
    }

    @Test(expected = UnsupportedOperationException.class) public void illegalSplit() {
        Drawing smallDrawing = new FirstTry(4097);
        smallDrawing.split();
    }

    @Test public void drawXs_basic() {
        new BasicDrawer().draw(new FirstTry(128));
    }
    @Test public void drawS_basic() {
        new BasicDrawer().draw(new FirstTry(1024));
    }
    @Test public void drawM_basic() {
        new BasicDrawer().draw(new FirstTry(2048));
    }

    @Test public void drawXs_tile() {
        new TilingDrawer().draw(new FirstTry(128));
    }
    @Test public void drawS_tile() {
        new TilingDrawer().draw(new FirstTry(1024));
    }
    @Test public void drawM_tile() {new TilingDrawer().draw(new FirstTry(2048));}

    @Test public void drawXs_tileAndMerge() {
            new TilingDrawer().draw(new FirstTry(2048));
        }

}
