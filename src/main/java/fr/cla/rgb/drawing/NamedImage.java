package fr.cla.rgb.drawing;

import java.awt.image.BufferedImage;

public class NamedImage implements Named {
    public final String name;
    public final BufferedImage image;

    protected NamedImage(BufferedImage image, String name) {
        this.image = image;
        this.name = name;
    }

    @Override public String name() {
        return name;
    }
    
    //TODO extract NumberedNamedImage?
    private int number;
    public NamedImage(BufferedImage image, String name, int number) {
        this(image, name);
        number(number);
    }
    public void number(int number) {
        this.number = number;
    }
    public int number() {
        return this.number;
    }


    private int xsize, ysize;
    public void size(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
    }
    public int xsize() { return xsize; }
    public int ysize() { return ysize; }
}