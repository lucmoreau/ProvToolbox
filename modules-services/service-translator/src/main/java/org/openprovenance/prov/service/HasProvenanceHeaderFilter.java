package org.openprovenance.prov.service;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class HasProvenanceHeaderFilter  implements Filter {

    //logger
    static Logger logger = LogManager.getLogger(HasProvenanceHeaderFilter.class);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        if (requestURI.contains("webjars/prov-template-library/")) {

            if (requestURI.contains("templates/org/openprovenance/prov/templates/")
                    && (requestURI.endsWith(".provn") || requestURI.endsWith(".png"))) {
                HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

                // last section of requestURI after the last slash
                String templateName = requestURI.substring(requestURI.lastIndexOf("/") + 1);
                // prefix preceding the last slash
                String prefix = requestURI.substring(0, requestURI.lastIndexOf("/"));
                // remove extension
                templateName = templateName.substring(0, templateName.lastIndexOf("."));

                String theProvenance = prefix + "/prov-" + templateName + ".prov-csv";
                httpServletResponse.setHeader("Link", "<" + theProvenance + ">; rel=\"http://www.w3.org/ns/prov#has_provenance\"");

                //Link: <http://acme.example.org/super-widget123/provenance>;
                //         rel="http://www.w3.org/ns/prov#has_provenance"



                logger.info("filter: " + requestURI);
                logger.info("filter:   " + theProvenance);

            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

