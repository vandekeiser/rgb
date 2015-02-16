package fr.cla.rgb;

import java.util.Spliterator;
import java.util.function.Consumer;

public class PngDrawingSpliterator implements Spliterator<PngDrawing> {

    private static final int MAX_DRAWING_SIZE = 1024;
    private PngDrawing drawing;
    private int tilesLeft;

    public PngDrawingSpliterator(PngDrawing drawing) {
        this.drawing = drawing;
        this.tilesLeft = nbOfTiles(drawing);
    }

    private int nbOfTiles(PngDrawing drawing) {
        int drawingSize = drawing.size();
        int tilesQuotient = MAX_DRAWING_SIZE % drawingSize;
        int tilesRemainder = MAX_DRAWING_SIZE % drawingSize;

        if(tilesRemainder !=0 ) {
            throw new UnsupportedOperationException("fffff: "+drawingSize + " , " + MAX_DRAWING_SIZE);
        }

        return tilesQuotient;
    }

    @Override public boolean tryAdvance(Consumer<? super PngDrawing> action) {
        action.accept(drawing);
        return --tilesLeft==0;
    }

    @Override public Spliterator<PngDrawing> trySplit() {
        /*if(drawing.isSmallEnough())*/ return null;

        //Pair<PngDrawing> split = drawing.splitIntoTwo();
    }

    @Override public long estimateSize() {
        return 0;
    }

    @Override public int characteristics() {
        return 0;
    }
}
