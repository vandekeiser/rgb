package fr.cla.rgb.drawer;

import fr.cla.rgb.drawer.im4java.Im4Java;
import fr.cla.rgb.drawer.jdk.JdkImageIo;
import fr.cla.rgb.drawer.opencv.OpenCvTiling;
import fr.cla.rgb.drawer.pngj.*;

public interface Stitching {

    void stitch(String[] imagesPaths, String wholeImageName);

    public enum Stitchings implements Stitching {
        WITH_PNGJ {  //very slow for big sets (7h for 65536 JuliaSet3!)
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                Pngj.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ_PRECOMPUTE_EXAMPLE_TILE {   //doesn't work
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                int ntiles = 0; //need to add param
                String tile0 = null; //need to write tile0first

                PngjPrecomputeExampleTile.PngwImi1Imi2 info = PngjPrecomputeExampleTile.info2(tile0, ntiles, wholeImageName);
                PngjPrecomputeExampleTile.doTiling(
                        info,
                        ntiles,
                        imagesPaths
                );
            }
        },
        WITH_PNGJ_PNG_WRITER_PER_TILE {  //doesn't work
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PngjPngWriterPerTile.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_JDK_IMAGE_IO { //not implemented
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                JdkImageIo.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_IM_4_JAVA { //imagemagick doesn't work
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                Im4Java.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_OPEN_CV {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                OpenCvTiling.tile(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        ;
    }
    
}
