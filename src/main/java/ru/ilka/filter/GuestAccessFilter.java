package ru.ilka.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Visitor;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Filter that forbids unregistered visitors use this system.
 * @since %G%
 * @version %I%
 */
@WebFilter(filterName = "GuestAccessFilter", urlPatterns = {"/jsp/guest/*"}, dispatcherTypes = {DispatcherType.FORWARD, DispatcherType.REQUEST})
public class GuestAccessFilter implements Filter {
    static Logger logger = LogManager.getLogger(GuestAccessFilter.class);

    private static final String PAGE_MAIN = "path.page.main";
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Visitor visitor = (Visitor) request.getSession().getAttribute(VISITOR_KEY);

        if(!visitor.getRole().equals(Visitor.Role.GUEST)){
            String page = ConfigurationManager.getProperty(PAGE_MAIN);
            ServletContext servletContext = filterConfig.getServletContext();
            RequestDispatcher dispatcher = servletContext.getRequestDispatcher(page);
            dispatcher.forward(request, response);
        }else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
