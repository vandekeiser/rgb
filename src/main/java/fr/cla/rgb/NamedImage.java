package fr.cla.rgb;

import java.awt.image.BufferedImage;

public class NamedImage {
    public BufferedImage getImage() {
        return image;
    }

    private final BufferedImage image;

    public String getName() {
        return name;
    }

    private final String name;

    public NamedImage(BufferedImage image, String name) {
        this.image = image;
        this.name = name;
    }
}