package fr.cla.rgb;

import java.util.Set;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.assertEquals;

import fr.cla.rgb.examples.FirstTry;
import org.junit.Test;

public class PngDrawingSplitTest {

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
}