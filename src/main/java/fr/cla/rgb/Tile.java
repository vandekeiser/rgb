package fr.cla.rgb;

import java.awt.image.BufferedImage;

public class Tile extends Drawing {

    private final SquareDrawing whole;
    private final int xoffset, yoffset;

    public Tile(SquareDrawing drawing, int xsize, int ysize, int xoffset, int yoffset) {
        super(xsize, ysize);
        this.whole = drawing;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    @Override protected int r(int x, int y, int size) {
        return whole.r(x+xoffset, y+yoffset, size);
    }

    @Override protected int g(int x, int y, int size) {
        return whole.g(x+xoffset, y+yoffset, size);
    }

    @Override protected int b(int x, int y, int size) {
        return whole.b(x+xoffset, y+yoffset, size);
    }

    public NamedImage renderTile() {
        int wholeSize = whole.size();
        BufferedImage img = new BufferedImage(wholeSize, wholeSize, BufferedImage.TYPE_INT_RGB);

            //__Should__ be threadsafe since we write to different pixels, so could use parallel()

        points().forEach(p -> {
            try {
                img.setRGB(p.x, p.y, rgb(p, wholeSize));
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println(String.format(
                    "Tile/renderTile/AIOOBE: p.x=%d, p.y=%d, xsize=%d, ysize=%d",
                    p.x,
                    p.y,
                    xsize(),
                    ysize()
                ));
                throw e;
            }
        });

        String name = whole.getClass().getSimpleName()+"_"+xoffset+"_"+yoffset+".png";

        return new NamedImage(img, name);
    }
}
