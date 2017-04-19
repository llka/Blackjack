package ru.ilka.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.dao.SettingsDao;
import ru.ilka.entity.GameSettings;
import ru.ilka.exception.DBException;
import ru.ilka.exception.LogicException;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SettingsLogic {
    static Logger logger = LogManager.getLogger(SettingsLogic.class);

    private static final int DEFAULT_MIN_BET_LIMIT = 1;
    private static final int DEFAULT_MAX_BET_LIMIT = 100;
    private static final int DEFAULT_DECKS = 6;

    public SettingsLogic(){

    }

    public void changeSettings(int min, int max, int decks) throws LogicException {
        SettingsDao settingsDao = new SettingsDao();
        try {
            settingsDao.updateSettings(min,max,decks);
        } catch (DBException e) {
            throw new LogicException("Error while changing settings - bet limits " + e);
        }
    }

    public GameSettings getSettings(){
        SettingsDao settingsDao = new SettingsDao();
        GameSettings settings;
        try {
            settings = settingsDao.getSettingsParams();
        } catch (DBException e) {
            logger.info("Default settings params will be used");
            settings = new GameSettings(DEFAULT_MIN_BET_LIMIT,DEFAULT_MAX_BET_LIMIT,DEFAULT_DECKS);
        }
        return settings;
    }
}
