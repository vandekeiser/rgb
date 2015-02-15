package fr.cla;

public enum U {
    ;

    static int sq (int x) {
        return x*x; //not using Math.pow, since that returns a double which might have unintended consequences
    }
}
