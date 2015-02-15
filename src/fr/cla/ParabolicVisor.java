package fr.cla;

import java.io.IOException;
import static fr.cla.U.sq;
import static java.lang.Math.sqrt;

public class ParabolicVisor extends RgbDrawing {

    public static void main(String[] args) throws IOException {
        new ParabolicVisor().draw();
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int i, int j, int size) {
        return (char)sqrt((double)(sq(i-size/2)*sq(j-size/2))*2.0);
    }
    @Override protected int g(int i, int j, int size) {
        return (char)sqrt((double)(
                (sq(i - size / 2)|sq(j-size/2))*
                (sq(i-size/2)&sq(j-size/2))
        ));
    }
    @Override protected int b(int i, int j, int size) {
        return (char)sqrt((double)(sq(i-size/2)&sq(j-size/2))*2.0);
    }

}