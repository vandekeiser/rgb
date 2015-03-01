package fr.cla.rgb.drawer.opencv;

import com.sun.java.swing.plaf.windows.resources.windows;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;

class SimpleSample {

  //static{ System.loadLibrary("opencv_java248"); }
    //[INFO] Copying opencvjar-runtime-2.4.8-natives-windows-x86_64.jar to g:\projets\
    //blog\rgb\target\lib\opencvjar-runtime-2.4.8-natives-windows-x86_64.jar
    //PAS MAJ TJRS EN VERSION 2.4.8..
    
    //-Djava.library.path=G:\projets\blog\rgb\opencv-full\opencv\build\java\x64
    //UnsatisfiedLinkError: no opencv_java2410 in java.library.path
  static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

  public static void main(String... args) {
    Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
    System.out.println("OpenCV Mat: " + m);
    Mat mr1 = m.row(1);
    mr1.setTo(new Scalar(1));
    Mat mc5 = m.col(5);
    mc5.setTo(new Scalar(5));
    System.out.println("OpenCV Mat data:\n" + m.dump());
  }

}