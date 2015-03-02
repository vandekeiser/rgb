package fr.cla.rgb.drawer.opencv;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import fr.cla.rgb.drawer.WrittenImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;

public class OpenCv {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    public static void loadOpenCvLibrary() {}
    
}