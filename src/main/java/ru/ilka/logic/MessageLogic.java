package ru.ilka.logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.dao.MessageDao;
import ru.ilka.entity.Message;
import ru.ilka.exception.DBException;
import ru.ilka.exception.LogicException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * Here could be your advertisement +375 29 3880490
 */
public class MessageLogic {

    private static final String DATE_TIME_REGEX = "yyyy-MM-dd HH:mm:ss";

    public MessageLogic(){

    }

    public ArrayList<Message> findReceivedMessages(int accountId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadReceivedMessages(accountId);
        } catch (DBException e) {
            throw new LogicException("Can not get received messages" + e);
        }
    }

    public ArrayList<Message> findSentMessages(int accountId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadSentMessages(accountId);
        } catch (DBException e) {
            throw new LogicException("Can not get sent messages" + e);
        }
    }

    public ArrayList<Message> findNewMessages(int accountId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadNewMessages(accountId);
        } catch (DBException e) {
            throw new LogicException("Can not get new messages" + e);
        }
    }

    public void markMessageRead(int messageId, boolean isRead) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            messageDao.updateReadMark(messageId, isRead);
        } catch (DBException e) {
            throw new LogicException("Can not mark messages as already read" + e);
        }
    }

    public void deleateMessage(int messageId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            messageDao.deleteMessage(messageId);
        } catch (DBException e) {
            throw new LogicException("Can not delete message" + e);
        }
    }

    public String findReceiverLogin(int messageId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadReceiverLogin(messageId);
        } catch (DBException e) {
            throw new LogicException("Can not find receiver login" + e);
        }
    }

    public String findSenderLogin(int messageId) throws LogicException {
        MessageDao messageDao = new MessageDao();
        try {
            return messageDao.loadSenderLogin(messageId);
        } catch (DBException e) {
            throw new LogicException("Can not find sender login" + e);
        }
    }

    public void sendMessage(String text, String receiverLogin, int accountId) throws LogicException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_REGEX);
        String currentTime = now.format(format);

        AccountLogic accountLogic = new AccountLogic();
        int receiverId;
        try {
            receiverId = accountLogic.getIdByLogin(receiverLogin);
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
