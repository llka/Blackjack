package ru.ilka.logic;

import ru.ilka.dao.MessageDao;
import ru.ilka.entity.Deal;
import ru.ilka.entity.Message;
import ru.ilka.exception.DBException;
import ru.ilka.exception.LogicException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * MessageLogic class provides functionality for users communication
 * @see Message
 * @since %G%
 * @version %I%
 */
public class MessageLogic {

    private static final String DATE_TIME_REGEX = "yyyy-MM-dd HH:mm:ss";

    public MessageLogic(){

    }

    /**
     * Finds all messages that this user receive.
     * @param accountId user's account id
     * @return list of messages
     * @throws LogicException if DBException occurred.
     */
    public ArrayList<Message> findReceivedMessages(int accountId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadReceivedMessages(accountId);
        } catch (DBException e) {
            throw new LogicException("Can not get received messages" + e);
        }
    }

    /**
     * Finds all messages that this user has already sent.
     * @param accountId user's account id
     * @return list of messages
     * @throws LogicException if DBException occurred.
     */
    public ArrayList<Message> findSentMessages(int accountId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadSentMessages(accountId);
        } catch (DBException e) {
            throw new LogicException("Can not get sent messages" + e);
        }
    }

    /**
     * Finds all messages that this user has not already read or marked.
     * @param accountId user's account id
     * @return list of messages
     * @throws LogicException if DBException occurred.
     */
    public ArrayList<Message> findNewMessages(int accountId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadNewMessages(accountId);
        } catch (DBException e) {
            throw new LogicException("Can not get new messages" + e);
        }
    }

    /**
     * Sets new message already read status
     * @param messageId message id
     * @param isRead true if message is already read, false otherwise
     * @throws LogicException if DBException occurred.
     */
    public void markMessageRead(int messageId, boolean isRead) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            messageDao.updateReadMark(messageId, isRead);
        } catch (DBException e) {
            throw new LogicException("Can not mark messages as already read" + e);
        }
    }

    /**
     * Removes message from data base.
     * @param messageId message id
     * @throws LogicException if DBException occurred.
     */
    public void deleteMessage(int messageId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            messageDao.deleteMessage(messageId);
        } catch (DBException e) {
            throw new LogicException("Can not delete message" + e);
        }
    }

    /**
     * Finds receiver login.
     * @param messageId message id
     * @return receiver login
     * @throws LogicException if DBException occurred.
     */
    public String findReceiverLogin(int messageId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadReceiverLogin(messageId);
        } catch (DBException e) {
            throw new LogicException("Can not find receiver login" + e);
        }
    }

    /**
     * Finds sender login.
     * @param messageId message id
     * @return sender login
     * @throws LogicException if DBException occurred.
     */
    public String findSenderLogin(int messageId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadSenderLogin(messageId);
        } catch (DBException e) {
            throw new LogicException("Can not find sender login" + e);
        }
    }

    /**
     * Sends message and registers it in data base.
     * @param text message text
     * @param receiverLogin receiver login
     * @param accountId sender's account id
     * @throws LogicException if there is no receiver with such login or DBException occurred.
     */
    public void sendMessage(String text, String receiverLogin, int accountId) throws LogicException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_REGEX);
        String currentTime = now.format(format);

        AccountLogic accountLogic = new AccountLogic();
        int receiverId;
        try {
            receiverId = accountLogic.findIdByLogin(receiverLogin);
        } catch (LogicException e) {
            throw new LogicException("Error while finding receiverId" + e);
        }

        MessageDao messageDao = new MessageDao();
        try {
            messageDao.addNewMessage(text,currentTime,accountId,receiverId);
        } catch (DBException e) {
            throw new LogicException("Error while sending new message" + e);
        }
    }
}
