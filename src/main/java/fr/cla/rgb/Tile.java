package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static java.util.function.Function.identity;

public class Tile extends Drawing {

    private final Drawing whole;
    private final int xoffset, yoffset;

    public Tile(Drawing drawing, int size, int xoffset, int yoffset) {
        super(size);
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

    public Stream<Point> points() {
//        //DiagonalSierpinsky(8192): Rendering took: PT6.194S
//        //JuliaSet(8192): Rendering took: PT4M19.659S
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist

        //DiagonalSierpinsky(8192): Rendering took: PT8.551S
        //JuliaSet(8192): Rendering took: PT2M29.091S
        return IntStream.range(0, size()).mapToObj(
                x -> IntStream.range(0, size()).mapToObj(
                        y -> new Point(x, y)
                )
        ).flatMap(identity()); //Workaround: IntStream.flatMapToObj doesn't exist

        //OOME
//        return IntStream.range(0, size()).mapToObj(
//                x -> IntStream.range(0, size()).mapToObj(
//                        y -> new Point(x, y)
//                )
//        ).flatMap(identity()).collect(toList()).stream(); //Workaround: IntStream.flatMapToObj doesn't exist
    }

    public NamedImage renderTile() {
        int size = size();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            //__Should__ be threadsafe since we write to different pixels, so could use parallel()

        points().forEach(p -> {
            try {
                img.setRGB(p.x, p.y, rgb(p, size));
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println(String.format(
                    "Tile/renderTile/AIOOBE: p.x=%d, p.y=%d, size=%d",
                    p.x,
                    p.y,
                    size
                ));
                throw e;
            }
        });

        String name = whole.getClass().getSimpleName()+"_"+xoffset+"_"+yoffset+".png";

        return new NamedImage(img, name);
    }
}
