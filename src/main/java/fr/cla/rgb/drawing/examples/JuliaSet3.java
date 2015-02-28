package fr.cla.rgb.drawing.examples;

import java.util.IntSummaryStatistics;
import fr.cla.rgb.drawer.ParallelAsyncTilingDrawer;
import fr.cla.rgb.drawing.Point;
import fr.cla.rgb.drawing.WholeDrawing;
import static java.lang.Math.*;

public class JuliaSet3 extends WholeDrawing {

    private int minDivergingIteration, maxDivergingIteration;

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
        js.minDivergingIteration = divergingIterationStats.getMin();
        System.out.println("Color scale: stats=" + divergingIterationStats);
        System.out.printf("Color scale: nonlinearWavelength(0)=%.0f%n", js.nonlinearWavelength(0));
        System.out.printf("Color scale: nonlinearWavelength(maxDivergingIteration/2)=%.0f%n", 
                js.nonlinearWavelength((int) (js.maxDivergingIteration/2.0)));
        System.out.printf("Color scale: nonlinearWavelength(maxDivergingIteration*3/4)=%.0f%n", 
                js.nonlinearWavelength((int) (js.maxDivergingIteration*3.0/4.0)));
        System.out.printf("Color scale: nonlinearWavelength(maxDivergingIteration)=%.0f%n", 
                js.nonlinearWavelength(js.maxDivergingIteration));
        System.out.printf("Color scale: nonlinearWavelength(maxDivergingIteration+1)=%.0f%n", 
                js.nonlinearWavelength(js.maxDivergingIteration+1));

        ParallelAsyncTilingDrawer.INSTANCE.draw(js);
    }

    @Override protected int RGB(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        
        int n = divergingIteration(x, y, s);
        //double lambda = linearWavelength(n);
        double lambda = nonlinearWavelength(n);
        int[] rgb = U.waveLengthToRGB(lambda);
        
        return (rgb[0] << 8 | rgb[1]) << 8 | rgb[2];
    }

    private double linearWavelength(int divergingIteration) {
        if(divergingIteration > MAX_IT) return Double.NaN;
        double purple = 380.0, red = 780.0, range = red - purple;
        return purple + (range*1.0/maxDivergingIteration) * divergingIteration;
    }
    
    private double nonlinearWavelength(int divergingIteration) {
        if(divergingIteration > MAX_IT) return Double.NaN; //black if no divergence detected

        double purple = 380.0, red = 780.0, range = red - purple;

        double x = 1.0*(divergingIteration-minDivergingIteration)/(maxDivergingIteration-minDivergingIteration); // E [0, 1]

        return purple + range * (1.0 - exp(-TAU*x))/(1.0 - exp(-TAU));
    }
    private static int MAX_IT = 2048;
    //"time constant" of the exponential used to get more detail toward reds
    private static double TAU = 100;
    
    private static int divergingIteration(int i, int j, int size) {
        double x = D(i, size),
              y = D(j, size),
              X, Y; //values before iteration

//Some of the more famous Julia sets are:
        //double xadd = 0, yadd = 0;           //cercle
        //double xadd = 0.5, yadd = 0.0;       //semi-interessant
        //double xadd = -1.0, yadd = 0.0;      //semi-interessant
        //double xadd = 0.0, yadd = -1.0;      //"the Dendrite"
        //double xadd = -0.1, yadd = 0.8;      //"the Rabbit"
        //double xadd = -0.672, yadd = 0.435;  //"spirales jumelles"
        //double xadd = 0.36237, yadd = 0.32;  //"spirales imbriquees"
        //double xadd = -0.7, yadd = 0.27015;  //"spirales sur spirales", 2018/10

        double xadd = 0.36, yadd = 0.1;      //"the Dragon"

        int n = 0;
        while(n++<MAX_IT && (x*x+y*y)<4.0) {//certain divergence outside of the circle of radius 2
            X = x;
            Y = y;
            //f(z)=z^2+c
            //c=xadd + i*yadd
            //z=x+iy -> z^2=x^2-y^2 + 2i*x*y
            x = X*X - Y*Y + xadd;
            y = 2*X*Y     + yadd;
        }
        return n;
    }
    
    private static double D(int i, int size) {
        //i E [0, size] 
        // --> (i-size/2) E [-size/2, +size/2] 
        // --> ret E [-1, 1]
        return (i - size/2.0)/(size/2.0);
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