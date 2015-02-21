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
 
Times are measured on an old 2-cores machine


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