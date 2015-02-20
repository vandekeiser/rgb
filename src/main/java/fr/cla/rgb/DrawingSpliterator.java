package fr.cla.rgb;

import java.util.Spliterator;
import java.util.function.Consumer;

public class DrawingSpliterator implements Spliterator<Drawing> {

    private final Drawing drawing;

    public DrawingSpliterator(Drawing drawing) {
        this.drawing = drawing;
    }

    @Override public boolean tryAdvance(Consumer<? super Drawing> action) {
        return false;
    }

    @Override public Spliterator<Drawing> trySplit() {
        return null;
    }

    @Override public long estimateSize() {
        return 0;
    }

    @Override public int characteristics() {
        return 0;
    }
}
