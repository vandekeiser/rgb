package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.RgbDrawing;

public class DiagonalSierpinsky extends RgbDrawing {

    public static void main(String[] args) throws IOException {
        new DiagonalSierpinsky().draw();
    }

    @Override protected int size() { return 8192; }

    @Override protected int r(int i, int j, int size) {
        return (i!=0&&j!=0)?((i%j)&(j%i)):0;
    }
    @Override protected int g(int i, int j, int size) {
        return (i!=0&&j!=0)?((i%j)+(j%i)):0;
    }
    @Override protected int b(int i, int j, int size) {
        return (i!=0&&j!=0)?((i%j)|(j%i)):0;
    }

}