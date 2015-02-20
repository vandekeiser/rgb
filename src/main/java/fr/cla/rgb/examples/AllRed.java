package fr.cla.rgb.examples;

import fr.cla.rgb.BasicDrawer;
import fr.cla.rgb.Drawing;

import java.io.IOException;

public class AllRed extends Drawing {

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new AllRed());
    }

    @Override protected int size() { return 128; }

    @Override protected int r(int x, int y, int size) {
        return 0xff;
    }
    @Override protected int g(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int b(int x, int y, int size) { return 0x00; }

}