package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.logic.LogicResult;
import ru.ilka.manager.ConfigurationManager;
import ru.ilka.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;


/**
 * Here could be your advertisement +375 29 3880490
 */
public class AccountInfoCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(AccountInfoCommand.class);

    private static final String PAGE_PROFILE = "path.page.profile";
    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";

    private static final String MESSAGE_SUCCESS_CHANGE = "message.profile.success.change";
    private static final String MESSAGE_SUCCESS_PASSWORD_CHANGE = "message.profile.success.password";
    private static final String MESSAGE_INVALID = "message.profile.invalid";
    private static final String MESSAGE_INVALID_PASSWORD = "message.profile.invalid.password";
    private static final String MESSAGE_SAME_PASSWORD = "message.profile.same.password";
    private static final String MESSAGE_UNQNIQUE = "message.profile.ununique";
    private static final String ATTRIBUTE_SUCCESS_NAME_MESSAGE = "successNameChangeMessage";
    private static final String ATTRIBUTE_SUCCESS_LAST_NAME_MESSAGE = "successLastNameChangeMessage";
    private static final String ATTRIBUTE_SUCCESS_EMAIL_MESSAGE = "successEmailChangeMessage";
    private static final String ATTRIBUTE_SUCCESS_LOGIN_MESSAGE = "successLoginChangeMessage";
    private static final String ATTRIBUTE_SUCCESS_PASSWORD_MESSAGE = "successPasswordChangeMessage";
    private static final String ATTRIBUTE_ERROR_PASSWORD_MESSAGE = "errorPasswordChangeMessage";
    private static final String ATTRIBUTE_ERROR_LOGIN_MESSAGE = "errorLoginChangeMessage";
    private static final String ATTRIBUTE_ERROR_EMAIL_MESSAGE = "errorEmailChangeMessage";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_PROFILE);
        String firstName = request.getParameter(PARAM_FIRST_NAME);
        String lastName = request.getParameter(PARAM_LAST_NAME);
        String email = request.getParameter(PARAM_EMAIL);
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        AccountLogic accountLogic = new AccountLogic();
        LogicResult logicResult = LogicResult.FAILED;
        if(!account.getFirstName().equals(firstName)){
            try {
                accountLogic.changeFirstName((int)account.getAccountId(),firstName);
            } catch (LogicException e) {
                logger.error("Can't change first name " + e);
            }
            account.setFirstName(firstName);
            request.setAttribute(ATTRIBUTE_SUCCESS_NAME_MESSAGE, MessageManager.getProperty(MESSAGE_SUCCESS_CHANGE,visitor.getLocale()));
        }
        if(!account.getLastName().equals(lastName)){
            try {
                accountLogic.changeLastName((int)account.getAccountId(),lastName);
            } catch (LogicException e) {
                logger.error("Can't change last name " + e);
            }
            account.setLastName(lastName);
            request.setAttribute(ATTRIBUTE_SUCCESS_LAST_NAME_MESSAGE, MessageManager.getProperty(MESSAGE_SUCCESS_CHANGE,visitor.getLocale()));
        }
        if(!account.getLogin().equals(login)){
            try {
                logicResult = accountLogic.changeLogin((int)account.getAccountId(),login);
            } catch (LogicException e) {
                logger.error("Can't change login " + e);
            }
            switch (logicResult){
                case LOGIN_UNUNIQUE:
                    request.setAttribute(ATTRIBUTE_ERROR_LOGIN_MESSAGE, MessageManager.getProperty(MESSAGE_UNQNIQUE,visitor.getLocale()) + " " + login + " is already used.");
                    break;
                case INVALID_LOGIN:
                    request.setAttribute(ATTRIBUTE_ERROR_LOGIN_MESSAGE, MessageManager.getProperty(MESSAGE_INVALID,visitor.getLocale()));
                    break;
                case OK:
                    request.setAttribute(ATTRIBUTE_SUCCESS_LOGIN_MESSAGE, MessageManager.getProperty(MESSAGE_SUCCESS_CHANGE,visitor.getLocale()));
                    account.setLogin(login);
                    visitor.setName(login);
                    break;
                default:
                    throw new CommandException("Unknown Logic Result achieved while login changing");
            }
        }
        if(!account.getEmail().equals(email)){
            try {
                logicResult = accountLogic.changeEmail((int)account.getAccountId(),email);
            } catch (LogicException e) {
                logger.error("Can't change e-mail " + e);
            }
            switch (logicResult){
                case EMAIL_UNUNIQUE:
                    request.setAttribute(ATTRIBUTE_ERROR_EMAIL_MESSAGE, MessageManager.getProperty(MESSAGE_UNQNIQUE,visitor.getLocale()) + " " + email + " is already used.");
                    break;
                case INVALID_EMAIL:
                    request.setAttribute(ATTRIBUTE_ERROR_EMAIL_MESSAGE, MessageManager.getProperty(MESSAGE_INVALID,visitor.getLocale()));
                    break;
                case OK:
                    request.setAttribute(ATTRIBUTE_SUCCESS_EMAIL_MESSAGE, MessageManager.getProperty(MESSAGE_SUCCESS_CHANGE,visitor.getLocale()));
                    account.setEmail(email);
                    break;
                default:
                    throw new CommandException("Unknown Logic Result achieved while changing email");
            }
        }
        if(password != null) {
            if (!account.getPassword().equals(password) && password != null) {
                try {
                    logicResult = accountLogic.changePassword((int) account.getAccountId(), password);
                } catch (LogicException e) {
                    logger.error("Can't change password " + e);
                }
                switch (logicResult) {
                    case INVALID_PASSWORD:
                        request.setAttribute(ATTRIBUTE_ERROR_PASSWORD_MESSAGE, MessageManager.getProperty(MESSAGE_INVALID_PASSWORD, visitor.getLocale()));
                        break;
                    case OK:
                        request.setAttribute(ATTRIBUTE_SUCCESS_PASSWORD_MESSAGE, MessageManager.getProperty(MESSAGE_SUCCESS_PASSWORD_CHANGE, visitor.getLocale()));
                        account.setPassword(password);
                        break;
                    default:
                        throw new CommandException("Unknown Logic Result achieved while changing password");
                }
            } else {
                request.setAttribute(ATTRIBUTE_ERROR_PASSWORD_MESSAGE, MessageManager.getProperty(MESSAGE_SAME_PASSWORD, visitor.getLocale()));
            }
        }
        session.setAttribute(ACCOUNT_KEY,account);
        return page;
    }
}
