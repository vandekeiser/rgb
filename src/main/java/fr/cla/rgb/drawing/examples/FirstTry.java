package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.BasicDrawer;
import fr.cla.rgb.drawing.WholeDrawing;

public class FirstTry extends WholeDrawing {

    public FirstTry(int size) {
        super(size);
    }

    @Override protected int R(int x, int y, int size) {
        return x + y;
    }
    @Override protected int G(int x, int y, int size) {
        return x - y;
    }
    @Override protected int B(int x, int y, int size) { return x * y; }

    public static void main(String... args) throws IOException {
        BasicDrawer.INSTANCE.draw(new FirstTry(1024));
    }
}