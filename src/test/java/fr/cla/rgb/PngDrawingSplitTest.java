package fr.cla.rgb;

import java.util.stream.Stream;
import static org.junit.Assert.assertEquals;

import fr.cla.rgb.examples.FirstTry;
import org.junit.Test;

public class PngDrawingSplitTest {

	
	@Test public void neutralSplit() {
        PngDrawing smallDrawing = new FirstTry(128);
        Stream<PngDrawing> smallDrawingSplit = smallDrawing.split();
	}
}
