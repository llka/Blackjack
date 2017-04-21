package ru.ilka.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Message;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.MessageLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ManageMessagesCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(ManageMessagesCommand.class);

    private static final String PAGE_MESSAGES = "path.page.messages";
    private static final String ATTRIBUTE_RECEIVED = "accounts";
    private static final String ATTRIBUTE_SENT= "sent";
    private static final String READ_CHECKBOX = "read";
    private static final String DELETE_CHECKBOX = "delete";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_MESSAGES);
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        MessageLogic messageLogic = new MessageLogic();
        int accountId = account.getAccountId();
        try {
            ArrayList<Message> receivedMessages = messageLogic.findReceivedMessages(accountId);
            ArrayList<Message> sentMessages = messageLogic.findSentMessages(accountId);
            ArrayList<Message> toRemove = new ArrayList<>();
            for (Message received : receivedMessages) {
                int messageId = received.getMessageId();
                if(READ_CHECKBOX.equals(request.getParameter(READ_CHECKBOX + String.valueOf(messageId)))){
                    messageLogic.markMessageRead(messageId,true);
                    received.setReadMark(true);
                }else if(DELETE_CHECKBOX.equals(request.getParameter(DELETE_CHECKBOX + String.valueOf(messageId)))){
                    messageLogic.deleateMessage(messageId);
                    toRemove.add(received);
                }
            }
            if(!toRemove.isEmpty()){
                receivedMessages.removeAll(toRemove);
            }
            logger.debug("receivedMessages after remove" + receivedMessages);
            request.setAttribute(ATTRIBUTE_RECEIVED,receivedMessages);
            request.setAttribute(ATTRIBUTE_SENT,sentMessages);
        } catch (LogicException e) {
            throw new CommandException("Error while managing messages " + e);
        }
        return page;
    }
}
