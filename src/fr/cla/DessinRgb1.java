package fr.cla;

import java.io.IOException;

public class DessinRgb1 extends AbstractDessinRgb {

    public static void main(String[] args) throws IOException {
        new DessinRgb1().dessine();
    }

    @Override protected int xsize() { return 256; }
    @Override protected int ysize() { return 128; }

    @Override protected int red(AbstractDessinRgb.Point p) {
        return p.x + p.y;
    }
    @Override protected int green(AbstractDessinRgb.Point p) {
        return p.x - p.y;
    }
    @Override protected int blue(AbstractDessinRgb.Point p) {
        return p.x * p.y;
    }

}