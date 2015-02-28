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
        double lambda = wavelength(n);
        int[] rgb = U.waveLengthToRGB(lambda);
        
        return (rgb[0] << 8 | rgb[1]) << 8 | rgb[2];
    }

    private double wavelength(int n) {
        //double red = 380.0, purple = 780.0, range = purple - red;
        double red = 540.0, purple = 600.0, range = purple - red;
        //double red = 490.0, purple = 510.0, range = purple - red;
        //double red = 470.0, purple = 500.0, range = purple - red;
        
        //if(n==1) return Double.NaN;
        return red + (range/ maxDivergingIteration) * n;
    }
    
    private static int divergingIteration(int i, int j, int size) {
        double x = D(i, size),
              y = D(j, size),
              X, Y; //values before iteration
        int n = 0, MAX_IT = 1024;
        
        double xadd = 0.36237,
               yadd = 0.32;
        while(n++<MAX_IT && (x*x+y*y)<4.0) {//certain divergence outside of the circle of radius 2
            X = x;
            Y = y;
            //z=x+iy -> z^2=x^2-y^2 + 2i*x*y
            x = X*X - Y*Y + xadd;
            y = 2*X*Y     + yadd;
        }
        return n;
    }
    
    private static double D(int x, int size) {
        return (x - size/2.0)/(size/2.0);
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