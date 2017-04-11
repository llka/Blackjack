package ru.ilka.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.entity.GameSettings;
import ru.ilka.exception.DBException;

import java.sql.*;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SettingsDao {
    static Logger logger = LogManager.getLogger(SettingsDao.class);

    private static final String UPDATE_BET_LIMITS = "UPDATE `settings` SET `min_bet` = ? , `max_bet` = ? WHERE `settings_id` = 1";
    private static final String FIND_BET_LIMITS = "SELECT `min_bet`, `max_bet` FROM `settings` WHERE `settings_id` = 1";

    private static final String COLUMN_SETTINGS_ID = "account_id";
    private static final String COLUMN_MIN_BET = "min_bet";
    private static final String COLUMN_MAX_BET = "max_bet";


    public SettingsDao(){
    }

    public void updateBetLimits(int min, int max) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BET_LIMITS)) {
            preparedStatement.setInt(1,min);
            preparedStatement.setInt(2,max);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating bet limits in  in database." + e);
        }
    }

    public GameSettings getBetLimits() throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BET_LIMITS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            GameSettings settings = new GameSettings();
            if(resultSet.next()){
                settings.setMinBet(resultSet.getInt(COLUMN_MIN_BET));
                settings.setMaxBet(resultSet.getInt(COLUMN_MAX_BET));
                return settings;
            }
            else {
                settings.setMinBet(1);
                settings.setMaxBet(1);
                return settings;
            }
        } catch (SQLException e) {
            throw new DBException("Error while getting bet limits in from database." + e);
        }
    }

}
