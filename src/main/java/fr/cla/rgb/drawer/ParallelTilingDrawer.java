package fr.cla.rgb.drawer;

import java.util.stream.Stream;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;

public class ParallelTilingDrawer extends TilingDrawer {
    
    public static final ParallelTilingDrawer INSTANCE = new ParallelTilingDrawer();

    private ParallelTilingDrawer() {}
    
    @Override protected Stream<Tile> tile(WholeDrawing drawing) {
        return drawing.unorderedSplit();
    }

}