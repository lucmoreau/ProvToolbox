package org.openprovenance.prov.service.translator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.openprovenance.prov.service.core.ServiceUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;


@Path("")
public class ViewService {
    static Logger logger = Logger.getLogger(ViewService.class);


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
        request.getRequestDispatcher("../../../../vis.jsp").forward(request, response);

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
