package fr.cla;

import java.io.IOException;
import static java.lang.Math.sqrt;

public class ParabolicVisor extends RgbDrawing {

    public static void main(String[] args) throws IOException {
        new ParabolicVisor().draw();
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int i, int j, int size) {
        return (char)sqrt((double)(_sq(i-size/2)*_sq(j-size/2))*2.0);
    }
    @Override protected int g(int i, int j, int size) {
        return (char)sqrt((double)(
                (_sq(i-size/2)|_sq(j-size/2))*
                (_sq(i-size/2)&_sq(j-size/2))
        ));
    }
    @Override protected int b(int i, int j, int size) {
        return (char)sqrt((double)(_sq(i-size/2)&_sq(j-size/2))*2.0);
    }

    private int _sq(int i) {
        return i*i;
    }
}