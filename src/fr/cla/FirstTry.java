package fr.cla;

import java.io.IOException;

public class FirstTry extends RgbDrawing {

    public static void main(String[] args) throws IOException {
        new FirstTry().draw();
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int x, int y, int size) {
        return x + y;
    }
    @Override protected int g(int x, int y, int size) {
        return x - y;
    }
    @Override protected int b(int x, int y, int size) { return x * y; }

}