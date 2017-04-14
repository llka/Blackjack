package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Game;
import ru.ilka.entity.GameSettings;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.CommandException;
import ru.ilka.logic.GameLogic;
import ru.ilka.logic.LogicResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.SETTINGS_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class DealCardsCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(DealCardsCommand.class);

    private static final String PARAM_BET_1 = "bet1";
    private static final String PARAM_BET_2 = "bet2";
    private static final String PARAM_BET_3 = "bet3";
    private static final int PLACES_FOR_BETS_QNT = 3;
    private static final int DEALER_HAND = 0;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ServletContext servletContext = request.getServletContext();
        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        GameLogic gameLogic = new GameLogic();
        Game game = new Game();

        ArrayList<Integer> bets = new ArrayList<>(PLACES_FOR_BETS_QNT);
        bets.add(Integer.parseInt(request.getParameter(PARAM_BET_1)));
        bets.add(Integer.parseInt(request.getParameter(PARAM_BET_2)));
        bets.add(Integer.parseInt(request.getParameter(PARAM_BET_3)));

        boolean[] hands = new boolean[PLACES_FOR_BETS_QNT + 1];
        hands[DEALER_HAND] = true;
        for (int i = 0; i < bets.size(); i++) {
            hands[i+1] = bets.get(i) > settings.getMinBet();
        }

        ArrayList<ArrayList<LogicResult>> cards = gameLogic.dealCards(hands);
        ArrayList<Integer> points = gameLogic.countPoints(cards);

        StringBuilder result = new StringBuilder();

        result.append("<div class=\"DealerCard1\">\n");
        result.append("<div class=\"cardBack\">");
        result.append("</div>\n");
        result.append("</div>\n");

        result.append("<div class=\"DealerCard2\">\n");
        gameLogic.writeCard(cards.get(DEALER_HAND).get(1),result);
        result.append("</div>");

        for (int i = 1; i < cards.size(); i++) {
            if(!cards.get(i).isEmpty()){
                for (int j = 1; j < 3; j++) {
                    result.append("<div class=\"card" + i + j + "\">\n");
                    gameLogic.writeCard(cards.get(i).get(j-1),result);
                    if(j == 2){
                        gameLogic.writePoints(points.get(i),result);
                    }
                    result.append("</div>");
                }
            }
        }

        return result.toString();
    }
}
