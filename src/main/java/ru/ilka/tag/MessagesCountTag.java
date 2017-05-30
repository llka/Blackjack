package ru.ilka.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.MessageLogic;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * MessagesCountTag displays number of new received messages.
 * @since %G%
 * @version %I%
 */
public class MessagesCountTag extends TagSupport {
    static Logger logger = LogManager.getLogger(MessagesCountTag.class);

    private int accountId;

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public int doStartTag(){
        MessageLogic messageLogic = new MessageLogic();
        int newMessagesCount = 0;
        try {
            newMessagesCount = messageLogic.findNewMessages(accountId).size();
        } catch (LogicException e) {
            logger.error("Error while trying to get new messages" + e);
        }
        if(newMessagesCount > 0) {
            JspWriter out = pageContext.getOut();
            try {
                out.write("<div class=\"messageCounter\">");
                out.write(String.valueOf(newMessagesCount));
                out.write("</div>");
            } catch (IOException e) {
                logger.error("Error in MessageCounter, can't count new messages " + e);
            }
        }
        return SKIP_BODY;
    }
}
