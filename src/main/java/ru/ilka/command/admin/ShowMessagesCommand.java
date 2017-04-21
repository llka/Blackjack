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
public class ShowMessagesCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(ShowMessagesCommand.class);

    private static final String PAGE_MESSAGES = "path.page.messages";
    private static final String ATTRIBUTE_RECEIVED = "received";
    private static final String ATTRIBUTE_SENT= "sent";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_MESSAGES);
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        MessageLogic messageLogic = new MessageLogic();
        int selfAccountId = account.getAccountId();

        try {
            ArrayList<Message> receivedMessages = messageLogic.findReceivedMessages(selfAccountId);
            ArrayList<Message> sentMessages = messageLogic.findSentMessages(selfAccountId);
            request.setAttribute(ATTRIBUTE_RECEIVED,receivedMessages);
            request.setAttribute(ATTRIBUTE_SENT,sentMessages);
        } catch (LogicException e) {
            throw new CommandException("Can not get all messages" + e);
        }

        return page;
    }
}
