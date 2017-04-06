package ru.ilka.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Account;
import ru.ilka.entity.Visitor;

import javax.servlet.ServletContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.ONLINE_USERS_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class SessionDieListener implements HttpSessionListener {
    static Logger logger = LogManager.getLogger(SessionDieListener.class);

    private static final String PAGE_LOGIN = "path.page.login";

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
        ConcurrentHashMap<Long,Account> onlineUsers = (ConcurrentHashMap<Long,Account>) servletContext.getAttribute(ONLINE_USERS_KEY);
        onlineUsers.remove(account.getAccountId());
        servletContext.setAttribute(ONLINE_USERS_KEY,onlineUsers);
        logger.debug("SessionDieListener " + account.getLogin() + " is offline now");
        logger.debug("Online users : " + onlineUsers);
    }
}
