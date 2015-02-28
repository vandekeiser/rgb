package fr.cla.rgb.drawing.examples;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public enum U {
    ;
    private static final int[] ZERO = {0, 0, 0};

    public static double sq (double x) {
        return pow(x, 2.0);
    }

    public static int sq (int x) {
        //Not using Math.pow, since that returns a double which might have unintended consequences
        // what with all the mixed types computations..
        return x*x;
    }
    
    static private double Gamma = 0.80;
    static private double IntensityMax = 255;

    /** Taken from Earl F. Glynn's web page:
    * <a href="http://www.efg2.com/Lab/ScienceAndEngineering/Spectra.htm">Spectra Lab Report</a>
    * */
    public static int[] waveLengthToRGB(double wavelength){
        if(Double.isNaN(wavelength)) return ZERO;
        
        double factor;
        double Red,Green,Blue;

        if((wavelength >= 379) && (wavelength<440)){
            Red = -(wavelength - 440) / (440 - 380);
            Green = 0.0;
            Blue = 1.0;
        }else if((wavelength >= 440) && (wavelength<490)){
            Red = 0.0;
            Green = (wavelength - 440) / (490 - 440);
            Blue = 1.0;
        }else if((wavelength >= 490) && (wavelength<510)){
            Red = 0.0;
            Green = 1.0;
            Blue = -(wavelength - 510) / (510 - 490);
        }else if((wavelength >= 510) && (wavelength<580)){
            Red = (wavelength - 510) / (580 - 510);
            Green = 1.0;
            Blue = 0.0;
        }else if((wavelength >= 580) && (wavelength<645)){
            Red = 1.0;
            Green = -(wavelength - 645) / (645 - 580);
            Blue = 0.0;
        }else if((wavelength >= 645) && (wavelength<781)){
            Red = 1.0;
            Green = 0.0;
            Blue = 0.0;
        }else{
            throw new AssertionError();
        };

        // Let the intensity fall off near the vision limits

        if((wavelength >= 380) && (wavelength<420)){
            factor = 0.3 + 0.7*(wavelength - 380) / (420 - 380);
        }else if((wavelength >= 420) && (wavelength<701)){
            factor = 1.0;
        }else if((wavelength >= 701) && (wavelength<781)){
            factor = 0.3 + 0.7*(780 - wavelength) / (780 - 700);
        }else{
            factor = 0.0;
        };


        int[] rgb = new int[3];
        // Don't want 0^x = 1 for x <> 0
        rgb[0] = Red==0.0 ? 0 : (int) round(IntensityMax * pow(Red * factor, Gamma));
        rgb[1] = Green==0.0 ? 0 : (int) round(IntensityMax * pow(Green * factor, Gamma));
        rgb[2] = Blue==0.0 ? 0 : (int) round(IntensityMax * pow(Blue * factor, Gamma));
        return rgb;
    }
}
