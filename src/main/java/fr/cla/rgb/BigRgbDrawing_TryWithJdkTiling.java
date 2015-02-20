package fr.cla.rgb;

import fr.cla.rgb.examples.AllBlue;
import fr.cla.rgb.examples.AllRed;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import static java.lang.System.out;

/**
 * DOESN'T WORK
 */
public class BigRgbDrawing_TryWithJdkTiling {

    static final String IMG_TYPE = "png";
    private static String imageFileName() {
        return BigRgbDrawing_TryWithJdkTiling.class.getSimpleName() + "." + IMG_TYPE;
    }

    public static void main(String[] args) throws IOException {
        Iterator writers0 = ImageIO.getImageWritersByFormatName("png");
        while(writers0.hasNext()) {
            System.out.println("writers0.next(): " + writers0.next());
            //writers0.next(): com.sun.imageio.plugins.png.PNGImageWriter@28a418fc
            //et c'est tout, et il n'override pas canInsertImage=false
        }

        //0. Prepare image writer
        Iterator writers = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = (ImageWriter)writers.next();
        File f = new File(imageFileName());
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);


        //1. Render RED LEFT
        out.println("Rendering RED LEFT...");
        AllRed allRed = new AllRed();
        BufferedImage redLeft = allRed.render();

        //2. Write RED LEFT to disk
        out.println("Writing RED LEFT to file...");
        IIOImage iioRedLeft = new IIOImage(redLeft, null, null);
        writer.write(null, iioRedLeft, null);
        redLeft = null; iioRedLeft = null;

        //3. Render BLUE RIGHT
        out.println("Rendering BLUE RIGHT...");
        AllBlue allBlue = new AllBlue();
        BufferedImage blueRight = allBlue.render();

        //4. Write BLUE RIGHT to disk
        out.println("Writing BLUE RIGHT to file...");
        IIOImage iioBlueRight = new IIOImage(blueRight, null, null);
        if (writer.canInsertImage(-1)) {
            //ImageWriteParam.setTilingMode/setTiling
            ImageWriteParam iwp = new ImageWriteParam(Locale.getDefault());
            iwp.setTilingMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setTiling(
                    allBlue.size(),  //tileWidth
                    allBlue.size(),  //tileHeight
                    1,  //tileGridXOffset
                    0); //tileGridYOffset
            writer.writeInsert(-1, iioBlueRight, iwp);
        } else {
            throw new RuntimeException("Writer can't append a second image at: " + 1);
        }
        blueRight = null; iioBlueRight = null;
    }
}