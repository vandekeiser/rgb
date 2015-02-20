package fr.cla.rgb.examples;

import fr.cla.rgb.Drawer;
import fr.cla.rgb.Drawing;

import java.io.IOException;

public class AllBlue extends Drawing {

    public static void main(String[] args) throws IOException {
        new Drawer().draw(new AllBlue());
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