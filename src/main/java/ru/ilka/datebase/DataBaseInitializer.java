package ru.ilka.datebase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * DataBaseInitializer gets data base properties.
 * @since %G%
 * @version %I%
 */
class DataBaseInitializer {
    static  Logger logger = LogManager.getLogger(DataBaseInitializer.class);

    final String DRIVER;
    final String URL;
    final String LOGIN;
    final int POOL_SIZE;
    private int poolSize;
    final static int DEFAULT_POOL_SIZE = 3;
    final String PASSWORD;

    DataBaseInitializer() {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("properties.database");
            DRIVER = resourceBundle.getString("db.driver");
            URL = resourceBundle.getString("db.url");
            LOGIN = resourceBundle.getString("db.login");
            poolSize = Integer.valueOf(resourceBundle.getString("db.poolsize"));
            if(poolSize > 0){
                POOL_SIZE = poolSize;
            }else {
                logger.info("Pool size is less then 1, default size will be used.");
                POOL_SIZE = DEFAULT_POOL_SIZE;
                logger.info("Invalid pool size parameters, now pool size is " + POOL_SIZE);
            }
            PASSWORD = resourceBundle.getString("db.password");
        } catch (NumberFormatException | MissingResourceException  e){
            throw new RuntimeException("Cannot initialize the database connection. " + e);
        }
    }
}
