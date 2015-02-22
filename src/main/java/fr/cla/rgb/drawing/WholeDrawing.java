package fr.cla.rgb.drawing;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class WholeDrawing extends Drawing {

    protected WholeDrawing(int size) {
        super(size, size);
    }

    final int size() {return xsize();}
    protected final int wholeDrawingSize() {return size();}

    @Override public final String name() {
        return getClass().getSimpleName() + "." + IMG_TYPE;
    }

    /**
     * @return A sequential Stream (that can't be made parallel)
     */
    public Stream<Tile> sequentialSplit() {
        return StreamSupport.stream(
            new SequentialSpliterator(this),
            false //"parallel" necessarily false
        );
    }
    
    /**
     * @return A sequential Stream (that can be made parallel with stream.parallel())
     */
    public Stream<Tile> divideAndConquerSplit() {
        return StreamSupport.stream(
            new DivideAndConquerSpliterator(this),
            false //"parallel" false, let the user of this API decide on parallelism
        );
    }

    /**
     * @return Split into lines: 
     * width of tiles (X) unchanged, 
     * height of tiles (Y) is whole height / nbOfLines()
     * @throws BadTilingException if size() % nbOfLines() != 0
     */
    public List<Tile> tileSequentially() {
        int size = size();
        int lines = nbOfLines();
        
        int tileXSize = size;
        int tileYSize = size / lines;
        int tileYSizeRemainder = size() % lines;
        if(tileYSizeRemainder!=0) throw new BadTilingException(String.format(
                "size=%d is not a multiple of lines=%d, remainder=%d",
                size,
                lines,
                tileYSizeRemainder
        ));

        return IntStream.range(0, lines)
                .mapToObj(line -> Tile.of(this)
                        .sized(tileXSize, tileYSize)
                        .offset(0, line * tileYSize)
                        .build()
                )
                .collect(Collectors.toList());
    }
    
    public static final int MAX_SIZE_BEFORE_SPLIT = 2048;

    /**
     * @return this.size() / MAX_SIZE_BEFORE_SPLIT
     * @throws BadTilingException if size()%MAX_SIZE_BEFORE_SPLIT!=0 && size()/MAX_SIZE_BEFORE_SPLIT!=0
     */
    public final int nbOfLines() {
        int wholeSize = this.size();
        int tilesQuotient = wholeSize / MAX_SIZE_BEFORE_SPLIT;
        int tilesRemainder = wholeSize % MAX_SIZE_BEFORE_SPLIT;

        if(tilesQuotient==0) return 1;

        if(tilesQuotient!=0 && tilesRemainder !=0) {
            throw new BadTilingException(String.format(
                "drawingSize=%d is not a multiple of MAX_SIZE_BEFORE_SPLIT=%d: remainder=%d",
                wholeSize,
                    MAX_SIZE_BEFORE_SPLIT,
                tilesRemainder)
            );
        }

        return tilesQuotient;
    }

}