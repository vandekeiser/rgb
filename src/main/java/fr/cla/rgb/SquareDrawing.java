package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class SquareDrawing extends Drawing {

    protected SquareDrawing(int size) {
        super(size, size);
    }

    public BufferedImage render() {
        int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            //__Should__ be threadsafe since we write to different pixels, so could use parallel()
        points().forEach(p -> img.setRGB(p.x, p.y, rgb(p, size)));
        return img;
    }

    public final int size() {return xsize();}

    @Override public String toString() {
        return String.format("%s {size:%d}", getClass().getSimpleName(), size());
    }

    public Stream<Tile> split() {
        Spliterator<Tile> thisSpliterator = new DrawingSpliterator(this);
        return StreamSupport.stream(thisSpliterator, false);
    }
    public Deque<Tile> tile() {
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
            tiles.add(new Tile(this, tileXSize, tileYSize, 0, line*tileYSize));
        }

        return tiles;
    }
    private static final int MAX_DRAWING_SIZE = 1024;
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