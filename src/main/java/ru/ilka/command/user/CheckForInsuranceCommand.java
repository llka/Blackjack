package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Deal;
import ru.ilka.entity.GameSettings;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.GameLogic;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.*;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class CheckForInsuranceCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();
        Deal deal = (Deal) session.getAttribute(GAME_DEAL_KEY);
        ServletContext servletContext = request.getServletContext();
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        GameLogic gameLogic = new GameLogic(settings.getNumberOfDecks());

        StringBuilder result = new StringBuilder();
        try {
            gameLogic.checkForInsurance(deal,result);
        } catch (LogicException e) {
            throw new CommandException("Error while checking fkr insurance " + e);
        }

        return result.toString();
    }
}
