package ru.ilka.command.guest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.GameSettings;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.logic.LogicResult;
import ru.ilka.logic.SettingsLogic;
import ru.ilka.manager.ConfigurationManager;
import ru.ilka.manager.MessageManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import static ru.ilka.controller.ControllerConstants.*;
import static ru.ilka.controller.ControllerConstants.ONLINE_USERS_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class RegisterCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(RegisterCommand.class);

    private static final String PARAM_FIRST_NAME = "firstName";
    private static final String PARAM_LAST_NAME = "lastName";
    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_BIRTH_DATE = "birthDate";
    private static final String PARAM_GENDER = "gender";
    private static final String PARAM_AVATAR = "avatar";
    private static final String DEFAULT_AVATAR = "default_avatar.jpg";
    private static final String PARAM_INVITE_CODE = "inviteCode";
    private static final String PAGE_MAIN = "path.page.main";
    private static final String PAGE_REGISTER = "path.page.register";
    private static final String FILE_DELIMITER = "\\";
    private static final String MESSAGE_REGISTER_FAILED = "message.register.failed";
    private static final String MESSAGE_LOGIN_NOT_UNIQUE = "message.register.login.ununique";
    private static final String MESSAGE_EMAIL_NOT_UNIQUE = "message.register.email.ununique";
    private static final String MESSAGE_INVITE_INVALID = "message.register.invite.invalid";
    private static final String ATTRIBUTE_ERROR_MESSAGE = "errorRegisterMessage";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_REGISTER);
        String firstName = request.getParameter(PARAM_FIRST_NAME);
        String lastName = request.getParameter(PARAM_LAST_NAME);
        String email = request.getParameter(PARAM_EMAIL);
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        String birthDate = request.getParameter(PARAM_BIRTH_DATE);
        String gender = request.getParameter(PARAM_GENDER);
        String inviteCode = request.getParameter(PARAM_INVITE_CODE);
        String avatar;

        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        AccountLogic accountLogic = new AccountLogic();
        SettingsLogic settingsLogic = new SettingsLogic();
        LogicResult registerResult = null;

        Part img = null;
        try {
            img = request.getPart(PARAM_AVATAR);
        } catch (IOException | ServletException e) {
            logger.error("Can't get img from request " + e);
            avatar = DEFAULT_AVATAR;
        }

        if(img.getSize() > 1) {
            logger.debug("img.getSize() = " + img.getSize());

            String uploadFilePath = AVATAR_DIRECTORY_STATIC;

            // creates the save directory if it does not exists
            File fileSaveDir = new File(uploadFilePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }
            String fileName = Paths.get(img.getSubmittedFileName()).getFileName().toString();
            try {
                //Get all the parts from request and write it to the file on server
                for (Part part : request.getParts()) {
                    part.write(uploadFilePath + FILE_DELIMITER + fileName);
                }
            }catch (IOException | ServletException e){
                throw new CommandException("Error while writing file parts " + e);
            }
            avatar = fileName;
        } else {
            avatar = DEFAULT_AVATAR;
        }

        try {
            registerResult = accountLogic.register(firstName,lastName,email,login,password,birthDate,gender,avatar,inviteCode);
        } catch (LogicException e) {
            logger.error("Error while registering an account " + e);
        }
        switch (registerResult){
            case EMAIL_UNUNIQUE:
                request.setAttribute(ATTRIBUTE_ERROR_MESSAGE,email + " " + MessageManager.getProperty(MESSAGE_EMAIL_NOT_UNIQUE,visitor.getLocale()));
                break;
            case LOGIN_UNUNIQUE:
                request.setAttribute(ATTRIBUTE_ERROR_MESSAGE,"\"" + login + "\" " +  MessageManager.getProperty(MESSAGE_LOGIN_NOT_UNIQUE,visitor.getLocale()));
                break;
            case FAILED:
                request.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MessageManager.getProperty(MESSAGE_REGISTER_FAILED,visitor.getLocale()));
                break;
            case REGISTER_INVITE_WRONG:
                request.setAttribute(ATTRIBUTE_ERROR_MESSAGE, MessageManager.getProperty(MESSAGE_INVITE_INVALID,visitor.getLocale()));
                break;
            case OK:
                page = ConfigurationManager.getProperty(PAGE_MAIN);
                Account account;
                try {
                    account = accountLogic.getAccountByLogin(login);
                } catch (LogicException e) {
                    throw new CommandException("Error in register command with account loading " + e);
                }
                visitor.setRole(Visitor.Role.USER);
                visitor.setCurrentPage(PAGE_MAIN);
                visitor.setName(account.getLogin());
                visitor.setSessionLost(false);
                session.setAttribute(VISITOR_KEY,visitor);
                session.setAttribute(ACCOUNT_KEY,account);

                ConcurrentHashMap<Integer,Account> onlineUsers = (ConcurrentHashMap<Integer,Account>) servletContext.getAttribute(ONLINE_USERS_KEY);
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
                break;
            default:
                throw new CommandException("Unknown Logic Result");
        }
        return page;
    }
}
