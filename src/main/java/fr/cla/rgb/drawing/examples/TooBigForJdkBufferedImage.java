package fr.cla.rgb.drawing.examples;

import java.awt.image.BufferedImage;

//-mx1G
public class TooBigForJdkBufferedImage {
    private static final int TOO_BIG = /*4096*/ /*8192*/ 16384;

    public static void main(String[] args) {
        new BufferedImage(TOO_BIG, TOO_BIG, BufferedImage.TYPE_INT_ARGB);
    }
}
