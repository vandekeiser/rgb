package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.AsyncDrawer;
import fr.cla.rgb.drawer.ParallelAsyncTilingDrawer;
import fr.cla.rgb.drawing.WholeDrawing;

public class JuliaSet extends WholeDrawing {

    public JuliaSet(int size) {
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
        
        AsyncDrawer.INSTANCE.draw(new JuliaSet(2048));


    }

    @Override protected int R(int i, int j, int size) {
        float x=D(i, size),y=D(j, size),X,Y,n=0;
        while(n++<200&&(X=x*x)+(Y=y*y)<4){x=X-Y+0.36237F;y=2*x*y+0.32F;}
        return (int)(Math.log(n)*256);
    }
    @Override protected int G(int i, int j, int size) {
        float x=D(i, size),y=D(j, size),X,Y,n=0;
        while(n++<200&&(x*x+y*y)<4){X=x;Y=y;x=X*X-Y*Y+-0.7F;y=2*X*Y+0.27015F;}
        return (int)(Math.log(n)*128);
    }
    @Override protected int B(int i, int j, int size) {
        float x=D(i, size),y=D(j, size),X,Y,n=0;
        while(n++<600&&(x*x+y*y)<4){X=x;Y=y;x=X*X-Y*Y+0.36237F;y=2*X*Y+0.32F;}
        return (int)(Math.log(n)*128);
    }

    float D(int x, int size) {
        return (x-size/2.0F)/(size/2.0F);
    }
}