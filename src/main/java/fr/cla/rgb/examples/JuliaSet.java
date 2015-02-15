package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.PngDrawing;

public class JuliaSet extends PngDrawing {

    public static void main(String[] args) throws IOException {
        new JuliaSet().draw();
    }

    @Override protected int size() { return 16384; }   //16384: OOME
//    Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
//    	at java.awt.image.DataBufferInt.<init>(DataBufferInt.java:75)
//    	at java.awt.image.Raster.createPackedRaster(Raster.java:467)
//    	at java.awt.image.DirectColorModel.createCompatibleWritableRaster(DirectColorModel.java:1032)
//    	at java.awt.image.BufferedImage.<init>(BufferedImage.java:331)
//    	at fr.cla.rgb.RgbDrawing.draw(RgbDrawing.java:50)
//    	at fr.cla.rgb.examples.JuliaSet.main(JuliaSet.java:9)
//    	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//    	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
//    	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//    	at java.lang.reflect.Method.invoke(Method.java:483)
//    	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:134)

//    JAI
//    Libraries are not on Maven Central
//    Last stable release in 2006

    @Override protected int r(int i, int j, int size) {
        float x=D(i, size),y=D(j, size),X,Y,n=0;
        while(n++<200&&(X=x*x)+(Y=y*y)<4){x=X-Y+0.36237F;y=2*x*y+0.32F;}
        return (int)(Math.log(n)*256);
    }
    @Override protected int g(int i, int j, int size) {
        float x=D(i, size),y=D(j, size),X,Y,n=0;
        while(n++<200&&(x*x+y*y)<4){X=x;Y=y;x=X*X-Y*Y+-0.7F;y=2*X*Y+0.27015F;}
        return (int)(Math.log(n)*128);
    }
    @Override protected int b(int i, int j, int size) {
        float x=D(i, size),y=D(j, size),X,Y,n=0;
        while(n++<600&&(x*x+y*y)<4){X=x;Y=y;x=X*X-Y*Y+0.36237F;y=2*X*Y+0.32F;}
        return (int)(Math.log(n)*128);
    }

    float D(int x, int size) {
        return (x-size/2.0F)/(size/2.0F);
    }
}