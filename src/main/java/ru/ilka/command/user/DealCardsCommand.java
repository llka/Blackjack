package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.GameSettings;
import ru.ilka.exception.CommandException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static ru.ilka.controller.ControllerConstants.SETTINGS_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class DealCardsCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(DealCardsCommand.class);

    private static final String PARAM_BET_1 = "bet1";
    private static final String PARAM_BET_2 = "bet2";
    private static final String PARAM_BET_3 = "bet3";
    private static final int PLACES_FOR_BETS_QNT = 3;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ServletContext servletContext = request.getServletContext();
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        ArrayList<Integer> bets = new ArrayList<>(PLACES_FOR_BETS_QNT);
        bets.add(Integer.parseInt(request.getParameter(PARAM_BET_1)));
        bets.add(Integer.parseInt(request.getParameter(PARAM_BET_2)));
        bets.add(Integer.parseInt(request.getParameter(PARAM_BET_3)));

        logger.debug("bets " + bets);

        StringBuilder result = new StringBuilder();

        for (int i = 1; i < bets.size() + 1 ; i++) {
            if(bets.get(i-1) > settings.getMinBet()){
                for (int j = 1; j < 3; j++) {
                    result.append("<div class=\"card" + i + j + "\">\n");
                    result.append("<div class=\"cardNumber\">");
                    result.append(1);
                    result.append("</div>\n");
                    result.append("<div class=\"cardSuit\">");
                    result.append("<div class=\"spades\"></div>");
                    result.append("</div>\n");
                    result.append("<div class=\"cardNumberDown\">");
                    result.append(1);
                    result.append("</div>\n");
                    result.append("</div>");
                }
            }
        }

        return result.toString();
    }
}
