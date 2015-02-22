package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.BasicDrawer;
import fr.cla.rgb.drawing.WholeDrawing;
import static java.lang.Math.sqrt;

public class ParabolicVisor extends WholeDrawing {

    public ParabolicVisor(int size) {
        super(size);
    }

    public static void main(String... args) throws IOException {
        BasicDrawer.INSTANCE.draw(new ParabolicVisor(1024));
    }

    @Override protected int R(int i, int j, int size) {
        return (char)sqrt((double)(U.sq(i - size / 2)* U.sq(j - size / 2))*2.0);
    }
    @Override protected int G(int i, int j, int size) {
        return (char)sqrt((double)(
                (U.sq(i - size / 2)| U.sq(j - size / 2))*
                (U.sq(i - size / 2)& U.sq(j - size / 2))
        ));
    }
    @Override protected int B(int i, int j, int size) {
        return (char)sqrt((double)(U.sq(i - size / 2)& U.sq(j - size / 2))*2.0);
    }

}