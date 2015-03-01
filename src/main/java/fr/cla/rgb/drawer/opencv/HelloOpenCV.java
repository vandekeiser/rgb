package fr.cla.rgb.drawer.opencv;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
class DetectFaceDemo {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    
    public void run() throws URISyntaxException {
        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        String confPath = Paths.get(getClass().getResource("/lbpcascade_frontalface.xml").toURI()).toString();
        System.out.println("confPath: " + confPath);
        String imgPath = Paths.get(getClass().getResource("/lena.png").toURI()).toString();
        System.out.println("imgPath: " + imgPath);
        
        CascadeClassifier faceDetector = new CascadeClassifier(confPath);
        Mat image = Highgui.imread(imgPath);

        // Detect faces in the image.
        // MatOfRect is a special container class for Rect.
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // Save the visualized detection.
        String filename = "faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        Highgui.imwrite(filename, image);
    }
}

public class HelloOpenCV {
  public static void main(String[] args) throws URISyntaxException {
    System.out.println("Hello, OpenCV");

    new DetectFaceDemo().run();
  }
}