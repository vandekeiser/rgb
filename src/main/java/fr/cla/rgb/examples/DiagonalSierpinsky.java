package fr.cla.rgb.examples;

import java.io.IOException;

import fr.cla.rgb.Drawer;
import fr.cla.rgb.Drawing;

public class DiagonalSierpinsky extends Drawing {

    public static void main(String[] args) throws IOException {
        new Drawer().draw(new DiagonalSierpinsky());
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