package fr.cla.rgb.drawing;

public class Point {

    public final int x;
    public final int y;

    Point(int x, int y) {this.x = x; this.y = y;}

    public Point offset(int xoffset, int yoffset) {
        return new Point(x + xoffset, y + yoffset);
    }
}
