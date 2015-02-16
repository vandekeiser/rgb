package fr.cla.rgb;

import java.util.Spliterator;
import java.util.function.Consumer;

public class PngDrawingSpliterator implements Spliterator<PngDrawing> {

    private PngDrawing drawing;

    public PngDrawingSpliterator(PngDrawing drawing) {
        this.drawing = drawing;
    }

    @Override public boolean tryAdvance(Consumer<? super PngDrawing> action) {
        //action.accept();
        return false;
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
