package fr.cla.rgb.drawer;

import java.util.stream.Stream;

public interface Parallelism {
    
    <T> Stream<T> maybeParallel(Stream<T> in);
    
    public enum Parallelisms implements Parallelism {
        SINGLE_THREADED {
            @Override public <T> Stream<T> maybeParallel(Stream<T> in) {
                return in.sequential();
            }
        },
        PARALLEL {
            @Override public <T> Stream<T> maybeParallel(Stream<T> in) {
                return in.parallel();
            }
        },
        ;
    }
    
}
