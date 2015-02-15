package fr.cla;

import java.io.IOException;

public class ToutBleu extends DessinRgb {

    public static void main(String[] args) throws IOException {
        new ToutBleu().dessine();
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int g(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int b(int x, int y, int size) { return 0xff; }

}