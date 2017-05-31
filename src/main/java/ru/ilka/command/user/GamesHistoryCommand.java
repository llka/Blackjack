package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Game;
import ru.ilka.entity.GameSettings;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.GameLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.SETTINGS_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class GamesHistoryCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(GamesHistoryCommand.class);
    private static final String PAGE_HISTORY = "path.page.history";
    private static final String ATTRIBUTE_GAMES = "games";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();

        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        ServletContext servletContext = request.getServletContext();
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        GameLogic gameLogic = new GameLogic(settings.getNumberOfDecks());

        try {
            List<Game> games = gameLogic.loadGamesHistory(account.getAccountId());
            request.setAttribute(ATTRIBUTE_GAMES,games);
        } catch (LogicException e) {
            throw new CommandException("Can not get games history " + e);
        }

        return ConfigurationManager.getProperty(PAGE_HISTORY);
    }
}
