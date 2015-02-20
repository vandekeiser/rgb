package fr.cla.rgb;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class PngDrawingSpliterator implements Spliterator<Tile> {

    private final Deque<Tile> tiles;

    public DrawingSpliterator(Drawing drawing) {
        this.tiles = drawing.tile();
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
        if(action==null) throw new NullPointerException();
        boolean hasMoreElements = !tiles.isEmpty();
        if(!hasMoreElements) return false;

        Tile next = tiles.removeFirst();
        action.accept(next);
        return hasMoreElements;
    }

    @Override public Spliterator<Tile> trySplit() {
        /*if(drawing.isSmallEnough())*/ return null;

        //Pair<PngDrawing> split = drawing.splitIntoTwo();
    }

    @Override public long estimateSize() {
        return tiles.size();
    }

    @Override public int characteristics() {
        return 0;
    }
}
