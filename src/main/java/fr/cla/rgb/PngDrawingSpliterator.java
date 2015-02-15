package fr.cla.rgb;

import java.util.Spliterator;
import java.util.function.Consumer;

public class PngDrawingSpliterator implements Spliterator<PngDrawing> {

    private final PngDrawing drawing;

    public PngDrawingSpliterator(PngDrawing drawing) {
        this.drawing = drawing;
    }

    @Override public boolean tryAdvance(Consumer<? super PngDrawing> action) {
        return false;
    }

    @Override public Spliterator<PngDrawing> trySplit() {
        return null;
    }

    @Override public long estimateSize() {
        return 0;
    }

    @Override public int characteristics() {
        return 0;
    }
}
