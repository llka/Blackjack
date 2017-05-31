package ru.ilka.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.entity.Game;
import ru.ilka.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * GameDao class is responsible for sending requests to the database and processing received answers.
 * @since %G%
 * @version %I%
 */
public class GameDao {
    static Logger logger = LogManager.getLogger(GameDao.class);

    private static final String ADD_GAME = "INSERT INTO `games` (`bet`, `player_win`, `win_coefficient`, `time`, `is_dealer`, `users_account_id`) VALUES(?,?,?,?,?,?)";
    private static final String LOAD_ALL_GAMES = "SELECT `bet`, `win_coefficient`, `time`, `users_account_id` FROM `games` WHERE `users_account_id` = ? ORDER BY `time` DESC";

    private static final String COLUMN_BET = "bet";
    private static final String COLUMN_WIN_COEFF = "win_coefficient";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_USER_ID = "users_account_id";

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

    public ArrayList<Game> loadGamesHistory(int accountId) throws DBException{
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(LOAD_ALL_GAMES)){
            preparedStatement.setInt(1,accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Game> games = new ArrayList<>();
            while (resultSet.next()){
                Game game = new Game();
                game.setPlayerAccountId(accountId);
                game.setWinCoefficient(resultSet.getDouble(COLUMN_WIN_COEFF));
                String time = resultSet.getString(COLUMN_TIME);
                game.setTime(time.substring(0,time.length()-2));
                game.setBet(resultSet.getInt(COLUMN_BET));
                games.add(game);
            }
            return games;
        } catch (SQLException e) {
            throw new DBException("Error while loading games history." + e);
        }
    }
}
