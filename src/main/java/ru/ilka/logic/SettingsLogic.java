package ru.ilka.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.dao.SettingsDao;
import ru.ilka.entity.Settings;
import ru.ilka.exception.DBException;
import ru.ilka.exception.LogicException;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SettingsLogic {
    static Logger logger = LogManager.getLogger(SettingsLogic.class);

    public SettingsLogic(){

    }

    public void changeBetLimits(int min, int max) throws LogicException {
        SettingsDao settingsDao = new SettingsDao();
        try {
            settingsDao.updateBetLimits(min,max);
        } catch (DBException e) {
            throw new LogicException("Error while changing settings - bet limits " + e);
        }
    }

    public Settings getSettings() throws LogicException {
        SettingsDao settingsDao = new SettingsDao();
        try {
            Settings settings = settingsDao.getBetLimits();
            return settings;
        } catch (DBException e) {
            throw new LogicException("Error while getting settings - bet limits " + e);
        }
    }
}
