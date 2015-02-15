package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.RgbDrawing;

public class JuliaSet extends RgbDrawing {

    public static void main(String[] args) throws IOException {
        new JuliaSet().draw();
    }

    @Override protected int size() { return 8192; }

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