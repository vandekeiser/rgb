package fr.cla.rgb.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import fr.cla.rgb.drawing.examples.JuliaSet;
import static java.lang.System.out;

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
        print("new");
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
        //draw(new JuliaSet(16384)); //IllegalStateException: nbOfTiles!=1
        print("tryAdvance");
        //if(tiles.size()!=1) throw new IllegalStateException("nbOfTiles!=1");
        
        tiles.forEach(action::accept);
        tiles.clear();
        return false; 
    }

    @Override public Spliterator<Tile> trySplit() {
        //subList is a view so need to prevent ConcurrentModificationException from removeAll
//        System.out.println("nbOfTiles: " + tiles.size());
//        System.out.println("tiles: " + tiles);
        print("trySplit/START");
        if(tiles.size()==1) return null;
            
        List<Tile> top = new ArrayList<>(tiles.subList(0, tiles.size() / 2));

        this.tiles.removeAll(top);
        print("trySplit/END");
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
    
    private void print(String method) {
        out.printf("________%s/%s, nbOfTiles=%d%n",
                Integer.toHexString(System.identityHashCode(this)), 
                method,
                tiles.size()
        );
    }
}
