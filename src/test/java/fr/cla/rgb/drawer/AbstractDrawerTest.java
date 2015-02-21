package fr.cla.rgb.drawer;

import java.time.Duration;
import java.time.Instant;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class AbstractDrawerTest {

    private Instant whenTestMethodStarted;

    private Drawer drawer;
    protected abstract Drawer createDrawer();
    public final Drawer drawer() { return drawer; }
    
    @BeforeMethod public void before() {
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
