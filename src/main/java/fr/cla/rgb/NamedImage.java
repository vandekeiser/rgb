package fr.cla.rgb;

import java.awt.image.BufferedImage;

class NamedImage {
    final String name;
    final BufferedImage image;

    NamedImage(BufferedImage image, String name) {
        this.image = image;
        this.name = name;
    }
}