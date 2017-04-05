package ru.ilka.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static ru.ilka.controller.ControllerConstants.DEFAULT_LOCALE;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class MessageManager {
    static Logger logger = LogManager.getLogger(MessageManager.class);

    private static final String MESSAGES_FILE_PATH = "properties.messages";
    private static MessageManager messageManager = new MessageManager();
    private ResourceBundle resourceBundle;
    private Locale currentLocale = DEFAULT_LOCALE;

    private MessageManager() {
        try {
            resourceBundle = ResourceBundle.getBundle(MESSAGES_FILE_PATH, DEFAULT_LOCALE);
        } catch (MissingResourceException e) {
            throw new RuntimeException("Can't load content property file." + e);
        }
    }

    public static String getProperty(String key, Locale locale) {
        if (locale != null) {
            if (!messageManager.currentLocale.equals(locale)) {
                messageManager.resourceBundle = ResourceBundle.getBundle(MESSAGES_FILE_PATH, locale);
                messageManager.currentLocale = locale;
            }
        } else {
            messageManager.resourceBundle = ResourceBundle.getBundle(MESSAGES_FILE_PATH, DEFAULT_LOCALE);
            messageManager.currentLocale = DEFAULT_LOCALE;
        }
        return messageManager.resourceBundle.getString(key);
    }
}
