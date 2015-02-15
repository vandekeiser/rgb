package fr.cla;

import java.io.IOException;

public class DessinRgb1 extends DessinRgb {

    public static void main(String[] args) throws IOException {
        new DessinRgb1().dessine();
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