package com.qg.www.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 字符过滤器，避免多次编写setCharacterEncoding("utf-8");
 * @author linxu
 * @version 1.2
 *
 */
public class CharacterFilter implements Filter {
    private String encoding=null;
    @Override
    public void destroy() {
        encoding=null;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(encoding!=null)
        {
            request.setCharacterEncoding(encoding);
            response.setContentType("text/html;charset="+encoding);
        }
        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig fConfig)  {
        encoding=fConfig.getInitParameter("encoding");
    }
}