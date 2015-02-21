package fr.cla.rgb.trytiling;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import fr.cla.rgb.PngjSamples;
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
        AllRed allRed = new AllRed(128);
        BufferedImage redLeft = allRed.render().getImage();

        //2. Write RED LEFT to disk
        String RED_LEFT_FILE = "RED_LEFT.png";
        out.println("Writing RED LEFT to file...");
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(RED_LEFT_FILE))) {
            ImageIO.write(redLeft, IMG_TYPE, out);
        }
        redLeft = null;

        //3. Render BLUE RIGHT
        out.println("Rendering BLUE RIGHT...");
        AllBlue allBlue = new AllBlue(128);
        BufferedImage blueRight = allBlue.render().getImage();

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
                new String[]{RED_LEFT_FILE, BLUE_RIGHT_FILE},
                imageFileName(),
                2
        );
    }







}