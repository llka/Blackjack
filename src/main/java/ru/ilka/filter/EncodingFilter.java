package ru.ilka.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

/**
 * Supports appropriate encoding format.
 * @since %G%
 * @version %I%
 */
@WebFilter(filterName = "EncodingFilter", urlPatterns = { "/*" }, initParams =
        {@WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding Param") },
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class EncodingFilter implements Filter {
    private String code;

    @Override
    public void init(FilterConfig filterConfig){
        code = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String codeRequest = servletRequest.getCharacterEncoding();
        String codeResponse = servletResponse.getCharacterEncoding();

        if(code != null && !code.equalsIgnoreCase(codeRequest)) {
            servletRequest.setCharacterEncoding(code);
        }
        if(code != null && !code.equalsIgnoreCase(codeResponse)) {
            servletResponse.setCharacterEncoding(code);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        code = null;
    }
}
