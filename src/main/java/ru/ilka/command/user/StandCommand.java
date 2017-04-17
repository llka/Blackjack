package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Deal;
import ru.ilka.exception.CommandException;
import ru.ilka.logic.GameLogic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.GAME_DEAL_KEY;
import static ru.ilka.controller.ControllerConstants.IN_GAME_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class StandCommand implements ActionCommand {

    private static final String PARAM_BET_PLACE = "betPlace";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        Deal deal = (Deal) session.getAttribute(GAME_DEAL_KEY);
        GameLogic gameLogic = new GameLogic();

        int betPlace = Integer.parseInt(request.getParameter(PARAM_BET_PLACE));

        StringBuilder result = new StringBuilder();
        gameLogic.stand(betPlace,deal,account,result);
        boolean inGame = gameLogic.isUserInGame(deal);
        session.setAttribute(IN_GAME_KEY,inGame);
        if (!inGame){
            gameLogic.writeDealerCards(deal,result);
        }
        return result.toString();
    }
}
