package fr.cla.rgb.drawing.examples;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.IntSummaryStatistics;
import fr.cla.rgb.drawer.OpencvParallelAsyncTilingDrawer;
import fr.cla.rgb.drawing.Point;
import fr.cla.rgb.drawing.WholeDrawing;

public class BigDecimalJuliaSet4 extends WholeDrawing {

    private int maxDivergingIteration;
    public BigDecimalJuliaSet4(int size) {
        super(size);
    }

    public static void main(String... args) throws Exception {
        BigDecimalJuliaSet4 js = new BigDecimalJuliaSet4(1024);
        System.out.println("Computing color scale..");
        IntSummaryStatistics divergingIterationStats = js.points().parallel().mapToInt(
                p -> js.divergingIteration(p.x, p.y, js.wholeDrawingSize())
        ).summaryStatistics();
        js.maxDivergingIteration = divergingIterationStats.getMax();
        System.out.println("Color scale: stats="+divergingIterationStats);
        
        OpencvParallelAsyncTilingDrawer.INSTANCE.draw(js);
    }

    @Override protected int RGB(Point p, int wholeDrawingsize) {
        int x = p.x, y = p.y, s = wholeDrawingsize;
        
        int n = divergingIteration(x, y, s);
        BigDecimal lambda = wavelength(n);
        int[] rgb = U.wavelengthToRgb(lambda);
        
        return (rgb[0] << 8 | rgb[1]) << 8 | rgb[2];
    }

    private BigDecimal wavelength(int n) {
        BigDecimal red = new BigDecimal("380"),
                purple = new BigDecimal("780"),
                range  = purple.subtract(red);
        //double red = 540.0, purple = 600.0, range = purple - red;
        //double red = 490.0, purple = 510.0, range = purple - red;
        //double red = 470.0, purple = 500.0, range = purple - red;
        
        //if(n==1) return Double.NaN;
        
        //return red + (range/ maxDivergingIteration) * n;
        return red.add(BigDecimal
                        .valueOf((long) n)
                        .multiply(range, mc)
                        .divide(BigDecimal.valueOf((long) maxDivergingIteration), mc)
        );
    }
    
    final static BigDecimal 
            DIV_RADIUS = BigDecimal.valueOf(2),
            DIV_AREA   = DIV_RADIUS.pow(2);
    final static MathContext mc = MathContext.DECIMAL32;

    private static int divergingIteration(int i, int j, int size) {
        if(i%512==0 && j%512==0)System.out.printf("divergingIteration i=%d, j=%d%n", i, j);
        BigDecimal x = D(i, size, mc),
              y = D(j, size, mc),
              X, Y; //values before iteration
        
        //bizarrement structure plus jolie qd petit, 
        // plus gros ecrase les couleurs
        //mais trop petit (8) pas bon non plus.. 32 OK
        int n = 0, MAX_IT = 512;
        
        //f(z)=z^2+c, c=xadd + iyadd
        //double xadd = 0.36237, yadd = 0.32;     //interessant
        //double xadd = 0, yadd = 0;              //cercle
        //double xadd = 0.03515, yadd = -0.07467; //queud
        BigDecimal xadd = new BigDecimal("-0.672"), //interessant
                   yadd = new BigDecimal("0.435");

        //while(n++<MAX_IT && (x*x+y*y)<4.0)
        while(n++<MAX_IT && (x.pow(2, mc).add(y.pow(2, mc), mc)).compareTo(DIV_AREA)<0) {//certain divergence outside of the circle of radius 2
            X = x;
            Y = y;
            //z=x+iy -> z^2=x^2-y^2 + 2i*x*y
                //x = X*X - Y*Y + xadd;
            x = X.pow(2, mc).subtract(Y.pow(2, mc)).add(xadd, mc);
                //y = 2*X*Y     + yadd;
            y = BigDecimal.valueOf((long)2).multiply(X, mc).multiply(Y, mc).add(yadd, mc);
        }
        return n;
    }
    
    private static BigDecimal D(int x, int size, MathContext mc) {
        //return (x - size/2.0)/(size/2.0);
        BigDecimal halfSize = BigDecimal.valueOf((long)size).divide(DIV_RADIUS, mc);
        return (BigDecimal
                .valueOf((long) x)
                .subtract(halfSize, mc)
        ).divide(halfSize, mc);
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