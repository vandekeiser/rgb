package fr.cla.rgb.examples;

import java.io.IOException;
import fr.cla.rgb.BasicDrawer;
import fr.cla.rgb.SquareDrawing;

public class AllBlue extends SquareDrawing {

    public AllBlue(int size) {
        super(size);
    }

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new AllBlue(128));
    }

    @Override protected int R(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int G(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int B(int x, int y, int size) { return 0xff; }

}