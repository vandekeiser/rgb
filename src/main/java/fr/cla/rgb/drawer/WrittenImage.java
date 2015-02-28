package fr.cla.rgb.drawer;

import fr.cla.rgb.drawing.NamedImage;

public class WrittenImage extends NamedImage {
    WrittenImage(NamedImage image) {
        super(image.image, image.name, image.number);
    }

}
