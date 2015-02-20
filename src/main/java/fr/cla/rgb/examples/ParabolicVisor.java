package fr.cla.rgb.examples;

import fr.cla.rgb.BasicDrawer;
import fr.cla.rgb.Drawing;
import fr.cla.rgb.U;

import java.io.IOException;

import static java.lang.Math.sqrt;

public class ParabolicVisor extends Drawing {

    public ParabolicVisor(int size) {
        super(size);
    }

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new ParabolicVisor(1024));
    }

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