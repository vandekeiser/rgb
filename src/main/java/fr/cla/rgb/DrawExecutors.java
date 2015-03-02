package fr.cla.rgb;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DrawExecutors {
    public static final Executor ioExecutor = java.util.concurrent.Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });
    
    public static void init() {}
}
