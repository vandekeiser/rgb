package fr.cla.rgb.examples;

import fr.cla.rgb.BasicDrawer;
import fr.cla.rgb.Drawing;

import java.io.IOException;

public class DiagonalSierpinsky extends Drawing {

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new DiagonalSierpinsky());
    }

    @Override protected int size() { return 1024; }

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