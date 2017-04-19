package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Deal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static ru.ilka.controller.ControllerConstants.GAME_DEAL_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class InsureBetCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(InsureBetCommand.class);

    private static final String PARAM_BET_PLACE = "betPlace";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Deal deal = (Deal) session.getAttribute(GAME_DEAL_KEY);
        boolean[] insuredBets = deal.getInsuredBets();
        insuredBets[(Integer.parseInt(request.getParameter(PARAM_BET_PLACE)))-1] = true;
        deal.setInsuredBets(insuredBets);
        session.setAttribute(GAME_DEAL_KEY,deal);
        for (int i = 0; i < insuredBets.length; i++) {
            logger.debug("insured Bet " + insuredBets[i]);
        }
        return "";
    }
}
