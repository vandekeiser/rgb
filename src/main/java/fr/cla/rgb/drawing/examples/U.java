package fr.cla.rgb.drawing.examples;

import java.awt.*;
import java.math.BigDecimal;
import static java.lang.Math.pow;
import static java.lang.Math.round;

public enum U {
    ;
    private static final int[] ZERO = {0, 0, 0};
    public static final double PURPLE_WAVELENGTH = 380.0, RED_WAVELENGTH = 780.0;

    public static double sq (double x) {
        return pow(x, 2.0);
    }

    public static int sq (int x) {
        //Not using Math.pow, since that returns a double which might have unintended consequences
        // what with all the mixed types computations..
        return x*x;
    }

    public static int waveLengthToRGB2(double wavelength){
//        The <code>saturation</code> and <code>brightness</code> components
//       should be floating-point values between zero and one
//       (numbers in the range 0.0-1.0).  The <code>hue</code> component
//       can be any floating-point number.  The floor of this number is
//       subtracted from it to create a fraction between 0 and 1.  This
//       fractional number is then multiplied by 360 to produce the hue
//       angle in the HSB color model.
        float maxHue = 280F, maxHueFactor = maxHue/360F; //avoid periodicity if < 1
        float hue =(float)(maxHueFactor*(1.0-((wavelength-PURPLE_WAVELENGTH)/(RED_WAVELENGTH-PURPLE_WAVELENGTH)))),
              saturation = 1.0F,
              //brightness = 1.0F;
              brightness = (float)intensityFactor(wavelength);
        return Color.HSBtoRGB(hue, saturation, brightness);
    }
    
    static private double Gamma = 0.80;
    static private double IntensityMax = 255;
    /** Taken from Earl F. Glynn's web page:
    * <a href="http://www.efg2.com/Lab/ScienceAndEngineering/Spectra.htm">Spectra Lab Report</a>
    * */
//AUTRE POSSIBILITE UTILISER HSV?
    public static int[] waveLengthToRGB(double wavelength){
        if(Double.isNaN(wavelength)) {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!GOT NaN!!!!!!!!!!!!!!!!!!!!!");
            return ZERO;
        } 

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

        double factor = intensityFactor(wavelength);

        int[] rgb = new int[3];
        // Don't want 0^x = 1 for x <> 0
        rgb[0] = Red==0.0 ? 0 : (int) round(IntensityMax * pow(Red * factor, Gamma));
        rgb[1] = Green==0.0 ? 0 : (int) round(IntensityMax * pow(Green * factor, Gamma));
        rgb[2] = Blue==0.0 ? 0 : (int) round(IntensityMax * pow(Blue * factor, Gamma));
        return rgb;
    }

    private static double intensityFactor(double wavelength) {
        double see = /*0.3*/0.9, maybeSee = 1.0 - see;
        // Let the intensity fall off near the vision limits
        if((wavelength >= PURPLE_WAVELENGTH) && (wavelength<420.0)){
            return see + maybeSee*(wavelength - PURPLE_WAVELENGTH) / (420.0 - PURPLE_WAVELENGTH);
        }else if((wavelength >= 420.0) && (wavelength<701.0)){
            return 1.0;
        }else if((wavelength >= 701.0) && (wavelength<=RED_WAVELENGTH)){
            return see + maybeSee*(RED_WAVELENGTH - wavelength) / (RED_WAVELENGTH - 700.0);
        }else{
            return 0.0;
        }
    }

    public static int[] waveLengthToRGB(BigDecimal lambda) {
        return waveLengthToRGB(lambda.doubleValue());
    }
}
