package ru.ilka.tag;

import ru.ilka.entity.Visitor;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * AdminNavTag displays administrator functionality block according to visitors role.
 * @since %G%
 * @version %I%
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
