package fr.cla.rgb.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ParallelReadyDrawingSpliterator implements Spliterator<Tile> {

    private final List<Tile> tiles;

    public ParallelReadyDrawingSpliterator(WholeDrawing whole) {
        //!!! TilingDrawer.stitchTilesTogether attend un stream ordonne via drawing.split,
        //Si ts les fichiers sont generes, peu importe pour les recoller ds quel ordre ils ont ete generes
        // donc on peut en utiliser un autre
        this(whole.tileSequentially());
    }
    
    private ParallelReadyDrawingSpliterator(List<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
        if(tiles.size()!=1) throw new IllegalStateException("nbOfTiles!=1");
        Tile next = tiles.get(0);
        action.accept(next);
        return false; 
    }

    @Override public Spliterator<Tile> trySplit() {
        //subList is a view so need to prevent ConcurrentModificationException from removeAll
//        System.out.println("nbOfTiles: " + tiles.size());
//        System.out.println("tiles: " + tiles);
        List<Tile> top = new ArrayList<>(tiles.subList(0, tiles.size() / 2));

        this.tiles.removeAll(top);
        return new ParallelReadyDrawingSpliterator(top);
    }

    @Override public long estimateSize() {
        return this.tiles.size();
    }

    @Override public int characteristics() {
        return    SIZED      //TODO...
                + SUBSIZED 
                + DISTINCT 
                + CONCURRENT 
                + NONNULL 
                + IMMUTABLE
                ;
    }
}
