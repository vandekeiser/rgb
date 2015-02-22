package fr.cla.rgb.drawing.examples;

import java.io.IOException;
import fr.cla.rgb.drawer.ParallelBlockingTilingDrawer;
import fr.cla.rgb.drawing.Point;
import fr.cla.rgb.drawing.WholeDrawing;

public class JuliaSet2 extends WholeDrawing {

    private final double cx, cy; //starting complex
    private final double x1 = -1.5, y1 = -1.5, x2 = 1.5, y2 = 1.5; //complex plane bounds
    private final double dx, dy; //step
    
    private final int iterations; //iteratons for each point
    private static int maxIterations; //iteratons for each point
    private final int[] colors;  //color scale for how fast iterations diverge

    public JuliaSet2(int size) {
        //this(size,  -1, 0);
        //this(size,  -1.3, 0.3);
        this(size,  0.03515, -0.7467);
    }
    
    public JuliaSet2(int size, double cx, double cy) {
        super(size);
        this.cx = cx;
        this.cy = cy;
        dx = (x2 - x1)*1D / size;
        dy = (y2 - y1)*1D / size;
        
        iterations = 1024;
        //iterations = 91;
        colors = new int[iterations];
        for (int i = 0; i < colors.length; i++) {
            colorScale(i, colors);
        }
//        colors = IntStream.range(0, iterations)
//                .mapToDouble(i->(double) i / (double) iterations)
//                .mapToObj(d->Color.getHSBColor((float) d, 0.85f, 1.0f))
//                .mapToInt(color->color.getRGB())
//                .toArray();
    }

    private void colorScale(int i, int[] colors) {
        //colors[63 - i] = (i * 4 << 16) + (i * 4 << 8) + i * 4; // grayscale
        colors[iterations -1 - i] = (i*4) ^ ((i * 3)<<6) ^ ((i * 7)<<13); // crazy technicolor
        
        //(R<< 8 | G) << 8 | B
//        int R = (int)(Math.log(i)*256);
//        int G = (int)(Math.log(i)*128);
//        int B = (int)(Math.log(i)*128);
//        colors[iterations -1 - i] = (R<< 8 | G) << 8 | B;
    }

    public static void main(String[] args) throws IOException {
        //new SequentialTilingDrawer().draw(new JuliaSet(8192));
        //new SequentialTilingDrawer().draw(new JuliaSet(16384));
        //new SequentialTilingDrawer().draw(new JuliaSet(32768));
        ParallelBlockingTilingDrawer.INSTANCE.draw(new JuliaSet2(4096));
        System.out.println("maxIterations: " + maxIterations);
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
          if (zx * zx + zy * zy > 4) {
              maxIterations = Math.max(maxIterations, i);
              return colors[i];
          }

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