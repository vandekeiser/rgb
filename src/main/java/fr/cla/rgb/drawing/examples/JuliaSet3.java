package fr.cla.rgb.drawing.examples;

import java.util.IntSummaryStatistics;
import fr.cla.rgb.drawer.ParallelAsyncTilingDrawer;
import fr.cla.rgb.drawing.Point;
import fr.cla.rgb.drawing.WholeDrawing;

public class JuliaSet3 extends WholeDrawing {

    private int maxDivergingIteration;

    public JuliaSet3(int size) {
        super(size);
    }

    public static void main(String... args) throws Exception {
        JuliaSet3 js = new JuliaSet3(1024);
        System.out.println("Computing color scale..");
        IntSummaryStatistics divergingIterationStats = js.points().parallel().mapToInt(
                p -> js.divergingIteration(p.x, p.y, js.wholeDrawingSize())
        ).summaryStatistics();
        js.maxDivergingIteration = divergingIterationStats.getMax();
        System.out.println("Color scale: stats="+divergingIterationStats);
        
        ParallelAsyncTilingDrawer.INSTANCE.draw(js);
    }

    @Override protected int RGB(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        
        int n = divergingIteration(x, y, s);
        double lambda = lambda(n);
        int[] rgb = U.waveLengthToRGB(lambda);
        
        return (rgb[0] << 8 | rgb[1]) << 8 | rgb[2];
    }

    private double lambda(int n) {
        double red = 380.0, purple = 780.0, range = purple - red;
        //double red = 490.0, purple = 510.0, range = purple - red;   //voit qetchos
        //double red = 470.0, purple = 500.0, range = purple - red;
        return red + (range/ maxDivergingIteration) * n;
    }
    
    private static int divergingIteration(int i, int j, int size) {
        float x = D(i, size), 
              y = D(j, size),
              X,
              Y; 
        
        int n = 0, MAX_IT = 600;
        
        while(n++<MAX_IT && (x*x+y*y)<4.0) {//certain divergence outside of the circle of radius 2
            X = x;
            Y = y;
            x = X*X - Y*Y + 0.36237F;
            y = 2*X*Y     + 0.32F;
        }
        return n;
    }
    
    private static float D(int x, int size) {
        return (x - size/2.0F)/(size/2.0F);
    }

 
    @Override protected int R(int x, int y, int wholeDrawingsize) {
            throw new AssertionError();
        }
        @Override protected int G(int x, int y, int wholeDrawingsize) {
            throw new AssertionError();
        }
        @Override protected int B(int x, int y, int wholeDrawingsize) {
            throw new AssertionError();
        }
}