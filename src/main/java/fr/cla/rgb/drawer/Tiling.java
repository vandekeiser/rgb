package fr.cla.rgb.drawer;

import java.util.stream.Stream;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;

public interface Tiling {
    
    Stream<Tile> tile(WholeDrawing drawing);
    
    public enum Tilings implements Tiling {
        SEQUENTIAL {
            @Override public Stream<Tile> tile(WholeDrawing drawing) {
                return drawing.sequentialSplit();
            }
        },
        DIVIDE_AND_CONQUER {
            @Override public Stream<Tile> tile(WholeDrawing drawing) {
                return drawing.divideAndConquerSplit();
            }
        },
        ;
    }
    
}
