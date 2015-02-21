package fr.cla.rgb.drawing;

import java.util.Spliterator;
import java.util.function.Consumer;

public class ParallelReadyDrawingSpliterator implements Spliterator<Tile> {

    private final WholeDrawing whole;
    private final int nbOfTiles;

    public ParallelReadyDrawingSpliterator(WholeDrawing whole) {
        this.whole = whole;
        //TODO get stream <tile>
        //!!! TilingDrawer.stitchTilesTogether attend un stream ordonne via drawing.split,
        //  donc pe faire un autre split?
        //Si ts les fichiers sont generes, peu importe pour les recoller ds quel ordre ils ont ete generes
        // donc on peut en utiliser un autre
        this.nbOfTiles = whole.nbOfLines();
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
//        if(action==null) throw new NullPointerException();
//        boolean hasMoreElements = !tiles.isEmpty();
//        if(!hasMoreElements) return false;
//
//        Tile next = tiles.removeFirst();
//        action.accept(next);
//        return hasMoreElements;
        
        //???
        //TJRS RETOURNER FALSE (sauf 1 fois?) car les tiles sont grosses
        // (ca serait pas pareil si on retournait un stream <point> mais on peut pas)
        Tile next = null;
        action.accept(next);
        return false; 
    }

    @Override public Spliterator<Tile> trySplit() {
        /*if(drawing.isSmallEnough())*/
        //Pair<PngDrawing> split = drawing.splitIntoTwo();
        
        //s'inspirer du code tileSequentially
        
        return null;
    }

    @Override public long estimateSize() {
        return this.nbOfTiles;
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
