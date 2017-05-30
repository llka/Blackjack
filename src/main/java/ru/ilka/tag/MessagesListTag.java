package ru.ilka.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Message;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.MessageLogic;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

/**
 * MessagesListTag displays all messages in one table.
 * @since %G%
 * @version %I%
 */
public class MessagesListTag extends TagSupport {
    static Logger logger = LogManager.getLogger(MessagesListTag.class);

    private static final int LINE_LENGTH = 30;
    private static final int TIME_LENGTH = 19;
    private List<Message> messages;
    private boolean received;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    @Override
    public int doStartTag() {
        JspWriter out = pageContext.getOut();
        MessageLogic messageLogic = new MessageLogic();
        try {
            for (int i = 0; i < messages.size() ; i++) {
                Message message = messages.get(i);
                int messageId = message.getMessageId();
                out.write("<div class=\"messageListRow\">");
                out.write("<div class=\"messageInfo\">");
                out.write("<div class=\"messageInfoElement\">");
                try {
                    if (received) {
                        out.write(messageLogic.findSenderLogin(messageId));
                    } else {
                        out.write(messageLogic.findReceiverLogin(messageId));
                    }
                } catch (LogicException e) {
                    logger.error("Error in login finding " + e);
                }
                out.write("</div>");
                out.write("<div class=\"messageInfoElement\">");
                out.write(message.getSendTime().substring(0,TIME_LENGTH));
                out.write("</div>");
                out.write("</div>");
                out.write("<div class=\"messageText\">");

                int lines = 1;
                StringBuilder stringBuilder = new StringBuilder(message.getText());
                while (stringBuilder.length() > LINE_LENGTH * lines) {
                    stringBuilder.insert(LINE_LENGTH * lines, "\n");
                    lines++;
                }
                out.write(stringBuilder.toString());
                out.write("</div>");
                if(received) {
                    out.write("<div class=\"actionMark\">");
                    if (message.isReadMark()) {
                        out.write("<input type=\"checkbox\" name=\"" + "read" + messageId + "\" value=\"read\" disabled checked>");
                    } else {
                        out.write("<input type=\"checkbox\" name=\"" + "read" + messageId + "\" value=\"read\">");
                    }
                    out.write("</div>");
                    out.write("<div class=\"actionDelete\">");
                    out.write("<input type=\"checkbox\" name=\"" + "delete" + messageId + "\" value=\"delete\">");
                    out.write("</div>");
                }else {
                    out.write("<div class=\"actionMarkSent\">");
                    if (message.isReadMark()) {
                        out.write("<input type=\"checkbox\" name=\"" + "read" + messageId + "\" value=\"read\" disabled checked>");
                    } else {
                        out.write("<input type=\"checkbox\" name=\"" + "read" + messageId + "\" value=\"read\" disabled>");
                    }
                    out.write("</div>");
                }
                out.write("</div>");
            }
        } catch (IOException e) {
            logger.error("Error in MessagesList, can't show all messages " + e);
        }
        return SKIP_BODY;
    }
}
