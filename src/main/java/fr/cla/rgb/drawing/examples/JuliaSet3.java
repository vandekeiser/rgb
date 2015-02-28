package fr.cla.rgb.drawing.examples;

import java.util.IntSummaryStatistics;
import fr.cla.rgb.drawer.ParallelAsyncTilingDrawer;
import fr.cla.rgb.drawing.Point;
import fr.cla.rgb.drawing.WholeDrawing;
import static java.lang.Math.exp;

public class JuliaSet3 extends WholeDrawing {

    private int minDivergingIteration, maxDivergingIteration;

    public JuliaSet3(int size) {
        super(size);
    }

    public static void main(String... args) throws Exception {
        JuliaSet3 js = new JuliaSet3(SIZE);
        System.out.printf("Drawing Julia set %s, size %d %n", juliaSet, SIZE);
        
        System.out.println("Computing diverging iteration stats..");
        IntSummaryStatistics divergingIterationStats = js.points().parallel().mapToInt(
                p -> js.divergingIteration(p.x, p.y, js.wholeDrawingSize())
        ).summaryStatistics();
        js.maxDivergingIteration = divergingIterationStats.getMax();
        js.minDivergingIteration = divergingIterationStats.getMin();
        System.out.println("Diverging iteration: " + divergingIterationStats);
        
        System.out.printf("Color scale: colorScale.wavelength(0.0)=%.0f%n", colorScale.wavelength(0.0));
        System.out.printf("Color scale: colorScale.wavelength(0.25)=%.0f%n", colorScale.wavelength(0.25));
        System.out.printf("Color scale: colorScale.wavelength(0.5)=%.0f%n", colorScale.wavelength(0.5));
        System.out.printf("Color scale: colorScale.wavelength(0.75)=%.0f%n", colorScale.wavelength(0.75));
        System.out.printf("Color scale: colorScale.wavelength(1.0)=%.0f%n", colorScale.wavelength(1.0));
        System.out.printf("Color scale: colorScale.wavelength(1.1)=%.0f%n", colorScale.wavelength(1.1));

        ParallelAsyncTilingDrawer.INSTANCE.draw(js);
    }

    @Override protected int RGB(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        
        int divergingIteration = divergingIteration(x, y, s);
        
        //fractals traditionally black where no divergence is detected
        if(divergingIteration > MAX_ITERATIONS) return 0; 

        // E [0, 1]
        double relativeDivergingIteration = 1.0 
                * (divergingIteration-minDivergingIteration)
                /  (maxDivergingIteration-minDivergingIteration);
        
        // E [PURPLE, RED]
        double lambda = colorScale.wavelength(relativeDivergingIteration);
        
        int[] rgb = U.waveLengthToRGB(lambda);
        
        return (rgb[0] << 8 | rgb[1]) << 8 | rgb[2];
    }

    private static final int SIZE = 1024;
    private static final int MAX_ITERATIONS = 2048;
    //"time constant" of the exponential used to get more detail toward reds
    private static final double TAU = 100;
    private static final ColorScale colorScale = ColorScale.ColorScales.EXPONENTIAL;
    private static final JuliaSet juliaSet = JuliaSet.DRAGON;
    
    private static int divergingIteration(int i, int j, int size) {
        double x = D(i, size),
              y = D(j, size),
              X, Y; //values before iteration

        int n = 0;
        while(n++< MAX_ITERATIONS && (x*x+y*y)<4.0) {//certain divergence outside of the circle of radius 2
            X = x;
            Y = y;
            //f(z)=z^2+c
            //c=xadd + i*yadd
            //z=x+iy -> z^2=x^2-y^2 + 2i*x*y
            x = X*X - Y*Y + juliaSet.x;
            y = 2*X*Y     + juliaSet.y;
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
    
    enum JuliaSet {
        //Some of the more famous Julia sets are:
        CIRCLE           ( 0.0,       0.0     ),
        X1               ( 0.5,       0.0     ),
        X2               (-1.0,       0.0     ),
        DENDRITE         ( 0.0,      -1.0     ),
        RABBIT           (-0.1,       0.8     ),
        SPIRALS_TWO      (-0.672,     0.435   ),
        SPIRALS_NESTED   ( 0.36237,   0.32    ),
        SPIRALS_SIDE     (-0.7,       0.27015 ),
        DRAGON           ( 0.36,      0.1     ),
        ;
        
        private final double x;
        private final double y;
        JuliaSet(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    interface ColorScale {
        double PURPLE = 380.0, RED = 780.0, RANGE = RED - PURPLE;

        /**
         * *
         * @param x belongs to [0, 1]
         * @return belongs to [purple, red]
         */
        double wavelength(double x);
        
        enum ColorScales implements ColorScale {
            LINEAR {
                @Override public double wavelength(double x) {
                    return PURPLE + RANGE * x;
                }
            },
            EXPONENTIAL {
                @Override public double wavelength(double x) {
                    return PURPLE + RANGE * (1.0 - exp(-TAU * x))/(1.0 - exp(-TAU));
                }
            },
            ;
        }
    }

}