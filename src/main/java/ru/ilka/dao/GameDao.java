package ru.ilka.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.entity.Game;
import ru.ilka.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class GameDao {
    static Logger logger = LogManager.getLogger(GameDao.class);

    private static final String ADD_GAME = "INSERT INTO `games` (`bet`, `player_win`, `win_coefficient`, `time`, `is_dealer`, `users_account_id`) VALUES(?,?,?,?,?,?)";

    public GameDao() {
    }

    public void registerGame(int accountId, Game game) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_GAME)){
            preparedStatement.setDouble(1,game.getBet());
            preparedStatement.setBoolean(2,game.isPlayerWin());
            preparedStatement.setDouble(3,game.getWinCoefficient());
            preparedStatement.setString(4,game.getTime());
            preparedStatement.setBoolean(5,game.isPlayerIsDealer());
            preparedStatement.setInt(6,accountId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while inserting new game into database." + e);
        }
    }
}
