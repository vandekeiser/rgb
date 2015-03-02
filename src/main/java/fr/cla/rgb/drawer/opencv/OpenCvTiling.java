package fr.cla.rgb.drawer.opencv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class OpenCvTiling {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void tile(String[] tilesPaths, String outPath) throws IOException {
        Stream<String> tilesPathsStream = Arrays.stream(tilesPaths);
        Stream<Mat> tilesMatsStream = tilesPathsStream.map(Highgui::imread);
        
        Mat firstTileMat = Highgui.imread(tilesPaths[0]);

        Mat outMat = new Mat(
                firstTileMat.rows()*tilesPaths.length, 
                firstTileMat.cols()*tilesPaths.length,
                firstTileMat.type());
        
        
        
        tilesMatsStream.forEach(tilesMat -> 
//                Imgproc.accumulate(tilesMat, outMat)
                        tilesMat.copyTo(outMat)
        );
        Highgui.imwrite(outPath, outMat);
//        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
//        System.out.println("OpenCV Mat: " + m);
//        Mat mr1 = m.row(1);
//        mr1.setTo(new Scalar(1));
//        Mat mc5 = m.col(5);
//        mc5.setTo(new Scalar(5));
//        System.out.println("OpenCV Mat data:\n" + m.dump());
        
        //Files.createFile(Paths.get("OpenCvTilingTest.png"));
    }
}