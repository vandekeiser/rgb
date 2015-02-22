package fr.cla.rgb.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import static java.lang.System.out;

public class DivideAndConquerSpliterator implements Spliterator<Tile> {

    private final List<Tile> tiles;

    public DivideAndConquerSpliterator(WholeDrawing whole) {
        //Fetching the tile sequentially doesn't matter a lot,
        // because tiles are just definitions so producing them is cheap
        //What matters is that they are rendered in parallel, 
        // which is only possible if trySplit doesn't return null.
        this(whole.tileSequentially());
    }
    
    private DivideAndConquerSpliterator(List<Tile> tiles) {
        this.tiles = tiles;
        debug("new");
    }

    @Override public boolean tryAdvance(Consumer<? super Tile> action) {
        debug("tryAdvance");
        tiles.forEach(action::accept);
        return false;
    }

    @Override public Spliterator<Tile> trySplit() {
        debug("trySplit/START");
        if(tiles.size()==1) return null; //Can't split 1
            
        List<Tile> top = new ArrayList<>(tiles.subList(0, tiles.size() / 2));

        this.tiles.removeAll(top);
        debug("trySplit/END");
        return new DivideAndConquerSpliterator(top);
    }

    @Override public long estimateSize() {
        return this.tiles.size();
    }

    @Override public int characteristics() {
        return    SIZED       //Size is the number of tiles..
                + SUBSIZED    //..same for splits
                + DISTINCT    //The tiles don't override equals
                + CONCURRENT  //TODO... relire Mastering Lambdas
                + NONNULL     //WholeDrawing doesn't produce null tiles
                + IMMUTABLE   //Yes since WholeDrawing is not modified?
                ;
    }
    
    private void debug(String method) {
        out.printf("________%s/%s, nbOfTiles=%d%n",
                Integer.toHexString(System.identityHashCode(this)), 
                method,
                tiles.size()
        );
    }
}
