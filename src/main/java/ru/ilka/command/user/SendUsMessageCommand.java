package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.logic.MessageLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SendUsMessageCommand implements ActionCommand {

    static Logger logger = LogManager.getLogger(SendUsMessageCommand.class);

    private static final String ATTRIBUTE_PAGE_PATH = "path";
    private static final String PARAM_TEXT = "text";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String path = request.getSession().getAttribute(ATTRIBUTE_PAGE_PATH).toString();
        String page = ConfigurationManager.getProperty(path);
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        MessageLogic messageLogic = new MessageLogic();
        AccountLogic accountLogic = new AccountLogic();

        int accountId = account.getAccountId();
        String text = request.getParameter(PARAM_TEXT);

        List<Account> admins;
        try {
            admins = accountLogic.getAllAdmins(accountId);
        } catch (LogicException e) {
            throw new CommandException("Error while getting all admins " + e);
        }

        admins.forEach(admin -> {
            try {
                messageLogic.sendMessage(text, admin.getLogin(), accountId);
            } catch (LogicException e) {
                logger.error("Error while sending message to all admins " + e);
            }
        });

        return page;
    }
}
