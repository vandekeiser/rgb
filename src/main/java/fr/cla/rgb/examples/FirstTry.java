package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.BasicDrawer;
import fr.cla.rgb.SquareDrawing;

public class FirstTry extends SquareDrawing {

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

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new FirstTry(1024));
    }
}