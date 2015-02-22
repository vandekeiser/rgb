package fr.cla.rgb.drawing;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class SequentialSpliterator implements Spliterator<Tile> {

    private final List<Tile> tiles;

    public SequentialSpliterator(WholeDrawing drawing) {
        this.tiles = drawing.tileSequentially();
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
        tiles.forEach(action::accept);
        return false;
    }

    @Override public Spliterator<Tile> trySplit() {
        return null; //Not attempting parallelism here
    }

    @Override public long estimateSize() {
        return tiles.size();
    }

    @Override public int characteristics() {
        return    SIZED       //Size is the number of tiles..
                + SUBSIZED    //..same for splits
                + DISTINCT    //The tiles don't override equals
                + NONNULL     //WholeDrawing doesn't produce null tiles
                + IMMUTABLE   //No source is modified
                ;
    }
}
