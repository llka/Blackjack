package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.*;
import ru.ilka.exception.CommandException;
import ru.ilka.logic.GameLogic;
import ru.ilka.logic.LogicResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;

import static ru.ilka.controller.ControllerConstants.*;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class DealCardsCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(DealCardsCommand.class);

    private static final String PARAM_BET_1 = "bet1";
    private static final String PARAM_BET_2 = "bet2";
    private static final String PARAM_BET_3 = "bet3";
    private static final boolean IS_USER_IN_GAME = true;
    private static final int PLACES_FOR_BETS_QNT = 3;
    private static final int DEALER_HAND = 0;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        GameLogic gameLogic = new GameLogic();
        ArrayList<Double> bets = new ArrayList<>(PLACES_FOR_BETS_QNT);
        boolean[] hands = new boolean[PLACES_FOR_BETS_QNT + 1];

        bets.add(Double.parseDouble(request.getParameter(PARAM_BET_1)));
        bets.add(Double.parseDouble(request.getParameter(PARAM_BET_2)));
        bets.add(Double.parseDouble(request.getParameter(PARAM_BET_3)));

        int betSumm = 0;
        hands[DEALER_HAND] = true;
        for (int i = 0; i < bets.size(); i++) {
            hands[i+1] = bets.get(i) >= settings.getMinBet();
            betSumm += bets.get(i);
        }

        StringBuilder result = new StringBuilder();
        if(betSumm == 0){
            result.append("");
        }else if(account.getBalance().compareTo(BigDecimal.valueOf(betSumm)) >= 0) {
            ArrayList<ArrayList<LogicResult>> cards = gameLogic.dealCards(hands);
            ArrayList<Integer> points = gameLogic.countPoints(cards);

            Deal deal = new Deal(cards,bets,points);
            deal.setAlreadyUsedCards(gameLogic.getAlreadyUsed());
            gameLogic.showFirstCards(deal,result);

            session.setAttribute(GAME_DEAL_KEY,deal);
            session.setAttribute(IN_GAME_KEY,IS_USER_IN_GAME);
        }else {
            result.append("<div class=\"error\">Bets are bigger than your balance, try bet less!</div>");
        }
        return result.toString();
    }
}
