package ru.ilka.command.user;

import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Visitor;

import ru.ilka.manager.ConfigurationManager;
import ru.ilka.manager.MessageManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class AddBalanceCommand implements ActionCommand {

    private static final String PAGE_PROFILE = "path.page.profile";
    private static final String PAGE_PAY_CARD = "path.page.payCard";
    private static final String PARAM_AMOUNT = "amount";
    private static final String ATTRIBUTE_AMOUNT_TO_ADD = "amountToAdd";
    private static final String MESSAGE_INVALID_AMOUNT = "message.profile.invalidAmount";
    private static final String ATTRIBUTE_AMOUNT_TO_ADD_MESSAGE = "balanceMessage";
    private static final int MIN_ADD_AMOUNT = 1;
    private static final int SCALE = 2;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        String page = ConfigurationManager.getProperty(PAGE_PROFILE);

        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        String addAmount = request.getParameter(PARAM_AMOUNT);
        if(addAmount.contains(",")){
            addAmount = addAmount.replace(',','.');
        }
        if(Double.parseDouble(addAmount) >= MIN_ADD_AMOUNT) {
            BigDecimal amount = new BigDecimal(addAmount);
            session.setAttribute(ATTRIBUTE_AMOUNT_TO_ADD,amount.setScale(SCALE, BigDecimal.ROUND_FLOOR));
            page = ConfigurationManager.getProperty(PAGE_PAY_CARD);
        }else {
            request.setAttribute(ATTRIBUTE_AMOUNT_TO_ADD_MESSAGE, MessageManager.getProperty(MESSAGE_INVALID_AMOUNT,visitor.getLocale()));
        }
        return page;
    }
}
