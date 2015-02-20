package fr.cla.rgb.examples;

import java.io.IOException;

import fr.cla.rgb.Drawer;
import fr.cla.rgb.Drawing;
import fr.cla.rgb.U;
import static java.lang.Math.sqrt;

public class ParabolicVisor extends Drawing {

    public static void main(String[] args) throws IOException {
        new Drawer().draw(new ParabolicVisor());
    }

    @Override protected int size() { return 1024; }

    @Override protected int r(int i, int j, int size) {
        return (char)sqrt((double)(U.sq(i - size / 2)* U.sq(j - size / 2))*2.0);
    }
    @Override protected int g(int i, int j, int size) {
        return (char)sqrt((double)(
                (U.sq(i - size / 2)| U.sq(j - size / 2))*
                (U.sq(i - size / 2)& U.sq(j - size / 2))
        ));
    }
    @Override protected int b(int i, int j, int size) {
        return (char)sqrt((double)(U.sq(i - size / 2)& U.sq(j - size / 2))*2.0);
    }

}