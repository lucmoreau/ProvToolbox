package org.openprovenance.prov.service.translator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.service.core.ServiceUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import java.io.IOException;


@Path("")
public class ViewService {
    static Logger logger = LogManager.getLogger(ViewService.class);


    public static final String longVersion =  ServiceUtils.longContainerVersion;



    public ViewService() {
    }

    @GET
    @Path("/translator")
    @Produces("text/html")
    @Operation(summary = "Translator entry point")
    @Tag(name="view")
    public void translator(@Context HttpServletResponse response,
                           @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../translator.html").forward(request,response);
    }


    @GET
    @Path("/expander")
    @Produces("text/html")
    @Operation(summary = "Template expansion entry point")
    @Tag(name="view")
    public void expander(@Context HttpServletResponse response,
                         @Context HttpServletRequest request)
            throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        request.getRequestDispatcher("../expander.html").forward(request,response);
    }



    @GET
    @Path("/api")
    @Produces("text/html")
    @Operation(summary = "API documentation")
    @Tag(name="view")
    public void getApiDescription(@Context HttpServletResponse response,
                                  @Context HttpServletRequest request)
            throws ServletException, IOException {
        logger.debug(" api " +      request.getParameterMap());

        request.getRequestDispatcher("../api.html").forward(request, response);
    }

    @GET
    @Path("/about")
    @Produces("text/html")
    @Operation(summary = "About Validator and PROV Service")
    @Tag(name="view")
    public void getAboutPage(@Context HttpServletResponse response,
                             @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../about.html").forward(request, response);
    }

    @GET
    @Path("/contact")
    @Produces("text/html")
    @Operation(summary = "Contact details")
    @Tag(name="view")
    public void getContactPage(@Context HttpServletResponse response,
                               @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../contact.html").forward(request, response);
    }

}
