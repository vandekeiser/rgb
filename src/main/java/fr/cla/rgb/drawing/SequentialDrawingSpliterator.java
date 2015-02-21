package fr.cla.rgb.drawing;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SequentialDrawingSpliterator implements Spliterator<Tile> {

    private final List<Tile> tiles;

    public SequentialDrawingSpliterator(WholeDrawing drawing) {
        this.tiles = drawing.tileSequentially();
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
        if(action==null) throw new NullPointerException();
        boolean hasMoreElements = !tiles.isEmpty();
        if(!hasMoreElements) return false;

        Tile next = tiles.remove(0);
        action.accept(next);
        return hasMoreElements;
    }

    @Override public Spliterator<Tile> trySplit() {
        return null; //Not attempting parallelism here
    }

    @Override public long estimateSize() {
        return tiles.size();
    }

    @Override public int characteristics() {
        return    SIZED      //TODO...
                + SUBSIZED
                + DISTINCT
                //+ CONCURRENT
                + NONNULL
                //+ IMMUTABLE
                ;
    }
}
