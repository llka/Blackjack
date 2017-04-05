package ru.ilka.tag;

import ru.ilka.entity.Visitor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class AdminNavTag extends TagSupport {
    private Visitor.Role role;

    public void setRole(Visitor.Role role) {
        this.role = role;
    }

    @Override
    public int doStartTag(){
        return role == Visitor.Role.ADMIN ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}
