package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static java.util.function.Function.identity;

public abstract class Drawing {

    public static final String IMG_TYPE = "png";

    private final int size;

    protected Drawing(int size) {
        this.size = size;
    }

    public BufferedImage render() {
        int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            //__Should__ be threadsafe since we write to different pixels, so could use parallel()
        points().forEach(p -> img.setRGB(p.x, p.y, rgb(p, size)));
        return img;
    }

    public Stream<Point> points() {
//        //DiagonalSierpinsky(8192): Rendering took: PT6.194S
//        //JuliaSet(8192): Rendering took: PT4M19.659S
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist

        //DiagonalSierpinsky(8192): Rendering took: PT8.551S
        //JuliaSet(8192): Rendering took: PT2M29.091S
        return IntStream.range(0, size()).mapToObj(
                x -> IntStream.range(0, size()).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()).parallel(); //Workaround: IntStream.flatMapToObj doesn't exist

        //OOME
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()).collect(toList()).stream(); //Workaround: IntStream.flatMapToObj doesn't exist
    }

    protected final int size() {return size;}

    protected final int rgb(Point p, int size) {
        int x = p.x, y = p.y;
        return (r(x, y, size) << 8 | g(x, y, size)) << 8 | b(x, y, size);
    }
    protected abstract int r(int x, int y, int size);
    protected abstract int g(int x, int y, int size);
    protected abstract int b(int x, int y, int size);

    @Override public String toString() {
        return String.format("%s {size:%d}", getClass().getSimpleName(), size());
    }

    public boolean isSmallEnough() {
        return size()<=128; //For tests; OOME at 4096*4
    }

    public Stream<Tile> split() {
        Spliterator<Tile> thisSpliterator = new DrawingSpliterator(this);
        return StreamSupport.stream(thisSpliterator, false);
    }
    public Deque<Tile> tile() {
        int TILES_PER_LINE = 2;
        int nbOfTiles = nbOfTiles();
        int lines = nbOfTiles/TILES_PER_LINE;
        int linesRemainder = nbOfTiles%TILES_PER_LINE;
//        if(linesRemainder!=0) throw new UnsupportedOperationException(String.format(
//                "nbOfTiles=%d is not a multiple of TILES_PER_LINE=%d, remainder=%d",
//                nbOfTiles,
//                TILES_PER_LINE,
//                linesRemainder
//        ));

        int size = size();
        int tileSize = size/nbOfTiles;
        int tileSizeRemainder = size()%nbOfTiles;
        if(tileSizeRemainder!=0) throw new UnsupportedOperationException(String.format(
                "nbOfTiles=%d is not a multiple of size=%d, remainder=%d",
                nbOfTiles,
                size,
                tileSizeRemainder
        ));

        System.out.println("nbOfTiles: " + nbOfTiles);
        Deque<Tile> tiles = new LinkedList<>();

//        for(int line = 0; line < lines; line++) {
//            for(int col = 0; col < TILES_PER_LINE; col++) {
//                tiles.add(new Tile(this, col*tileSize, line*tileSize));
//            }
//        }
        int col=0, line=0;
        for(int tile = 1; tile <= nbOfTiles; tile++) {
            tiles.add(new Tile(this, tileSize, col*tileSize, line*tileSize));
            ++col;
            if(TILES_PER_LINE%tile==0) {
                col=0; ++line;
            }
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