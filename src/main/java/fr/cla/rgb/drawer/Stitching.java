package fr.cla.rgb.drawer;

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
        ;
    }
    
}
