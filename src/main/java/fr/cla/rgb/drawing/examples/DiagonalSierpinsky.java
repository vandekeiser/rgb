package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.BasicDrawer;
import fr.cla.rgb.drawing.WholeDrawing;

public class DiagonalSierpinsky extends WholeDrawing {

    public DiagonalSierpinsky(int size) {
        super(size);
    }

    public static void main(String[] args) throws IOException {
        BasicDrawer.INSTANCE.draw(new DiagonalSierpinsky(128));
    }

    @Override protected int R(int i, int j, int size) {
        return (i!=0&&j!=0)?((i%j)&(j%i)):0;
    }
    @Override protected int G(int i, int j, int size) {
        return (i!=0&&j!=0)?((i%j)+(j%i)):0;
    }
    @Override protected int B(int i, int j, int size) {
        return (i!=0&&j!=0)?((i%j)|(j%i)):0;
    }

}