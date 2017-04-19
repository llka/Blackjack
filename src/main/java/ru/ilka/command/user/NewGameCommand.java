package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.exception.CommandException;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.IN_GAME_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class NewGameCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(NewGameCommand.class);

    private static final String PAGE_GAME = "path.page.game";
    private static final boolean NOT_IN_GAME = false;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        String page = ConfigurationManager.getProperty(PAGE_GAME);
        HttpSession session = request.getSession();
        session.setAttribute(IN_GAME_KEY,NOT_IN_GAME);
        return page;
    }
}
