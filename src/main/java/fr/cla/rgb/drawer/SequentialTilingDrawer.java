package fr.cla.rgb.drawer;

import java.util.stream.Stream;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;

public class SequentialTilingDrawer extends TilingDrawer {
    
    public static final SequentialTilingDrawer INSTANCE = new SequentialTilingDrawer();
    private SequentialTilingDrawer() {}
    
    @Override protected Stream<Tile> tile(WholeDrawing drawing) {
        return drawing.orderedSplit();
    }

}