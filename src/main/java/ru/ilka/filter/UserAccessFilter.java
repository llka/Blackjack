package ru.ilka.filter;

import ru.ilka.entity.Visitor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
@WebFilter(filterName = "UserAccessFilter", urlPatterns = { "/jsp/user/*"}, dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST})
public class UserAccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig){

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Visitor visitor = (Visitor) request.getSession().getAttribute(VISITOR_KEY);

        if(visitor.getRole().equals(Visitor.Role.GUEST)){
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
