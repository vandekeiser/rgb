package fr.cla.rgb.drawing;

import java.util.Set;
import java.util.stream.Stream;
import fr.cla.rgb.drawing.examples.FirstTry;
import org.testng.annotations.Test;
import static java.util.stream.Collectors.toSet;
import static org.testng.Assert.assertEquals;

public class DrawingSplitTest {

	@Test
    public void neutralSplit() {
        WholeDrawing smallDrawing = new FirstTry(WholeDrawing.MAX_SIZE_BEFORE_SPLIT / 2);
        Stream<Tile> smallDrawingSplit = smallDrawing.sequentialSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
	}

    @Test public void neutralSplitLimit() {
        WholeDrawing smallDrawing = new FirstTry(WholeDrawing.MAX_SIZE_BEFORE_SPLIT);
        Stream<Tile> smallDrawingSplit = smallDrawing.sequentialSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(1, tiles.size());
    }

    @Test public void twoWaySplit() {
        WholeDrawing smallDrawing = new FirstTry(WholeDrawing.MAX_SIZE_BEFORE_SPLIT * 2);
        Stream<Tile> smallDrawingSplit = smallDrawing.sequentialSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(2, tiles.size());
    }

    @Test public void fourWaySplit() {
        WholeDrawing smallDrawing = new FirstTry(WholeDrawing.MAX_SIZE_BEFORE_SPLIT * 4);
        Stream<Tile> smallDrawingSplit = smallDrawing.sequentialSplit();
        Set<Drawing> tiles = smallDrawingSplit.collect(toSet());
        assertEquals(4, tiles.size());
    }

    @Test(expectedExceptions = BadTilingException.class)
    public void illegalSplit() {
        WholeDrawing smallDrawing = new FirstTry(WholeDrawing.MAX_SIZE_BEFORE_SPLIT * 4 + 1);
        smallDrawing.sequentialSplit();
    }

}
