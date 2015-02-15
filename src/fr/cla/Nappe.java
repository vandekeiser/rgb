package fr.cla;

import java.io.IOException;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Nappe extends AbstractDessinRgb {

    public static void main(String[] args) throws IOException {
        new Nappe().dessine();
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int i, int j, int size) {
        double s=3./(j+99);
        double y=(j+sin((i*i+sqrt(j-700)*5)/100./size)*35)*s;
        return (
                ((int)((i+size)*s+y))%2
                +
                ((int)((size*2-i)*s+y))%2
        )*127;
    }
    @Override protected int g(int i, int j, int size) {
        double s=3./(j+99);
        double y=(j+sin((i*i+sqrt(j-700)*5)/100./size)*35)*s;
        return (
                ((int)(5*((i+size)*s+y)))%2
                +
                ((int)(5*((size*2-i)*s+y)))%2
        )*127;
    }
    @Override protected int b(int i, int j, int size) {
        double s=3./(j+99);
        double y=(j+sin((i*i+sqrt(j-700)*5)/100./size)*35)*s;
        return (
                ((int)(29*((i+size)*s+y)))%2
                +
                ((int)(29*((size*2-i)*s+y)))%2
        )*127;
    }

}