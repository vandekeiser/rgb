package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.PngDrawing;

public class AllBlue extends PngDrawing {

    public static void main(String[] args) throws IOException {
        new AllBlue().draw();
    }

    @Override public int size() { return 128; }

    @Override protected int r(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int g(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int b(int x, int y, int size) { return 0xff; }

}