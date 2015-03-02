package fr.cla.rgb.drawer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import fr.cla.rgb.drawer.opencv.OpenCv;
import fr.cla.rgb.drawing.Drawing;
import fr.cla.rgb.drawing.WholeDrawing;
import fr.cla.rgb.drawing.examples.FirstTry;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.assertFalse;

public abstract class AbstractDrawerTest {

    private Instant whenTestMethodStarted;

    private Drawer drawer;
    protected abstract Drawer createDrawer();
    public final Drawer drawer() { return drawer; }
    
    protected static final WholeDrawing drawing_S = new FirstTry(1024),
                                        drawing_M = new FirstTry(2048),
                                        drawing_L = new FirstTry(8192);
    protected static final String drawingPath = drawing_S.name();

    
    @BeforeClass public void beforeClass() {
        OpenCv.loadOpenCvLibrary();
    }
    
    @BeforeMethod public void before() throws IOException {
        //Start from blank slate every time
        Files.delete(Paths.get(drawingPath));
        assertFalse(Paths.get(drawingPath).toFile().exists());
        
        drawer = createDrawer();
        
        this.whenTestMethodStarted = Instant.now();
    }

    @AfterMethod public void after(ITestResult result) {
        System.out.printf(
            "TEST %s took: %s%n",
            result.getMethod().getMethodName(),
            Duration.between(whenTestMethodStarted, Instant.now())
        );
    }
    
}
