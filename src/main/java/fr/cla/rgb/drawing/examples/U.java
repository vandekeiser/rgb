package fr.cla.rgb.drawing.examples;

import java.math.BigDecimal;
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
        if((wavelength >= 379.0) && (wavelength<440.0)){
            Red = -(wavelength - 440.0) / (440.0 - 380.0);
            Green = 0.0;
            Blue = 1.0;
        }else if((wavelength >= 440.0) && (wavelength<490.0)){
            Red = 0.0;
            Green = (wavelength - 440.0) / (490.0 - 440.0);
            Blue = 1.0;
        }else if((wavelength >= 490.0) && (wavelength<510.0)){
            Red = 0.0;
            Green = 1.0;
            Blue = -(wavelength - 510.0) / (510.0 - 490.0);
        }else if((wavelength >= 510.0) && (wavelength<580.0)){
            Red = (wavelength - 510.0) / (580.0 - 510.0);
            Green = 1.0;
            Blue = 0.0;
        }else if((wavelength >= 580.0) && (wavelength<645.0)){
            Red = 1.0;
            Green = -(wavelength - 645.0) / (645.0 - 580.0);
            Blue = 0.0;
        }else if((wavelength >= 645.0) && (wavelength<=780.1)){
            Red = 1.0;
            Green = 0.0;
            Blue = 0.0;
        }else{
            throw new AssertionError("bad wavelength: " + wavelength);
        };

        // Let the intensity fall off near the vision limits
        if((wavelength >= 380.0) && (wavelength<420.0)){
            factor = 0.3 + 0.7*(wavelength - 380.0) / (420.0 - 380.0);
        }else if((wavelength >= 420.0) && (wavelength<701.0)){
            factor = 1.0;
        }else if((wavelength >= 701.0) && (wavelength<781.0)){
            factor = 0.3 + 0.7*(780.0 - wavelength) / (780.0 - 700.0);
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

    public static int[] waveLengthToRGB(BigDecimal lambda) {
        return waveLengthToRGB(lambda.doubleValue());
    }
}
