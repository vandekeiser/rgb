package fr.cla.rgb;

import fr.cla.rgb.examples.AllBlue;
import fr.cla.rgb.examples.AllRed;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static fr.cla.rgb.Drawing.IMG_TYPE;
import static java.lang.System.out;

/**
 */
public class BigPngDrawing_TryWithPnjg2 {

    private final Drawing big;
    private final String imageFileName;

    public BigPngDrawing_TryWithPnjg2(Drawing big) {
        this.big = big;
        this.imageFileName = Drawer.imageFileName(this.big);
    }

    final void draw() throws IOException {
        //0. Prepare
        //0.1 Images
        AllRed allRed = new AllRed(); AllBlue allBlue = new AllBlue();
        BufferedImage redTile, blueTile;
        //0.2 Files
        String RED_TILE_FILE = "RED_TILE.png", BLUE_TILE_FILE = "BLUE_TILE.png";
        int tileNb = 0;
        String tileName;
        List<String> tiles = new ArrayList<>();

        //1.1. Render RED LEFT
        out.println("Rendering RED TILE...");
        redTile = allRed.render();

        //1.2. Write RED LEFT to disk
        tileNb++; tileName = RED_TILE_FILE + tileNb; tiles.add(tileName);
        out.printf("Writing RED LEFT to file %s ...%n", tileName);
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tileName))) {
            ImageIO.write(redTile, IMG_TYPE, out);
        }
        redTile = null;

        //2.1. Render BLUE RIGHT
        out.println("Rendering BLUE TILE...");
        blueTile = allBlue.render();

        //2.2. Write BLUE RIGHT to disk
        tileNb++; tileName = BLUE_TILE_FILE + tileNb; tiles.add(tileName);
        out.printf("Writing BLUE RIGHT to file %s ...%n", tileName);
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tileName))) {
            ImageIO.write(blueTile, IMG_TYPE, out);
        }
        blueTile = null;

        //3.1. Render BLUE RIGHT
        out.println("Rendering BLUE TILE...");
        blueTile = allBlue.render();

        //3.2. Write BLUE RIGHT to disk
        tileNb++; tileName = BLUE_TILE_FILE + tileNb; tiles.add(tileName);
        out.printf("Writing BLUE RIGHT to file %s ...%n", tileName);
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tileName))) {
            ImageIO.write(blueTile, IMG_TYPE, out);
        }
        blueTile = null;

        //4.1. Render RED LEFT
        out.println("Rendering RED TILE...");
        redTile = allRed.render();

        //4.2. Write RED LEFT to disk
        tileNb++; tileName = RED_TILE_FILE + tileNb; tiles.add(tileName);
        out.printf("Writing RED LEFT to file %s ...%n", tileName);
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tileName))) {
            ImageIO.write(redTile, IMG_TYPE, out);
        }
        redTile = null;

        //5. Recolle les morceaux
        PngjSamples.doTiling(
                tiles.toArray(new String[tiles.size()]),
                imageFileName,
                2
        );
    }

    public static void main(String[] args) throws IOException {
        new BigPngDrawing_TryWithPnjg2(new AllBlue()).draw();
    }
}