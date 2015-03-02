package fr.cla.rgb.drawing.examples;

import java.time.Duration;
import java.time.Instant;
import java.util.IntSummaryStatistics;
import fr.cla.rgb.DrawExecutors;
import fr.cla.rgb.drawer.*;
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
        System.out.printf(
                "Drawing Julia set %s(SIZE=%d-%d tiles, MAX_ITERATIONS=%d, COLOR_SCALE=%s, TAU=%.2f, WAVELENGTH_TO_RGB=%s)%n",
                JULIA_SET, SIZE, js.nbOfLines(), MAX_ITERATIONS, COLOR_SCALE, TAU, WAVELENGTH_TO_RGB
        );
        
        System.out.println("Computing diverging iteration stats..");
        Instant beforeStats = Instant.now();
        IntSummaryStatistics divergingIterationStats = js.points().parallel().mapToInt(
                p -> js.divergingIteration(p.x, p.y, js.wholeDrawingSize())
        ).summaryStatistics();
        js.maxDivergingIteration = divergingIterationStats.getMax();
        js.minDivergingIteration = divergingIterationStats.getMin();
        System.out.printf("Diverging iteration stats took %s, results:%s%n", Duration.between(beforeStats, Instant.now()), divergingIterationStats);
        
        System.out.printf("Color scale: COLOR_SCALE.wavelength(0.0)=%.0f%n", COLOR_SCALE.wavelength(0.0));
        System.out.printf("Color scale: COLOR_SCALE.wavelength(0.25)=%.0f%n", COLOR_SCALE.wavelength(0.25));
        System.out.printf("Color scale: COLOR_SCALE.wavelength(0.5)=%.0f%n", COLOR_SCALE.wavelength(0.5));
        System.out.printf("Color scale: COLOR_SCALE.wavelength(0.75)=%.0f%n", COLOR_SCALE.wavelength(0.75));
        System.out.printf("Color scale: COLOR_SCALE.wavelength(1.0)=%.0f%n", COLOR_SCALE.wavelength(1.0));
        System.out.printf("Color scale: COLOR_SCALE.wavelength(1.1)=%.0f%n", COLOR_SCALE.wavelength(1.1));

        Instant START = Instant.now();
        //BasicDrawer.INSTANCE.draw(js);                      //1M11.515S
        //SingleThreadedTilingDrawer.INSTANCE.draw(js);     //1M56.269S
        //ParallelBlockingTilingDrawer.INSTANCE.draw(js);   //1M26S
        //PngjParallelAsyncTilingDrawer.INSTANCE.draw(js);      //1M22.74S
        //OpencvParallelAsyncTilingDrawer.INSTANCE.draw(js);      //40.654S
        OpencvAsyncDrawer.INSTANCE.draw(js);              // 40.825S

        System.out.printf("JuliaSet took:%s%n", Duration.between(START, Instant.now()));
    }

    @Override protected int RGB(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        
        int divergingIteration = divergingIteration(x, y, s);
        
        //fractals are traditionally black where no divergence is detected
        if(divergingIteration > MAX_ITERATIONS) return 0; 

        // E [0, 1]
        double relativeDivergingIteration = 1.0 
                * (divergingIteration-minDivergingIteration)
                /  (maxDivergingIteration-minDivergingIteration);
        
        // E [PURPLE, RED]
        double wavelength = COLOR_SCALE.wavelength(relativeDivergingIteration);
        /*
        Color scale: COLOR_SCALE.wavelength(0.0)=380
        Color scale: COLOR_SCALE.wavelength(0.25)=777
        Color scale: COLOR_SCALE.wavelength(0.5)=780
        Color scale: COLOR_SCALE.wavelength(0.75)=780
        Color scale: COLOR_SCALE.wavelength(1.0)=780
        Color scale: COLOR_SCALE.wavelength(1.1)=780
        -->Jouer sur le brightness pour les distinguer?
        
              Color scale: COLOR_SCALE.wavelength(0.0)=380
              Color scale: COLOR_SCALE.wavelength(0.25)=667
              Color scale: COLOR_SCALE.wavelength(0.5)=750
              Color scale: COLOR_SCALE.wavelength(0.75)=773
              Color scale: COLOR_SCALE.wavelength(1.0)=780
              Color scale: COLOR_SCALE.wavelength(1.1)=781
                                                          -->KO?
        * * * */
        
        return WAVELENGTH_TO_RGB.toRgb(wavelength);
    }

    static final int SIZE = 8192; //1024, 2048, 4096, 8192, 16384, 32768(12mn/5mn), 65536
    static final int MAX_ITERATIONS = 512;
    //"time constant" of the exponential used to get more detail toward reds
    static final double TAU = 5.0;
    static final ColorScale COLOR_SCALE = ColorScale.Interpolating.EXPONENTIALLY;
    static final WavelengthToRgb WAVELENGTH_TO_RGB = WavelengthToRgb.Through.HSV;
    static final JuliaSet JULIA_SET = JuliaSet.SPIRALS_SIDE;//512/5.0
    
    static int divergingIteration(int i, int j, int size) {
        double x = value(i, size),
              y = value(j, size),
              X, Y; //values before iteration

        int n = 0;
        while(n++< MAX_ITERATIONS && (x*x+y*y)<4.0) {//certain divergence outside of the circle of radius 2
            X = x;
            Y = y;
            //f(z)=z^2+c
            //c=xadd + i*yadd
            //z=x+iy -> z^2=x^2-y^2 + 2i*x*y
            x = X*X - Y*Y + JULIA_SET.x;
            y = 2*X*Y     + JULIA_SET.y;
        }
        return n;
    }
    
    static double value(int index, int size) {
        //i E [0, size] 
        // --> (i-size/2) E [-size/2, +size/2] 
        // --> ret E [-1, 1]
        return (index - size/2.0)/(size/2.0);
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
        
        final double x, y;
        JuliaSet(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    interface ColorScale {
        //double PURPLE = 380.0, RED = 780.0, RANGE = RED - PURPLE;
        double PURPLE = U.PURPLE_WAVELENGTH, RED = U.RED_WAVELENGTH, RANGE = RED - PURPLE;

        /**
         * @param x belongs to [0, 1]
         * @return belongs to [purple, red]
         */
        double wavelength(double x);
        
        enum Interpolating implements ColorScale {
            LINEARLY { @Override public double wavelength(double x) {
                if(x<0.0 || x>1.0) return Double.NaN;
                return PURPLE + RANGE * x;
            }},
            EXPONENTIALLY { @Override public double wavelength(double x) {
                if(x<0.0 || x>1.0) return Double.NaN;
                return PURPLE + RANGE * (1.0 - exp(-TAU * x))/(1.0 - exp(-TAU));
            }},
            ;
        }
    }
    
    interface WavelengthToRgb {
        int toRgb(double wavelength);
        
        enum Through implements WavelengthToRgb {
            RGB { @Override public int toRgb(double wavelength) {
                int[] rgb = U.wavelengthToRgb(wavelength);
                return (rgb[0] << 8 | rgb[1]) << 8 | rgb[2];
            }},
            HSV { @Override public int toRgb(double wavelength) {
                return U.wavelengthToHsbToRgb(wavelength);
            }},
            ;
        }
    }

}