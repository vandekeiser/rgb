package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.TilingDrawer;
import fr.cla.rgb.drawing.Point;
import fr.cla.rgb.drawing.WholeDrawing;

public class JuliaSet2 extends WholeDrawing {

    private final double cx, cy;
    private double x1 = -1.5, y1 = -1.5, x2 = 1.5, y2 = 1.5;
    private double dx, dy;

    public JuliaSet2(int size) {
        this(size,  - 1, 0);
    }
    
    public JuliaSet2(int size, double cx, double cy) {
        super(size);
        this.cx = cx;
        this.cy = cy;
        dx = (x2 - x1)*1D / size;
        dy = (y2 - y1)*1D / size;
    }

    public static void main(String[] args) throws IOException {
        //new SequentialTilingDrawer().draw(new JuliaSet(8192));
        //new SequentialTilingDrawer().draw(new JuliaSet(16384));
        //new SequentialTilingDrawer().draw(new JuliaSet(32768));
        TilingDrawer.SEQUENTIAL.draw(new JuliaSet2(8192)); //MAX_SIZE_BEFORE_SPLIT = 2048 --> OK
    }

    static int[] colors;
    static { // Static initializer for the colors[] array.
        colors = new int[64];
        for (int i = 0; i < colors.length; i++) {
            //colors[63 - i] = (i * 4 << 16) + (i * 4 << 8) + i * 4; // grayscale
            colors[63 - i] = (i*4) ^ ((i * 3)<<6) ^ ((i * 7)<<13); // crazy technicolor
        }
    }
    
    @Override protected int RGB(Point p, int wholeDrawingsize) {
        double x = x1 + dx * p.x, y = y1 + dy * p.y;
        double zx = x, zy = y;
        
        for (int i = 0; i < colors.length; i++) {
          // Compute z = z*z + c;
          double newx = zx * zx - zy * zy + cx;
          double newy = 2 * zx * zy + cy;
          zx = newx;
          zy = newy;
          // Check magnitude of z and return iteration number
          if (zx * zx + zy * zy > 4)
            return colors[i];
        }
        return colors[colors.length - 1];
    }
    
    @Override protected int R(int i, int j, int size) {
        throw new Error();
    }
    @Override protected int G(int i, int j, int size) {
        throw new Error();
    }
    @Override protected int B(int i, int j, int size) {
        throw new Error();
    }
}