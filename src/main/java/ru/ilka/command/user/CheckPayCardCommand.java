package ru.ilka.command.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.Account;
import ru.ilka.entity.Visitor;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.AccountLogic;
import ru.ilka.logic.LogicResult;
import ru.ilka.manager.ConfigurationManager;
import ru.ilka.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class CheckPayCardCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(CheckPayCardCommand.class);

    private static final String PAGE_PROFILE = "path.page.profile";
    private static final String PAGE_PAY_CARD = "path.page.payCard";
    private static final String PARAM_TIME_LEFT = "timeLeft";
    private static final String PARAM_NAME = "fio";
    private static final String ATTRIBUTE_AMOUNT_TO_ADD = "amountToAdd";
    private static final String MESSAGE_WRONG_NAME = "message.payCard.error.name";
    private static final String MESSAGE_UNAVAILABLE_CARD = "message.payCard.error.card";
    private static final String ATTRIBUTE_CHECK_PAY_CARD_MESSAGE = "payCardMessage";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_PROFILE);

        HttpSession session = request.getSession();
        Visitor visitor = (Visitor) session.getAttribute(VISITOR_KEY);
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        AccountLogic accountLogic = new AccountLogic();
        int timeLeft = Integer.parseInt(request.getParameter(PARAM_TIME_LEFT));
        String name =  request.getParameter(PARAM_NAME);

        LogicResult logicResult = accountLogic.checkPayCard(account,name,timeLeft);
        switch (logicResult){
            case OK:
                BigDecimal amount = (BigDecimal) session.getAttribute(ATTRIBUTE_AMOUNT_TO_ADD);
                try {
                    accountLogic.addToBalance((int)account.getAccountId(),amount);
                    account.setBalance(account.getBalance().add(amount));
                } catch (LogicException e) {
                    logger.error("Can't add money to balance " + e);
                }
                session.removeAttribute(ATTRIBUTE_AMOUNT_TO_ADD);
                session.setAttribute(ACCOUNT_KEY,account);
                break;
            case WRONG_NAME:
                page = ConfigurationManager.getProperty(PAGE_PAY_CARD);
                request.setAttribute(ATTRIBUTE_CHECK_PAY_CARD_MESSAGE, MessageManager.getProperty(MESSAGE_WRONG_NAME,visitor.getLocale()));
                break;
            case UNAVALIABLE_CARD:
                page = ConfigurationManager.getProperty(PAGE_PAY_CARD);
                request.setAttribute(ATTRIBUTE_CHECK_PAY_CARD_MESSAGE, MessageManager.getProperty(MESSAGE_UNAVAILABLE_CARD,visitor.getLocale()));
                break;
            default:
                throw new CommandException("Unknown Logic Result achieved in pay card check command");
        }
        return page;
    }
}
