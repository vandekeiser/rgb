package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.BasicDrawer;
import fr.cla.rgb.SquareDrawing;

public class DiagonalSierpinsky extends SquareDrawing {

    public DiagonalSierpinsky(int size) {
        super(size);
    }

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new DiagonalSierpinsky(128));
    }

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