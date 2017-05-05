package ru.ilka.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Message;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.MessageLogic;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class NewMessageTag extends TagSupport{
    static Logger logger = LogManager.getLogger(NewMessageTag.class);

    private static final String ATTRIBUTE_RECEIVED_TEXT = "receivedText";
    private static final String ATTRIBUTE_SENDER_LOGIN = "receivedFrom";
    private int accountId;

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public int doStartTag() {
        MessageLogic messageLogic = new MessageLogic();
        ArrayList<Message> received = new ArrayList<>();
        String senderLogin = "";
        try {
            received = messageLogic.findNewMessages(accountId);
        } catch (LogicException e) {
            logger.error("Error while trying to get new messages " + e);
        }
        if(!received.isEmpty()) {
            try {
                senderLogin = messageLogic.findSenderLogin(received.get(0).getMessageId());
            } catch (LogicException e) {
                logger.error("Error while trying to get sender login " + e);
            }
            pageContext.setAttribute(ATTRIBUTE_RECEIVED_TEXT,received.get(0).getText());
            pageContext.setAttribute(ATTRIBUTE_SENDER_LOGIN,senderLogin);
            return EVAL_BODY_INCLUDE;
        }
        else {
            return SKIP_BODY;
        }
    }
}
