package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Deal;
import ru.ilka.logic.GameLogic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.*;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class CheckForInsuranceCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(CheckForInsuranceCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Deal deal = (Deal) session.getAttribute(GAME_DEAL_KEY);
        GameLogic gameLogic = new GameLogic();

        StringBuilder result = new StringBuilder();
        gameLogic.checkForInsurance(deal,result);

        return result.toString();
    }
}
