package fr.cla;

import java.io.IOException;
import static java.lang.Math.sin;

public class NappeOndulee extends DessinRgb {

    public static void main(String[] args) throws IOException {
        new NappeOndulee().dessine();
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int i, int j, int size) {
        float s=3.0F/(j+99);
        float y= (float) ((j+sin((i*i+_sq(j - 700)*5)/100.0F/size)*35)*s);
        return (
                ((int)((i+size)*s+y))%2
                +
                ((int)((size*2-i)*s+y))%2
        )*127;
    }

    @Override protected int g(int i, int j, int size) {
        float s=3.0F/(j+99);
        float y= (float) ((j+sin((i*i+_sq(j - 700)*5)/100./size)*35)*s);
        return (
                ((int)(5*((i+size)*s+y)))%2
                +
                ((int)(5*((size*2-i)*s+y)))%2
        )*127;
    }
    @Override protected int b(int i, int j, int size) {
        float s=3.0F/(j+99);
        float y= (float) ((j+sin((i*i+_sq(j - 700)*5)/100./size)*35)*s);
        return (
                ((int)(29*((i+size)*s+y)))%2
                +
                ((int)(29*((size*2-i)*s+y)))%2
        )*127;
    }

    private int _sq(int i) {
        return i*i;
    }
}