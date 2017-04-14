package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.AVATAR_DIRECTORY_STATIC;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ChangeAvatarCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(ChangeAvatarCommand.class);

    private static final String PARAM_AVATAR = "avatar";
    private static final String PAGE_PROFILE = "path.page.profile";
    private static final String FILE_DELIMITER = "\\";
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        AccountLogic accountLogic = new AccountLogic();
        String page = ConfigurationManager.getProperty(PAGE_PROFILE);
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);

        String uploadFilePath = AVATAR_DIRECTORY_STATIC;

        // creates the save directory if it does not exists
        File fileSaveDir = new File(uploadFilePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        Part filePart = null;
        try {
            filePart = request.getPart(PARAM_AVATAR);
        }catch (IOException | ServletException e) {
            throw new CommandException("Error while getting file part from request " + e);
        }
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        try {
            //Get all the parts from request and write it to the file on server
            for (Part part : request.getParts()) {
                part.write(uploadFilePath + FILE_DELIMITER + fileName);
            }
        }catch (IOException | ServletException e){
            throw new CommandException("Error while writing file parts " + e);
        }

        try {
            accountLogic.changeAvatar(account.getAccountId(),fileName);
            account.setAvatar(fileName);
            logger.debug("New avatar - " + fileName);
        } catch (LogicException e) {
            logger.error("Can't change avatar in data base " + e);
        }

        return page;
    }
}
