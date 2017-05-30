package ru.ilka.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.entity.GameSettings;
import ru.ilka.exception.DBException;

import java.sql.*;

/**
 * SettingsDao class is responsible for sending requests to the database and processing received answers.
 * @since %G%
 * @version %I%
 */
public class SettingsDao {
    static Logger logger = LogManager.getLogger(SettingsDao.class);

    private static final String UPDATE_BET_LIMITS = "UPDATE `settings` SET `min_bet` = ? , `max_bet` = ?, `decks` = ? WHERE `settings_id` = 1";
    private static final String FIND_BET_LIMITS = "SELECT `min_bet`, `max_bet` FROM `settings` WHERE `settings_id` = 1";
    private static final String FIND_NUMBER_OF_DECKS = "SELECT `decks` FROM `settings` WHERE `settings_id` = 1";
    private static final String FIND_SETTINGS = "SELECT `min_bet`, `max_bet`, `decks` FROM `settings` WHERE `settings_id` = 1";

    private static final String COLUMN_SETTINGS_ID = "settings_id";
    private static final String COLUMN_MIN_BET = "min_bet";
    private static final String COLUMN_MAX_BET = "max_bet";
    private static final String COLUMN_DECKS = "decks";

    private static final int DEFAULT_BET_LIMIT = 1;
    private static final int DEFAULT_DECKS = 6;


    public SettingsDao(){
    }

    public void updateSettings(int min, int max, int decks) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BET_LIMITS)) {
            preparedStatement.setInt(1,min);
            preparedStatement.setInt(2,max);
            preparedStatement.setInt(3,decks);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating bet limits in  in database." + e);
        }
    }

    public GameSettings getSettingsParams() throws DBException{
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_SETTINGS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            GameSettings settings = new GameSettings();
            if(resultSet.next()){
                settings.setMinBet(resultSet.getInt(COLUMN_MIN_BET));
                settings.setMaxBet(resultSet.getInt(COLUMN_MAX_BET));
                settings.setNumberOfDecks(resultSet.getInt(COLUMN_DECKS));
                return settings;
            }
            else {
                settings.setMinBet(DEFAULT_BET_LIMIT);
                settings.setMaxBet(DEFAULT_BET_LIMIT);
                settings.setNumberOfDecks(DEFAULT_DECKS);
                return settings;
            }
        } catch (SQLException e) {
            throw new DBException("Error while getting settings in from database." + e);
        }
    }

}
