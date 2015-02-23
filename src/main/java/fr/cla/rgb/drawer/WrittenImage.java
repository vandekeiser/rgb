package fr.cla.rgb.drawer;

import java.awt.image.BufferedImage;
import fr.cla.rgb.drawing.NamedImage;

public class WrittenImage extends NamedImage {
    WrittenImage(BufferedImage image, String name) {
        super(image, name);
    }

    WrittenImage(NamedImage image) {
        super(image.image, image.name);
    }
}
