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
    
    //TODO extract NumberedNamedImage
    public int number;
    public NamedImage(BufferedImage image, String name, int number) {
        this(image, name);
        number(number);
    }
    public void number(int number) {
        this.number = number;
    }
}