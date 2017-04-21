package ru.ilka.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.entity.Message;
import ru.ilka.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class MessageDao {
    static Logger logger = LogManager.getLogger(Message.class);

    private static final String LOAD_RECEIVED = "SELECT DISTINCT `message_id`, `text`, `time`, `sender_id`, `receiver_id`, `read_mark` FROM `users` JOIN `messages` ON `receiver_id` = ? GROUP BY `time` DESC";
    private static final String LOAD_SENT = "SELECT DISTINCT `message_id`, `text`, `time`, `sender_id`, `receiver_id`, `read_mark` FROM `users` JOIN `messages` ON `sender_id` = ? GROUP BY `time` DESC";
    private static final String UPDATE_READ_MARK = "UPDATE `messages` SET `read_mark`= ? WHERE `message_id`= ?";
    private static final String DELETE_MESSAGE = "DELETE FROM `messages` WHERE `message_id`= ?";
    private static final String FIND_RECEIVER_LOGIN = "SELECT `login` FROM `users` INNER JOIN `messages` ON `receiver_id` = `account_id` WHERE `message_id` = ?";
    private static final String FIND_SENDER_LOGIN = "SELECT `login` FROM `users` INNER JOIN `messages` ON `sender_id` = `account_id` WHERE `message_id` = ?";
    private static final String ADD_MESSAGE = "INSERT INTO `messages` (`text`, `time`, `sender_id`, `receiver_id`) VALUES (?, ?, ?, ?)";

    private static final String COLUMN_MESSAGE_ID = "message_id";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_SENDER_ID = "sender_id";
    private static final String COLUMN_RECEIVER_ID = "receiver_id";
    private static final String COLUMN_READ_MARK = "read_mark";
    private static final String COLUMN_LOGIN = "login";

    public MessageDao(){

    }

    public ArrayList<Message> loadReceivedMessages(int accountId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(LOAD_RECEIVED)){
            preparedStatement.setInt(1,accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Message> received = new ArrayList<>();
            while (resultSet.next()){
                Message message = new Message();
                message.setMessageId(resultSet.getInt(COLUMN_MESSAGE_ID));
                message.setText(resultSet.getString(COLUMN_TEXT));
                message.setSendTime(resultSet.getString(COLUMN_TIME));
                message.setSenderId(resultSet.getInt(COLUMN_SENDER_ID));
                message.setReceiverId(resultSet.getInt(COLUMN_RECEIVER_ID));
                message.setReadMark(resultSet.getBoolean(COLUMN_READ_MARK));
                received.add(message);
            }
            return received;
        } catch (SQLException e) {
            throw new DBException("Error in received messages loading " + e);
        }
    }

    public ArrayList<Message> loadSentMessages(int accountId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(LOAD_SENT)){
            preparedStatement.setInt(1,accountId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Message> sent = new ArrayList<>();
            while (resultSet.next()){
                Message message = new Message();
                message.setMessageId(resultSet.getInt(COLUMN_MESSAGE_ID));
                message.setText(resultSet.getString(COLUMN_TEXT));
                message.setSendTime(resultSet.getString(COLUMN_TIME));
                message.setSenderId(resultSet.getInt(COLUMN_SENDER_ID));
                message.setReceiverId(resultSet.getInt(COLUMN_RECEIVER_ID));
                message.setReadMark(resultSet.getBoolean(COLUMN_READ_MARK));
                sent.add(message);
            }
            return sent;
        } catch (SQLException e) {
            throw new DBException("Error in sent messages loading " + e);
        }
    }

    public void updateReadMark(int messageId, boolean isRead) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_READ_MARK)){
            preparedStatement.setInt(2,messageId);
            preparedStatement.setBoolean(1,isRead);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while updating read mark in message " + messageId + " " + e);
        }
    }

    public void deleteMessage(int messageId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_MESSAGE)){
            preparedStatement.setInt(1,messageId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while deleating message " + messageId + " " + e);
        }
    }

    public String loadReceiverLogin(int messageId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_RECEIVER_LOGIN)){
            preparedStatement.setInt(1,messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(COLUMN_LOGIN);
            }else {
                throw new DBException("No such receiver login in message " + messageId);
            }
        } catch (SQLException e) {
            throw new DBException("Error while finding receiver login in message " + messageId + " " + e);
        }
    }

    public String loadSenderLogin(int messageId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_SENDER_LOGIN)){
            preparedStatement.setInt(1,messageId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(COLUMN_LOGIN);
            }else {
                throw new DBException("No such sender login in message " + messageId);
            }
        } catch (SQLException e) {
            throw new DBException("Error while finding sender login in message " + messageId + " " + e);
        }
    }

    public void addNewMessage(String text, String time, int accountId, int receiverId) throws DBException {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_MESSAGE)){
            preparedStatement.setString(1,text);
            preparedStatement.setString(2,time);
            preparedStatement.setInt(3,accountId);
            preparedStatement.setInt(4,receiverId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DBException("Error while inserting new message " + e);
        }
    }
}
