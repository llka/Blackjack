package ru.ilka.command.admin;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SortByBanStatusCommand implements ActionCommand {

    private static final String PAGE_USERS = "path.page.users";
    private static final String ATTRIBUTE_ACCOUNTS = "accounts";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_USERS);

        HttpSession session = request.getSession();
        Account selfAccount = (Account) session.getAttribute(ACCOUNT_KEY);
        AccountLogic accountLogic = new AccountLogic();

        List<Account> accounts;
        try {
            accounts = accountLogic.getAllAccounts((int)selfAccount.getAccountId());
            accountLogic.sortByBanStatus(accounts);
        } catch (LogicException e) {
            throw new CommandException("Can't get all accounts " + e);
        }

        request.setAttribute(ATTRIBUTE_ACCOUNTS,accounts);
        return page;
    }
}
