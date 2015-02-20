package fr.cla.rgb.examples;

import fr.cla.rgb.Drawer;
import fr.cla.rgb.Drawing;

import java.io.IOException;

public class FirstTry extends Drawing {

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
        new Drawer().draw(new FirstTry(1024));
    }
}