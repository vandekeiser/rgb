# RGB Streams

WORK IN PROGRESS

 Heavily inspired by:
 * https://codegolf.stackexchange.com/questions/35569/tweetable-mathematical-art
 * https://codegolf.stackexchange.com/questions/22144/images-with-all-colors

Goals:
* Make the code easier to extend to any drawing by factoring points generation, rendering,
 and saving to a file, so that concrete classes only need to implement the RGB computation
* Make it work transparently for big images
* Test parallel streams speedups in various cases
 
Times are measured on an old 2-cores machine, mx1G


//    JAI
//    Libraries are not on Maven Central
//    Last stable release in 2006


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
        return IntStream.range(0, xsize).mapToObj(
                x -> IntStream.range(0, ysize).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist

        //OOME
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()).collect(toList()).stream(); //Workaround: IntStream.flatMapToObj doesn't exist
    }

TODO
 -Add finally for closes in PngjSamples
 -Split properly in SquareDrawing (no Deque)/DrawingSpliterator (non-null trySplit)
 -Pic
 -Completable
 -determiner nbtiles en fonction de x*y
 -!!stream(Spliterator<T> spliterator, boolean ____parallel____) {
 
 
 JuliaSet3_SPIRALSSIDE_65536_IT512_TAU5.png
 MAX_SIZE_BEFORE_SPLIT=512, availableProcessors=2
 Drawing Julia set SPIRALS_SIDE(SIZE=65536, MAX_ITERATIONS=512, COLOR_SCALE=Interpolating.EXPONENTIALLY, TAU=5,00, WAVELENGTH_TO_RGB=THROUGH.HSV)
 Computing diverging iteration stats..
 Diverging iteration took PT22M19.379S, results:IntSummaryStatistics{count=4294967296, sum=407291695105, min=2, average=94,829988, max=513}
 Color scale: COLOR_SCALE.wavelength(0.0)=380
 Color scale: COLOR_SCALE.wavelength(0.25)=667
 Color scale: COLOR_SCALE.wavelength(0.5)=750
 Color scale: COLOR_SCALE.wavelength(0.75)=773
 Color scale: COLOR_SCALE.wavelength(1.0)=780
 Color scale: COLOR_SCALE.wavelength(1.1)=781
 ParallelAsyncTilingDrawer/draw/will store tiles in temp directory: C:\Users\User\AppData\Local\Temp\tiles_7910748554651067801
 ParallelAsyncTilingDrawer/draw/start writing 128 tiles
 ParallelAsyncTilingDrawer/draw/done writing 128 tiles, it took PT39M32.877S
 ParallelAsyncTilingDrawer/draw/stitching tiles together
 ParallelAsyncTilingDrawer/draw/done stitching 128 tiles, it took PT6H51M45.248S
 -->snapshot-1425198534491-cpu-calltree.png

https://www.mail-archive.com/core-libs-dev@openjdk.java.net/msg00131.html
http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6399199
flusher pngw periodiquement?