package fr.cla.rgb.drawing.examples;

public enum U {
    ;

    public static double sq (double x) {
        return Math.pow(x, 2.0);
    }

    public static int sq (int x) {
        //Not using Math.pow, since that returns a double which might have unintended consequences
        // what with all the mixed types computations..
        return x*x;
    }
}
