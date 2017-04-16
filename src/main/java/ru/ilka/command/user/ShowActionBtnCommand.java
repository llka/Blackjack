package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Deal;
import ru.ilka.exception.CommandException;
import ru.ilka.logic.GameLogic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.GAME_DEAL_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ShowActionBtnCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        HttpSession session = request.getSession();
        Deal deal = (Deal) session.getAttribute(GAME_DEAL_KEY);
        GameLogic gameLogic = new GameLogic();

        StringBuilder result = new StringBuilder();
        gameLogic.suggestActionButtons(deal,result);
        return result.toString();
    }
}
