package fr.cla;

public enum U {
    ;

    static double sq (double x) {
        return Math.pow(x, 2.0); //not using Math.pow, since that returns a double which might have unintended consequences
    }

    static int sq (int x) {
        return x*x; //not using Math.pow, since that returns a double which might have unintended consequences
    }
}
