package ru.ilka.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class BanUsersCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(BanUsersCommand.class);

    private static final String PAGE_USERS = "path.page.users";
    private static final String ATTRIBUTE_ACCOUNTS = "accounts";
    private static final String BAN_CHECKBOX = "ban";
    private static final String ADMIN_CHECKBOX = "admin";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_USERS);
        HttpSession session = request.getSession();
        Account selfAccount = (Account) session.getAttribute(ACCOUNT_KEY);
        AccountLogic accountLogic = new AccountLogic();
        try {
            List<Account> accounts = accountLogic.getAllAccounts(selfAccount.getAccountId());
            for (Account account : accounts) {
                if(BAN_CHECKBOX.equals(request.getParameter(String.valueOf(account.getAccountId()))) && !account.isBan()){
                    accountLogic.banAccount(account.getAccountId(),true);
                    account.setBan(true);
                }else if(!BAN_CHECKBOX.equals(request.getParameter(String.valueOf(account.getAccountId()))) && account.isBan()){
                    accountLogic.banAccount(account.getAccountId(),false);
                    account.setBan(false);
                }

                if(ADMIN_CHECKBOX.equals(request.getParameter(ADMIN_CHECKBOX + String.valueOf(account.getAccountId())))
                        && !account.isAdmin()){
                    accountLogic.setAdminRole(account.getAccountId(),true);
                    account.setAdmin(true);
                }else if (!ADMIN_CHECKBOX.equals(request.getParameter(ADMIN_CHECKBOX + String.valueOf(account.getAccountId())))
                        && account.isAdmin()){
                    accountLogic.setAdminRole(account.getAccountId(),false);
                    account.setAdmin(false);
                }
            }
            request.setAttribute(ATTRIBUTE_ACCOUNTS,accounts);
        } catch (LogicException e) {
           throw new CommandException("Can not get all accounts " + e);
        }
        return page;
    }
}
