package ru.ilka.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Account;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * AllUsersTag displays all accounts in one table.
 * @since %G%
 * @version %I%
 */
public class AllUsersTag extends TagSupport {
    static Logger logger = LogManager.getLogger(AllUsersTag.class);
    private final static String JUVE_INVITE = "JUVE-TOP1";
    private List<Account> accounts;

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public int doStartTag(){
        JspWriter out = pageContext.getOut();

        try {
            for (int i = 0; i < accounts.size() ; i++) {
                out.write("<tr>");
                Account account = accounts.get(i);
                BigDecimal casinoProfit = account.getMoneySpend().subtract(account.getMoneyWon());
                out.write("<td>" + account.getAccountId() + "</td>");
                out.write("<td>" + account.getLogin() + "</td>");
                out.write("<td>");
                if(JUVE_INVITE.equals(account.getInviteCode())) {
                    out.write("+");
                }
                else {
                    out.write("-");
                }
                out.write("</td>");
                out.write("<td>" + account.getBalance() + "</td>");
                out.write("<td>" + account.getHandsPlayed() + "</td>");
                out.write("<td>" + account.getHandsWon() + "</td>");
                out.write("<td>" + casinoProfit + "</td>");
                out.write("<td>" + account.getRating() + "</td>");
                out.write("<td>");
                if(account.isAdmin()){
                    out.write("<input type=\"checkbox\" name=\"" + "admin" + account.getAccountId() + "\" value=\"admin\" checked>");
                }else {
                    out.write("<input type=\"checkbox\" name=\"" + "admin" + account.getAccountId() + "\" value=\"admin\">");
                }
                out.write("</td>");
                out.write("<td>");
                if(account.isBan()){
                    out.write("<input type=\"checkbox\" name=\"" + account.getAccountId() + "\" value=\"ban\" checked>");
                }else {
                    out.write("<input type=\"checkbox\" name=\"" + account.getAccountId() + "\" value=\"ban\">");
                }
                out.write("</td>");
                out.write("</tr>");
            }
        } catch (IOException e) {
            logger.error("Error in AllUsersTag, can't show all accounts " + e);
        }
        return SKIP_BODY;
    }
}
