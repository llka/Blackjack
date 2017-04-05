package ru.ilka.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.exception.LogicException;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ConfigurationManager {
    static Logger logger = LogManager.getLogger(ConfigurationManager.class);

    private static final String CONFIGURATION_FILE_PATH = "properties.configuration";

    private static  ConfigurationManager configurationManager = new ConfigurationManager();
    private ResourceBundle resourceBundle;

    private ConfigurationManager(){
        try {
            resourceBundle = ResourceBundle.getBundle(CONFIGURATION_FILE_PATH);
        } catch (MissingResourceException e) {
            throw new RuntimeException("Can't load configuration property file. " + e);
        }
    }

    public static String getProperty(String key) {
        return configurationManager.resourceBundle.getString(key);
    }
}
