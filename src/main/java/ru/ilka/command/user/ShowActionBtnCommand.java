package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Deal;
import ru.ilka.entity.GameSettings;
import ru.ilka.logic.GameLogic;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.GAME_DEAL_KEY;
import static ru.ilka.controller.ControllerConstants.IN_GAME_KEY;
import static ru.ilka.controller.ControllerConstants.SETTINGS_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ShowActionBtnCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        ServletContext servletContext = request.getServletContext();
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        Deal deal = (Deal) session.getAttribute(GAME_DEAL_KEY);
        GameLogic gameLogic = new GameLogic(settings.getNumberOfDecks());

        boolean inGame = gameLogic.isUserInGame(deal);
        session.setAttribute(IN_GAME_KEY, inGame);
        StringBuilder result = new StringBuilder();
        gameLogic.suggestActionButtons(deal,result);
        if(!inGame){
            gameLogic.suggestNewGame(result);
        }
        return result.toString();
    }
}
