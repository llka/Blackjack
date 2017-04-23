package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.MessageLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class DeleteMessageCommand implements ActionCommand {

    private static final String PAGE_PROFILE = "path.page.profile";
    private static final int NEWEST_MESSAGE = 0;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_PROFILE);
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        MessageLogic messageLogic = new MessageLogic();
        int messageId = 0;
        try {
            messageId = messageLogic.findNewMessages(account.getAccountId()).get(NEWEST_MESSAGE).getMessageId();
            messageLogic.deleateMessage(messageId);
        } catch (LogicException e) {
            throw new CommandException("Can not delete new message - " + messageId + " " + e);

        }
        return page;
    }
}
