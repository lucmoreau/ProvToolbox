package org.openprovenance.prov.service.core;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter({"*.provn", "*.provx"})
public class ServletConfig implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        String extension="";
        if (request instanceof HttpServletRequest) {
            String url = ((HttpServletRequest) request).getRequestURL().toString();
            final int index = url.lastIndexOf(".");
            final int len = url.length();
            if (index >0 && index+1< len) {
                extension=url.substring(index+1,len);
            }
        };

        switch (extension) {
            case "provn" : resp.setContentType("text/provenance-notation"); break;
            case "provx" : resp.setContentType("application/provenance-xml"); break;
        }
        chain.doFilter(request, resp);
    }
}