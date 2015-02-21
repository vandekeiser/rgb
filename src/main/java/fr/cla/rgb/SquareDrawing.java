package fr.cla.rgb;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class SquareDrawing extends Drawing {

    protected SquareDrawing(int size) {
        super(size, size);
    }

    final int size() {return xsize();}
    protected final int wholeDrawingSize() {return size();}

    @Override protected final String name() {
        return getClass().getSimpleName() + "." + Drawing.IMG_TYPE;
    }

    public Stream<Tile> split() {
        Spliterator<Tile> thisSpliterator = new SequentialDrawingSpliterator(this);
        return StreamSupport.stream(thisSpliterator, false);
    }

    public Deque<Tile> tileSequentially() {
        int nbOfTiles = nbOfTiles();
        int lines = nbOfTiles;

        int size = size();
        int tileXSize = size;
        int tileYSize = size/nbOfTiles;
        int tileYSizeRemainder = size()%nbOfTiles;
        if(tileYSizeRemainder!=0) throw new UnsupportedOperationException(String.format(
                "nbOfTiles=%d is not a multiple of size=%d, remainder=%d",
                nbOfTiles,
                size,
                tileYSizeRemainder
        ));

        System.out.println("nbOfTiles: " + nbOfTiles);
        Deque<Tile> tiles = new LinkedList<>();

        for(int line = 0; line < nbOfTiles; line++) {
            tiles.add(
                Tile.of(this)
                        .sized(tileXSize, tileYSize)
                        .offset(0, line*tileYSize)
                        .build()
            );
        }

        return tiles;
    }
    //private static final int MAX_DRAWING_SIZE = 1024;
    public static final int MAX_DRAWING_SIZE = 64;
    private int nbOfTiles() {
        int drawingSize = this.size();
        int tilesQuotient = drawingSize / MAX_DRAWING_SIZE;
        int tilesRemainder = drawingSize % MAX_DRAWING_SIZE;

        if(tilesQuotient==0) return 1;

        if(tilesQuotient!=0 && tilesRemainder !=0) {
            throw new UnsupportedOperationException(String.format(
                "drawingSize=%d is not a multiple of MAX_DRAWING_SIZE=%d: remainder=%d",
                drawingSize,
                MAX_DRAWING_SIZE,
                tilesRemainder)
            );
        }

        return tilesQuotient;
    }

}