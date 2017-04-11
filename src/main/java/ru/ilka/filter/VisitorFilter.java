package ru.ilka.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.GameSettings;
import ru.ilka.entity.Visitor;
import ru.ilka.logic.SettingsLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static ru.ilka.controller.ControllerConstants.*;

/**
 * Here could be your advertisement +375 29 3880490
 */
@WebFilter(filterName = "VisitorFilter", urlPatterns = { "/*" })
public class VisitorFilter implements Filter {
    static Logger logger = LogManager.getLogger(VisitorFilter.class);
    private static final String PAGE_START = "path.page.start";

    @Override
    public void init(FilterConfig filterConfig){

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Visitor visitor = (Visitor)request.getSession().getAttribute(VISITOR_KEY);
        if (visitor == null) {
            visitor = new Visitor(Visitor.Role.GUEST, DEFAULT_LOCALE, ConfigurationManager.getProperty(PAGE_START));
            request.getSession().setAttribute(VISITOR_KEY, visitor);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
