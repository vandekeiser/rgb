package fr.cla.rgb.drawing;

import java.nio.file.Path;

public interface Named {
    String name();
    
    default String toPath(Path tempTilesPath) {
        return tempTilesPath.resolve(name()).toString();
    }
}
