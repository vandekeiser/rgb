package fr.cla.rgb;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import fr.cla.rgb.examples.AllBlue;
import fr.cla.rgb.examples.AllRed;
import static java.lang.System.out;

/**
 */
public class BigRgbDrawing_TryWithPngj {

    static final String IMG_TYPE = "png";
    private static String imageFileName() {
        return BigRgbDrawing_TryWithPngj.class.getSimpleName() + "." + IMG_TYPE;
    }

    public static void main(String[] args) throws IOException {
        //1. Render RED LEFT
        out.println("Rendering RED LEFT...");
        AllRed allRed = new AllRed();
        BufferedImage redLeft = allRed.render();

        //2. Write RED LEFT to disk
        String RED_LEFT_FILE = "RED_LEFT.png";
        out.println("Writing RED LEFT to file...");
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(RED_LEFT_FILE))) {
            ImageIO.write(redLeft, IMG_TYPE, out);
        }
        redLeft = null;

        //3. Render BLUE RIGHT
        out.println("Rendering BLUE RIGHT...");
        AllBlue allBlue = new AllBlue();
        BufferedImage blueRight = allBlue.render();

        //4. Write BLUE RIGHT to disk
        out.println("Writing BLUE RIGHT to file...");
        String BLUE_RIGHT_FILE = "BLUE_RIGHT.png";
        out.println("Writing BLUE RIGHT to file...");
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(BLUE_RIGHT_FILE))) {
            ImageIO.write(blueRight, IMG_TYPE, out);
        }
        blueRight = null;

        //5. Recolle les morceaux
        PngjSamples.doTiling(
                new String[] {RED_LEFT_FILE, BLUE_RIGHT_FILE},
                imageFileName(),
                2
        );
    }







}