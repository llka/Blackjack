package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Message;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.logic.MessageLogic;
import ru.ilka.manager.ConfigurationManager;
import ru.ilka.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SendMessageCommand implements ActionCommand {

    private static final String ATTRIBUTE_PAGE_PATH = "path";
    private static final String ATTRIBUTE_ERROR_LOGIN = "errorReceiverLogin";
    private static final String ATTRIBUTE_RECEIVED = "received";
    private static final String ATTRIBUTE_SENT= "sent";
    private static final String MESSAGE_LOGIN_ERROR = "message.error.unknown.login";
    private static final String PAGE_MESSAGES = "path.page.messages";
    private static final String PARAM_RECEIVER = "receiver";
    private static final String PARAM_TEXT = "text";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String path = request.getSession().getAttribute(ATTRIBUTE_PAGE_PATH).toString();
        String page = ConfigurationManager.getProperty(path);
        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        MessageLogic messageLogic = new MessageLogic();
        AccountLogic accountLogic = new AccountLogic();

        String receiverLogin = request.getParameter(PARAM_RECEIVER);
        String text = request.getParameter(PARAM_TEXT);

        try {
            if(accountLogic.checkLoginExistence(receiverLogin)) {
                messageLogic.sendMessage(text, receiverLogin, account.getAccountId());
            }else {
                request.setAttribute(ATTRIBUTE_ERROR_LOGIN, MessageManager.getProperty(MESSAGE_LOGIN_ERROR,visitor.getLocale()));
            }
        } catch (LogicException e) {
           throw new CommandException("Error while sending new message" + e);
        }

        if(PAGE_MESSAGES.equals(path)){
            int selfAccountId = account.getAccountId();
            try {
                ArrayList<Message> receivedMessages = messageLogic.findReceivedMessages(selfAccountId);
                ArrayList<Message> sentMessages = messageLogic.findSentMessages(selfAccountId);
                request.setAttribute(ATTRIBUTE_RECEIVED,receivedMessages);
                request.setAttribute(ATTRIBUTE_SENT,sentMessages);
            } catch (LogicException e) {
                throw new CommandException("Can not get all messages" + e);
            }
        }
        return page;
    }
}
