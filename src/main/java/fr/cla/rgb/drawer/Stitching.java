package fr.cla.rgb.drawer;

import fr.cla.rgb.drawer.pngj.*;

public interface Stitching {

    void stitch(String[] imagesPaths, String wholeImageName);

    public enum Stitchings implements Stitching {
        WITH_PNGJ {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ.doTiling(
                        imagesPaths,
                        wholeImageName,
                        1 //Image is only split into lines, so 1 image per row
                );
            }
        },
        WITH_PNGJ2 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ2.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ3 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ3.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ4 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ4.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ5 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ5.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ6 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ6.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ7 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ7.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ8 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ8.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PNGJ9 {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                int ntiles = 0; //need to add param
                String tile0 = null; //need to write tile0first

                PNGJ9.PngwImi1Imi2 info = PNGJ9.info2(tile0, ntiles, wholeImageName);
                PNGJ9.doTiling(
                        info, 
                        ntiles,
                        imagesPaths
                );
            }
        },
        WITH_PNGJ8A {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PNGJ8A.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_PngjImageio {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                PngjImageio.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        WITH_Im4Java {
            @Override public void stitch(String[] imagesPaths, String wholeImageName) {
                Im4Java.doTiling(
                        imagesPaths,
                        wholeImageName
                );
            }
        },
        ;
    }
    
}
