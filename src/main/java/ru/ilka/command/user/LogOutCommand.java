package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Visitor;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.ONLINE_USERS_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class LogOutCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(LogOutCommand.class);
    private static final String PAGE_START = "path.page.start";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);

        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        ServletContext servletContext = request.getServletContext();
        ConcurrentHashMap<Integer,Account> onlineUsers = (ConcurrentHashMap<Integer,Account>) servletContext.getAttribute(ONLINE_USERS_KEY);
        onlineUsers.remove(account.getAccountId());
        servletContext.setAttribute(ONLINE_USERS_KEY,onlineUsers);
        logger.debug("LogOut - " + account.getLogin() + " is offline now");
        logger.debug("Online users : " + onlineUsers);

        session.removeAttribute(ACCOUNT_KEY);

        visitor.setRole(Visitor.Role.GUEST);
        visitor.setCurrentPage(PAGE_START);
        session.setAttribute(VISITOR_KEY,visitor);
        //request.getSession().invalidate();

        return ConfigurationManager.getProperty(PAGE_START);
    }
}
