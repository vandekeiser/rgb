package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.PngDrawing;

public class FirstTry extends PngDrawing {

    private final int size;
    public FirstTry(int size) { this.size = size; }
    @Override protected int size() { return size; }

    @Override protected int r(int x, int y, int size) {
        return x + y;
    }
    @Override protected int g(int x, int y, int size) {
        return x - y;
    }
    @Override protected int b(int x, int y, int size) { return x * y; }

    public static void main(String[] args) throws IOException {
        new FirstTry(1024).draw();
    }
}