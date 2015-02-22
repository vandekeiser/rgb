package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.BasicDrawer;
import fr.cla.rgb.drawing.WholeDrawing;

public class AllBlue extends WholeDrawing {

    public AllBlue(int size) {
        super(size);
    }

    public static void main(String[] args) throws IOException {
        BasicDrawer.INSTANCE.draw(new AllBlue(128));
    }

    @Override protected int R(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int G(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int B(int x, int y, int size) { return 0xff; }

}