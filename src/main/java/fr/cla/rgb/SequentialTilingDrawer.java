package fr.cla.rgb;

import java.util.stream.Stream;

public class SequentialTilingDrawer extends TilingDrawer {

    @Override
    protected Stream<Tile> tile(SquareDrawing drawing) {
        return drawing.orderedSplit();
    }
}