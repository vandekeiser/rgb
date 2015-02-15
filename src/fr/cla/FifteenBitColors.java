package fr.cla;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class FifteenBitColors {
    public static void main(String[] args) {
        BufferedImage img = new BufferedImage(256, 128, BufferedImage.TYPE_INT_RGB);

        // Generate algorithmically.
        for (int i = 0; i < 32768; i++) {
            int x = i & 255;
            int y = i / 256;
            int r = i << 3 & 0xF8;
            int g = i >> 2 & 0xF8;
            int b = i >> 7 & 0xF8;
            img.setRGB(x, y, (r << 8 | g) << 8 | b);
        }

        // Save.
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream("RGB15.png"))) {
            ImageIO.write(img, "png", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}