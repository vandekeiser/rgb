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
http://www.devguli.com/blog/eng/java-deflater-and-outofmemoryerror/
flusher pngw periodiquement? queueChunk(PngChunk chunk) ?
reinitialiser un pngw a chaque tile?


JuliaSet3_SPIRALSSIDE_32768_IT512_TAU5.png, sans setIdatMaxSize
    "C:\Program Files\Java\jdk1.8.0_25\bin\java" -Didea.launcher.port=7536 "-Didea.launcher.bin.path=C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 14.0.2\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_25\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\rt.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\zipfs.jar;G:\projets\blog\rgb\target\classes;C:\Users\User\.m2\repository\org\apache\commons\commons-math3\3.4.1\commons-math3-3.4.1.jar;C:\Users\User\.m2\repository\ar\com\hjg\pngj\2.1.0\pngj-2.1.0.jar;C:\Users\User\.m2\repository\org\testng\testng\6.8.13\testng-6.8.13.jar;C:\Users\User\.m2\repository\org\beanshell\bsh\2.0b4\bsh-2.0b4.jar;C:\Users\User\.m2\repository\com\beust\jcommander\1.27\jcommander-1.27.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 14.0.2\lib\idea_rt.jar" com.intellij.rt.execution.application.AppMain fr.cla.rgb.drawing.examples.JuliaSet3
    MAX_SIZE_BEFORE_SPLIT=512, availableProcessors=2
    Drawing Julia set SPIRALS_SIDE(SIZE=32768, MAX_ITERATIONS=512, COLOR_SCALE=EXPONENTIALLY, TAU=5,00, WAVELENGTH_TO_RGB=HSV)
    Computing diverging iteration stats..
    Diverging iteration stats took PT5M20.69S, results:IntSummaryStatistics{count=1073741824, sum=101823792209, min=2, average=94,830796, max=513}
    Color scale: COLOR_SCALE.wavelength(0.0)=380
    Color scale: COLOR_SCALE.wavelength(0.25)=667
    Color scale: COLOR_SCALE.wavelength(0.5)=750
    Color scale: COLOR_SCALE.wavelength(0.75)=773
    Color scale: COLOR_SCALE.wavelength(1.0)=780
    Color scale: COLOR_SCALE.wavelength(1.1)=781
    ParallelAsyncTilingDrawer/draw/will store tiles in temp directory: C:\Users\User\AppData\Local\Temp\tiles_1603396277261226541
    ParallelAsyncTilingDrawer/draw/start writing 64 tiles
    ParallelAsyncTilingDrawer/draw/done writing 64 tiles, it took PT10M17.737S
    ParallelAsyncTilingDrawer/draw/stitching tiles together
    ParallelAsyncTilingDrawer/draw/done stitching 64 tiles, it took PT11M6.468S

JuliaSet3_SPIRALSSIDE_32768_IT512_TAU5.png, avec setIdatMaxSize(1<<10)
    ParallelAsyncTilingDrawer/draw/done stitching 64 tiles, it took PT11M12.819S
    
si ecrit en parallele (1 pngw par iteration) ca marche pas
    
ImageIo du jdk: TOO_BIG = 16384;
    1/implements java.awt.image.WritableRenderedImage: dur
    2/utiliser un autre format que png, qui supporte le tiling
    http://blogs.mathworks.com/steve/2011/09/23/dealing-with-really-big-images-image-adapters/
    ->"Not all file formats are amenable to incremental "region-based" I/O."
    
    3/overrider bufferedimage, et marcher en "pull"?
IM4J: 
    bien (montage *.png output.png), mais imagemagick ca marche pas! (access denied * N)
    graphicsmagick ca s'installe mm pas..
PROCESSING
    PImage.set(x, y, img) a l'air pratique mais COMMENT ON FAIT AVEC UNE GROSSE IMAGE??
netpbm
    vieux et compliqué
OpenCV
    a l'air plus serieux que les guignolos de imagemagick 
    org.opencv.imgproc.Imgproc.accumulate(Mat src, Mat dst) 
        "Adds an image to the accumulator."
        ressemble a http://computer-vision-talks.com/articles/tile-based-image-processing/
            -copyTileToResultImage(tileOutput, resultImage, dstTile);
            -void copyTileToResultImage(const cv::Mat& tileImage, cv::Mat& resultImage, cv::Rect resultRoi);
        "java opencv tile processing"


    8196:
BasicDrawer.INSTANCE.draw(js);                     //1M11.515S
SingleThreadedTilingDrawer.INSTANCE.draw(js);      //1M56.269S
ParallelBlockingTilingDrawer.INSTANCE.draw(js);    //1M26S
PngjParallelAsyncTilingDrawer.INSTANCE.draw(js);   //1M22.74S
OpencvParallelAsyncTilingDrawer.INSTANCE.draw(js); //40.654S
OpencvAsyncDrawer.INSTANCE.draw(js);               //40.825S

    16384:
OpencvAsyncDrawer.INSTANCE.draw(js);               //2M36.852S
    32768
                                                   //16M16.075S
                                                   
                                                   
"C:\Program Files\Java\jdk1.8.0_25\bin\java" -mx1G -Djava.library.path=G:\projets\blog\rgb\opencv-full\opencv\build\java\x64 -Didea.launcher.port=7536 "-Didea.launcher.bin.path=C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 14.0.2\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_25\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\rt.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_25\jre\lib\ext\zipfs.jar;G:\projets\blog\rgb\target\classes;C:\Users\User\.m2\repository\org\apache\commons\commons-math3\3.4.1\commons-math3-3.4.1.jar;C:\Users\User\.m2\repository\ar\com\hjg\pngj\2.1.0\pngj-2.1.0.jar;C:\Users\User\.m2\repository\org\im4java\im4java\1.4.0\im4java-1.4.0.jar;C:\Users\User\.m2\repository\com\byteground\byteground-opencv_2.10\0.4.0\byteground-opencv_2.10-0.4.0.jar;C:\Users\User\.m2\repository\org\scala-lang\scala-library\2.10.4\scala-library-2.10.4.jar;C:\Users\User\.m2\repository\org\scala-lang\scala-reflect\2.10.4\scala-reflect-2.10.4.jar;C:\Users\User\.m2\repository\com\byteground\byteground-opencv-native_2.10\0.4.0\byteground-opencv-native_2.10-0.4.0.jar;C:\Users\User\.m2\repository\org\testng\testng\6.8.13\testng-6.8.13.jar;C:\Users\User\.m2\repository\org\beanshell\bsh\2.0b4\bsh-2.0b4.jar;C:\Users\User\.m2\repository\com\beust\jcommander\1.27\jcommander-1.27.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 14.0.2\lib\idea_rt.jar" com.intellij.rt.execution.application.AppMain fr.cla.rgb.drawing.examples.JuliaSet3
MAX_SIZE_BEFORE_SPLIT=512, availableProcessors=2
Drawing Julia set SPIRALS_SIDE(SIZE=65536-128 tiles, MAX_ITERATIONS=512, COLOR_SCALE=EXPONENTIALLY, TAU=5,00, WAVELENGTH_TO_RGB=HSV)
Computing diverging iteration stats..
Diverging iteration stats took PT20M59.792S, results:IntSummaryStatistics{count=4294967296, sum=407291695105, min=2, average=94,829988, max=513}
Color scale: COLOR_SCALE.wavelength(0.0)=380
Color scale: COLOR_SCALE.wavelength(0.25)=667
Color scale: COLOR_SCALE.wavelength(0.5)=750
Color scale: COLOR_SCALE.wavelength(0.75)=773
Color scale: COLOR_SCALE.wavelength(1.0)=780
Color scale: COLOR_SCALE.wavelength(1.1)=NaN
OpencvAsyncDrawer/draw/will store tiles in temp directory: C:\Users\User\AppData\Local\Temp\tiles_8541515164998254189
________7eda2dbb/new, nbOfTiles=128
OpenCV Error: Insufficient memory (Failed to allocate 4 bytes) in cv::OutOfMemoryError, file ..\..\..\..\opencv\modules\core\src\alloc.cpp, line 52
Exception in thread "main" CvException [org.opencv.core.CvException: cv::Exception: ..\..\..\..\opencv\modules\core\src\alloc.cpp:52: error: (-4) Failed to allocate 4 bytes in function cv::OutOfMemoryError
]
at org.opencv.core.Mat.n_Mat(Native Method)
at org.opencv.core.Mat.<init>(Mat.java:477)
at fr.cla.rgb.drawer.opencv.OpenCvTiling.tile(OpenCvTiling.java:77)
at fr.cla.rgb.drawer.OpencvAsyncDrawer.draw(OpencvAsyncDrawer.java:42)
at fr.cla.rgb.drawing.examples.JuliaSet3.main(JuliaSet3.java:49)
at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
at java.lang.reflect.Method.invoke(Method.java:483)
at com.intellij.rt.execution.application.AppMain.main(AppMain.java:134)

Process finished with exit code 1
