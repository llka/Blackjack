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

import static ru.ilka.controller.ControllerConstants.SETTINGS_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class DealCardsCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(DealCardsCommand.class);

    private static final String PARAM_BET_1 = "bet1";
    private static final String PARAM_BET_2 = "bet2";
    private static final String PARAM_BET_3 = "bet3";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        ServletContext servletContext = request.getServletContext();
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);
        int bet1 = Integer.parseInt(request.getParameter(PARAM_BET_1));
        int bet2 = Integer.parseInt(request.getParameter(PARAM_BET_2));
        int bet3 = Integer.parseInt(request.getParameter(PARAM_BET_3));

        response.setContentType("text/html");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            logger.error("Can't write new card ",e);
        }
        if(bet1 >= settings.getMinBet()) {
            pw.println("<div class=\"card1\">");
            pw.println("<div class=\"cardNumber\">");
            pw.println(1);
            pw.println("</div>");
            pw.println(" <div class=\"cardSuit\">");
            pw.println("<div class=\"spades\"></div>");
            pw.println("</div>");
            pw.println("<div class=\"cardNumberDown\">");
            pw.println(1);
            pw.println("</div>");
            pw.println("</div>");

            pw.println("<div class=\"card12\">");
            pw.println("<div class=\"cardNumber\">");
            pw.println(1);
            pw.println("</div>");
            pw.println(" <div class=\"cardSuit\">");
            pw.println("<div class=\"spades\"></div>");
            pw.println("</div>");
            pw.println("<div class=\"cardNumberDown\">");
            pw.println(1);
            pw.println("</div>");
            pw.println("</div>");
        }
        if(bet2 >= settings.getMinBet()) {
            pw.println("<div class=\"card2\">");
            pw.println("<div class=\"cardNumber\">");
            pw.println(1);
            pw.println("</div>");
            pw.println(" <div class=\"cardSuit\">");
            pw.println("<div class=\"spades\"></div>");
            pw.println("</div>");
            pw.println("<div class=\"cardNumberDown\">");
            pw.println(1);
            pw.println("</div>");
            pw.println("</div>");

            pw.println("<div class=\"card22\">");
            pw.println("<div class=\"cardNumber\">");
            pw.println(1);
            pw.println("</div>");
            pw.println(" <div class=\"cardSuit\">");
            pw.println("<div class=\"spades\"></div>");
            pw.println("</div>");
            pw.println("<div class=\"cardNumberDown\">");
            pw.println(1);
            pw.println("</div>");
            pw.println("</div>");
        }
        if(bet3 >= settings.getMinBet()) {
            pw.println("<div class=\"card3\">");
            pw.println("<div class=\"cardNumber\">");
            pw.println(1);
            pw.println("</div>");
            pw.println(" <div class=\"cardSuit\">");
            pw.println("<div class=\"spades\"></div>");
            pw.println("</div>");
            pw.println("<div class=\"cardNumberDown\">");
            pw.println(1);
            pw.println("</div>");
            pw.println("</div>");

            pw.println("<div class=\"card32\">");
            pw.println("<div class=\"cardNumber\">");
            pw.println(1);
            pw.println("</div>");
            pw.println(" <div class=\"cardSuit\">");
            pw.println("<div class=\"spades\"></div>");
            pw.println("</div>");
            pw.println("<div class=\"cardNumberDown\">");
            pw.println(1);
            pw.println("</div>");
            pw.println("</div>");
        }

        logger.debug("bet1 " + bet1);
        logger.debug("bet2 " + bet2);
        logger.debug("bet3 " + bet3);

        return "";
    }
}
