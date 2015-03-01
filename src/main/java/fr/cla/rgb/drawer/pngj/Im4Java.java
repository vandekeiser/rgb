package fr.cla.rgb.drawer.pngj;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.MontageCmd;
import org.im4java.process.ProcessStarter;

public class Im4Java {
    
    static {
        String myPath="C:\\Program Files\\ImageMagick-6.9.0-Q16";
        ProcessStarter.setGlobalSearchPath(myPath);
    }
    
    public static void doTiling(String[] imagesPaths, String wholeImageName) {
        IMOperation var1 = new IMOperation();
        String iImageDir = Paths.get(imagesPaths[0]).getParent().toString();
        var1.addImage(new String[]{iImageDir + "*.png"});
        var1.addImage(new String[]{wholeImageName});
        MontageCmd montage = new MontageCmd();
        try {
            montage.run(var1, new Object[0]);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            return;
        } catch (IM4JavaException e) {
            throw new RuntimeException(e);
        }
    }
    
}