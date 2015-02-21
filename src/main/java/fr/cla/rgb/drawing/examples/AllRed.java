package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.BasicDrawer;
import fr.cla.rgb.drawing.WholeDrawing;

public class AllRed extends WholeDrawing {

    public AllRed(int size) {
        super(size);
    }

    public static void main(String[] args) throws IOException {
        new BasicDrawer().draw(new AllRed(128));
    }

    @Override protected int R(int x, int y, int size) {
        return 0xff;
    }
    @Override protected int G(int x, int y, int size) {
        return 0x00;
    }
    @Override protected int B(int x, int y, int size) { return 0x00; }

}