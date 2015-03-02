package fr.cla.rgb;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DrawExecutors {
    private static final ThreadFactory defaultThreadFactory = java.util.concurrent.Executors.defaultThreadFactory();
    
    public static final Executor ioExecutor = java.util.concurrent.Executors.newCachedThreadPool(r -> {
        Thread t = defaultThreadFactory.newThread(r);
        t.setDaemon(true);
        return t;
    });
    
    public static void init() {}
}
