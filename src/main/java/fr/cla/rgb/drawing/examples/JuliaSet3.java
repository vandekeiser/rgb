package fr.cla.rgb.drawing.examples;

import java.util.IntSummaryStatistics;
import fr.cla.rgb.drawer.ParallelAsyncTilingDrawer;
import fr.cla.rgb.drawing.WholeDrawing;

public class JuliaSet3 extends WholeDrawing {

    private int maxn;

    public JuliaSet3(int size) {
        super(size);
    }

    public static void main(String... args) throws Exception {
        //ParallelTilingDrawer.INSTANCE.draw(new JuliaSet(8192));
        //ParallelTilingDrawer.INSTANCE.draw(new JuliaSet(32768));
        //ParallelTilingDrawer.INSTANCE.draw(new JuliaSet(65536)); //MAX_SIZE_BEFORE_SPLIT = 2048 --> OK
        
        //SingleThreadedTilingDrawer.INSTANCE.draw(new JuliaSet(16384)); //8 tiles, it took PT18M11.291S
        //ParallelTilingDrawer.INSTANCE.draw(new JuliaSet(16384)); //8 tiles, it took PT9M49.664S
        //ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(16384)); //8 tiles, it took PT9M39.954S
        //avec runAsync(cachedThreadPool):
        //ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(16384)); //8 tiles, it took PT10M1.04
        //ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(16384)); //128 tiles, it took PT9M55.406S
        //ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(16384)); //2048 tiles, it took PT10M30.518S
        
        //ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(1024));

        //PC du taf (MAX=1024, cores=8):
//        ParallelAsyncTilingDrawer/draw/done writing 16 tiles, it took PT1M47.278S
//        ParallelAsyncTilingDrawer/draw/stitching tiles together
//        ParallelAsyncTilingDrawer/draw/done stitching 16 tiles, it took PT3M14.203S
        //ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(16384));

        //PC du taf (MAX=1024, cores=8):
//        //done writing 256 tiles, it took PT7M5.605S
//        ParallelAsyncTilingDrawer/draw/stitching tiles together
//        ParallelAsyncTilingDrawer/draw/done stitching 256 tiles, it took PT10M26.605S
//        ParallelAsyncTilingDrawer.INSTANCE.draw(new JuliaSet(32768));
        
        //AsyncDrawer.INSTANCE.draw(new JuliaSet(2048));
        JuliaSet3 js = new JuliaSet3(1024);
        System.out.println("Computing color scale..");
        IntSummaryStatistics stats = js.points().parallel().mapToInt(p -> JuliaSet3.n(p.x, p.y, js.wholeDrawingSize())).summaryStatistics();
        js.maxn = stats.getMax();
        System.out.println("Color scale: stats="+stats);
        

        ParallelAsyncTilingDrawer.INSTANCE.draw(js);
    }

    @Override protected int R(int i, int j, int size) {
        //int n = n(i, j, size);
        float x=D(i, size),y=D(j, size),X,Y; int n=0;
        while(n++<200&&(X=x*x)+(Y=y*y)<4){x=X-Y+0.36237F;y=2*x*y+0.32F;}
        
        double lambda = lambda(n);
        int[] rgb = U.waveLengthToRGB(lambda);
        return rgb[0];
    }

    @Override protected int G(int i, int j, int size) {
        //int n = n(i, j, size);
        float x=D(i, size),y=D(j, size),X,Y; int n=0;
        while(n++<200&&(x*x+y*y)<4){X=x;Y=y;x=X*X-Y*Y+-0.7F;y=2*X*Y+0.27015F;}
        
        double lambda = lambda(n);
        int[] rgb = U.waveLengthToRGB(lambda);
        return rgb[1];
    }
    @Override protected int B(int i, int j, int size) {
        //int n = n(i, j, size);
        float x=D(i, size),y=D(j, size),X,Y; int n=0;
        while(n++<600&&(x*x+y*y)<4){X=x;Y=y;x=X*X-Y*Y+0.36237F;y=2*X*Y+0.32F;}
        
        double lambda = lambda(n);
        int[] rgb = U.waveLengthToRGB(lambda);
        return rgb[2];
    }

    private double lambda(int n) {
        //double red = 380.0, purple = 780.0, range = purple - red;    //voit rien
        //double red = 490.0, purple = 510.0, range = purple - red;   //voit qetchos
        double red = 470.0, purple = 500.0, range = purple - red;
        return red + (range/maxn) * n;
    }
    
    static float D(int x, int size) {
        return (x-size/2.0F)/(size/2.0F);
    }
    
    private static int n(int i, int j, int size) {
        float x=D(i, size), y=D(j, size), X, Y;
        int n=0;
        while(n++<1024&&(X=x*x)+(Y=y*y)<4){x=X-Y+0.36237F;y=2*x*y+0.32F;}
        return n;
        //return (int)(Math.log(n));
    }
    
    int as0xff(double d01) {
        return (int) (d01 * 0xff);
    }
    
//    double red01(double w) {
//        if (w >= 379.0 && w < 440.0) return (w - 440.0) / (440.0 - 380.0);
//        if (w >= 440.0 && w < 490.0) return 0.0;
//        if (w >= 490.0 && w < 510.0) return 0.0;
//        if (w >= 510.0 && w < 580.0) return (w - 510.0) / (580.0 - 510.0);
//        if (w >= 580.0 && w < 645.0) return 1.0;
//        if (w >= 645.0 && w < 781.0) return 1.0;
//        throw new AssertionError();
//    }
//    double green01(double w) {
//        if (w >= 379.0 && w < 440.0) return 0.0;
//        if (w >= 440.0 && w < 490.0) return (w - 440.0) / (490 - 440);
//        if (w >= 490.0 && w < 510.0) return 1.0;
//        if (w >= 510.0 && w < 580.0) return 1.0;
//        if (w >= 580.0 && w < 645.0) return (w - 645.0) / (645 - 580);
//        if (w >= 645.0 && w < 781.0) return 0.0;
//        throw new AssertionError();
//    }
//    double blue01(double w) {
//        if (w >= 379.0 && w < 440.0) return 1.0;
//        if (w >= 440.0 && w < 490.0) return 1.0;
//        if (w >= 490.0 && w < 510.0) return (int) ((w - 510.0) / (510.0 - 490.0));
//        if (w >= 510.0 && w < 580.0) return 0.0;
//        if (w >= 580.0 && w < 645.0) return 0.0;
//        if (w >= 645.0 && w <= 781.0) return 0.0;
//        throw new AssertionError();
//    }
//
////    void xxx(int wavelength) {
////        if w >= 380 and w < 440:
////            R = -(w - 440.) / (440. - 380.)
////            G = 0.0
////            B = 1.0
////        elif w >= 440 and w < 490:
////            R = 0.0
////            G = (w - 440.) / (490. - 440.)
////            B = 1.0
////        elif w >= 490 and w < 510:
////            R = 0.0
////            G = 1.0
////            B = -(w - 510.) / (510. - 490.)
////        elif w >= 510 and w < 580:
////            R = (w - 510.) / (580. - 510.)
////            G = 1.0
////            B = 0.0
////        elif w >= 580 and w < 645:
////            R = 1.0
////            G = -(w - 645.) / (645. - 580.)
////            B = 0.0
////        elif w >= 645 and w <= 780:
////            R = 1.0
////            G = 0.0
////            B = 0.0
////        else:
////            R = 0.0
////            G = 0.0
////            B = 0.0
////
////    }
}