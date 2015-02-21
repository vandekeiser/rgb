package fr.cla.rgb;

import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ParallelReadyDrawingSpliterator implements Spliterator<Tile> {

    private final Deque<Tile> tiles;

    public ParallelReadyDrawingSpliterator(SquareDrawing drawing) {
        this.tiles = drawing.tileSequentially();
        //TODO get stream <tile>
        //!!! TilingDrawer.stitchTilesTogether attend un stream ordonne via drawing.split,
        //  donc pe faire un autre split?
        //Si ts les fichiers sont generes, peu importe pour les recoller ds quel ordre ils ont ete generes
        // donc on peut en utiliser un autre
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
