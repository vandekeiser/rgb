package fr.cla.rgb.drawing;

import java.awt.image.BufferedImage;

public class NamedImage {
    public final String name;
    public final BufferedImage image;

    protected NamedImage(BufferedImage image, String name) {
        this.image = image;
        this.name = name;
    }
}