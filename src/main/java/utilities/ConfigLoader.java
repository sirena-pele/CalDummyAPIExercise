package utilities;


import java.io.File;
import java.util.Properties;

public class ConfigLoader {

    public Properties getProperties() {
        return properties;
    }

    private final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader() {
        PropertyUtils propertyUtils = new PropertyUtils();
        Log.info("SEARCHING FOR PROPERTIES FILE IN: "+System.getProperty("user.dir") + File.separator + "configs/config.properties");
        properties = propertyUtils.propertyLoader(System.getProperty("user.dir") + File.separator + "configs/config.properties");
    }

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

}
