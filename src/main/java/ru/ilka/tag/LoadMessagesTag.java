package ru.ilka.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Message;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.MessageLogic;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Here could be your advertisement +375 29 3880490
 */
public class LoadMessagesTag extends TagSupport {
    static Logger logger = LogManager.getLogger(TagSupport.class);

    private static final String ATTRIBUTE_RECEIVED = "received";
    private static final String ATTRIBUTE_SENT = "sent";
    private int accountId;

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public int doStartTag() {
        MessageLogic messageLogic = new MessageLogic();
        try {
            ArrayList<Message> receivedMessages = messageLogic.findReceivedMessages(accountId);
            ArrayList<Message> sentMessages = messageLogic.findSentMessages(accountId);
            pageContext.setAttribute(ATTRIBUTE_RECEIVED,receivedMessages);
            pageContext.setAttribute(ATTRIBUTE_SENT,sentMessages);
            return EVAL_PAGE;
        } catch (LogicException e) {
            logger.error("Error while trying to get messages lists " + e);
        }
        return SKIP_BODY;
    }
}
