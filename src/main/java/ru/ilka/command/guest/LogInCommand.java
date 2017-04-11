package ru.ilka.command.guest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.GameSettings;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.logic.SettingsLogic;
import ru.ilka.manager.ConfigurationManager;
import ru.ilka.manager.MessageManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static ru.ilka.controller.ControllerConstants.*;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class LogInCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(LogInCommand.class);

    private static final String PARAM_MAIL_OR_LOGIN = "emailOrLogin";
    private static final String PARAM_PASSWORD = "password";
    private static final String PAGE_MAIN = "path.page.main";
    private static final String PAGE_LOGIN = "path.page.login";
    private static final String MESSAGE_LOGIN_ERROR = "message.login.error";
    private static final String MESSAGE_BAN = "message.ban";
    private static final String ATTRIBUTE_ERROR_MESSAGE = "errorLoginPassMessage";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        String page = ConfigurationManager.getProperty(PAGE_LOGIN);
        String emailOrLogin = request.getParameter(PARAM_MAIL_OR_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        AccountLogic accountLogic = new AccountLogic();
        SettingsLogic settingsLogic = new SettingsLogic();
        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        try {
            if(accountLogic.checkLogIn(emailOrLogin, password)) {
                Account account = null;
                try {
                    account = accountLogic.getAccountByAuthorization(emailOrLogin);
                } catch (LogicException e) {
                    logger.error("Error in login command with account loading " + e);
                }
                if(account.isBan()){
                    logger.info("Banned: " + visitor);
                    page = ConfigurationManager.getProperty(PAGE_LOGIN);
                    request.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MessageManager.getProperty(MESSAGE_BAN,visitor.getLocale()));
                    return page;
                }
                page = ConfigurationManager.getProperty(PAGE_MAIN);
                if(account.isAdmin()){
                    visitor.setRole(Visitor.Role.ADMIN);
                }else {
                    visitor.setRole(Visitor.Role.USER);
                }
                visitor.setName(account.getLogin());
                visitor.setCurrentPage(page);
                visitor.setSessionLost(false);
                session.setAttribute(VISITOR_KEY,visitor);
                session.setAttribute(ACCOUNT_KEY,account);

                ConcurrentHashMap<Long,Account> onlineUsers = (ConcurrentHashMap<Long,Account>) servletContext.getAttribute(ONLINE_USERS_KEY);
                if(onlineUsers == null){
                    onlineUsers = new ConcurrentHashMap<>();
                }
                onlineUsers.put(account.getAccountId(),account);
                servletContext.setAttribute(ONLINE_USERS_KEY,onlineUsers);
                logger.debug("Online users : " + onlineUsers);
                GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
                if(settings == null) {
                    settings = settingsLogic.getSettings();
                    servletContext.setAttribute(SETTINGS_KEY, settings);
                }
            } else {
                visitor.setRole(Visitor.Role.GUEST);
                visitor.setCurrentPage(page);
                session.setAttribute(VISITOR_KEY,visitor);
                request.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MessageManager.getProperty(MESSAGE_LOGIN_ERROR,visitor.getLocale()));
            }
        }catch ( LogicException e) {
            logger.error("DB error in Login Check" + e);
        }
        return page;
    }
}
