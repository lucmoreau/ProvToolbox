package org.openprovenance.prov.pservice;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ServiceUtils;

import org.openprovenance.prov.xml.ProvFactory;
import org.apache.log4j.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ApplicationPath;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;

import javax.ws.rs.core.Context;
import javax.xml.bind.JAXBException;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Info;




@Path("")
public class ViewService {
    static Logger logger = Logger.getLogger(ViewService.class);


    public static final String longVersion =  ServiceUtils.longContainerVersion;



    final ProvFactory f;


    public ViewService() {
        f = ProvFactory.getFactory();
    }

    @GET
    @Path("/translator")
    @Produces("text/html")
    @Operation(summary = "Translator entry point")
    @Tag(name="view")
    public void translator(@Context HttpServletResponse response,
                           @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../translator.jsp").forward(request,
                                                                  response);
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

	//        response.setHeader("Access-Control-Max-Age", "3600");
	//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        request.getRequestDispatcher("../expander.jsp").forward(request,
                                                                 response);
    }

 /*   @GET
    @Path("/documents/{docId}/")
    @Produces("text/html")
    @Operation(summary = "Landing Page for a PROV Document",
	       description = "This page links to other documents",
               responses = { @ApiResponse(responseCode = "404", description = "Document not found") })
    @Tag(name="view")
    public void documentLandingPage(@Context HttpServletResponse response,
                                    @Context HttpServletRequest request,
                                    @PathParam("docId") String msg)
            throws ServletException, IOException {

        request.getRequestDispatcher("../../../document.jsp").forward(request,
                                                                      response);
    }
*/

 /*
    @GET
    @Path("/documents/{docId}/translation.html")
    @Operation(summary = "Translation of a document into given representation",
	       description = "No content negotiation allowed here",
	       responses = { @ApiResponse(responseCode = "404", description = "Document not found") })
    @Tag(name="view")
    public void actionTranslateLanding(@Context HttpServletResponse response,
                                       @Context HttpServletRequest request,
                                       @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg)
            throws FileNotFoundException, JAXBException, IOException,
            ServletException {
        logger.debug("translate landing");

        request.setAttribute("docId", msg);
        request.getRequestDispatcher("../../../translate.jsp")
                .forward(request, response);

    }


  */

    
    @GET
    @Path("/documents/{docId}/vis/{kind}")
    @Operation(summary = "Translation of a document into given representation",
	       description = "No content negotiation allowed here",
	       responses = { @ApiResponse(responseCode = "404", description = "Document not found") })
    @Tag(name="view")
    public void vis(@Context HttpServletResponse response,
                    @Context HttpServletRequest request,
                    @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg,
                    @Parameter(name = "kind", description = "visualization kind", example = "hive,gantt,wheel", required = true) @PathParam("kind") String kind)
            throws FileNotFoundException, JAXBException, IOException,
            ServletException {
        logger.debug("visualization page for " + kind);

        request.setAttribute("docId", msg);
        request.setAttribute("kind", kind);
        request.getRequestDispatcher("../../../../vis.jsp").forward(request,
                                                                    response);

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

        request.getRequestDispatcher("../api.jsp").forward(request, response);
    }

    @GET
    @Path("/about")
    @Produces("text/html")
    @Operation(summary = "About Validator and PROV Service")
    @Tag(name="view")
    public void getAboutPage(@Context HttpServletResponse response,
                                  @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../about.jsp").forward(request, response);
    }

    @GET
    @Path("/contact")
    @Produces("text/html")
    @Operation(summary = "Contact details")
    @Tag(name="view")
    public void getContactPage(@Context HttpServletResponse response,
                                  @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../contact.jsp").forward(request, response);
    }

}
