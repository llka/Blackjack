package ru.ilka.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Visitor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;


/**
 * Filter that forbids common users using admin functionality.
 * @since %G%
 * @version %I%
 */
@WebFilter(filterName = "AdminAccessFilter", urlPatterns = "/jsp/admin/*", dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST})
public class AdminAccessFilter implements Filter {
    static Logger logger = LogManager.getLogger(GuestAccessFilter.class);

    @Override
    public void init(FilterConfig filterConfig){

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Visitor visitor = (Visitor) request.getSession().getAttribute(VISITOR_KEY);

        if(!visitor.getRole().equals(Visitor.Role.ADMIN)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
