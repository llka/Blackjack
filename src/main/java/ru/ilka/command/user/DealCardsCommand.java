package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.exception.CommandException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        logger.debug("deal card command");
        int bet1 = Integer.parseInt(request.getParameter(PARAM_BET_1));
        int bet2 = Integer.parseInt(request.getParameter(PARAM_BET_2));
        int bet3 = Integer.parseInt(request.getParameter(PARAM_BET_3));

        response.setContentType("text/html");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            logger.error("Can't erite new card ",e);
        }
        pw.println("<div class=\"cardNumber\">");
        pw.println(1);
        pw.println("</div>");
        pw.println(" <div class=\"cardSuit\">");
        pw.println("<div class=\"spades\"></div>");
        pw.println("</div>");
        pw.println("<div class=\"cardNumberDown\">");
        pw.println(1);
        pw.println("</div>");


        logger.debug("bet1 " + bet1);
        logger.debug("bet2 " + bet2);
        logger.debug("bet3 " + bet3);

        return "";
    }
}
