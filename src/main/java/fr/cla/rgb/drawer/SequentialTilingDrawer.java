package fr.cla.rgb.drawer;

import java.util.stream.Stream;
import fr.cla.rgb.drawing.Tile;
import fr.cla.rgb.drawing.WholeDrawing;

public class SequentialTilingDrawer extends TilingDrawer {

    @Override
    protected Stream<Tile> tile(WholeDrawing drawing) {
        return drawing.orderedSplit();
    }
}