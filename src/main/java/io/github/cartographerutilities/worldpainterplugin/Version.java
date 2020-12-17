package io.github.cartographerutilities.worldpainterplugin;

import java.io.IOException;
import java.util.Properties;

public class Version{
    public static final String VERSION;

    static{
        Properties props = new Properties();
        try{
            props.load(Version.class.getResourceAsStream("/io.github.cartographerutilities.worldpainter.version.properties"));
            VERSION = props.getProperty("version");
        }
        catch(IOException e){
            throw new RuntimeException("I/O error reading version number from classpath", e);
        }
    }
}