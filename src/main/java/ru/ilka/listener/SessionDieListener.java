package ru.ilka.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Account;
import ru.ilka.entity.Visitor;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import java.util.concurrent.ConcurrentHashMap;
import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.ONLINE_USERS_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Shows when user logs out or is inactive too long.
 */
public class SessionDieListener implements HttpSessionListener {
    static Logger logger = LogManager.getLogger(SessionDieListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        visitor.setSessionLost(true);
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        ServletContext servletContext = event.getSession().getServletContext();
        ConcurrentHashMap<Integer,Account> onlineUsers = (ConcurrentHashMap<Integer,Account>) servletContext.getAttribute(ONLINE_USERS_KEY);
        onlineUsers.remove(account.getAccountId());
        servletContext.setAttribute(ONLINE_USERS_KEY,onlineUsers);

        logger.debug("User  " + account.getLogin() + " is offline now");
        logger.debug("Online users : " + onlineUsers);
    }
}
