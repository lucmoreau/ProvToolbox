package org.openprovenance.prov.service.xplain;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.core.ServiceUtils;

import java.io.IOException;

@Path("")
@OpenAPIDefinition(
        info = @Info(
                title = "the title",
                version = "0.0",
                description = "My API",
                license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
        ),
        tags = {
                @Tag(name = "documents", description = "provenance api (documents)", externalDocs = @ExternalDocumentation(description = "docs desc")),
                @Tag(name = "narrative", description = "provenance api (narrative)", externalDocs = @ExternalDocumentation(description = "docs desc")),

                @Tag(name = "view", description = "browsing interface", externalDocs = @ExternalDocumentation(description = "NOTE: /provapi is incorrect and should be /view")),
                @Tag(name = "provapi", description = "provenance api", externalDocs = @ExternalDocumentation(description = "docs desc"))

        },
        externalDocs = @ExternalDocumentation(description = "definition docs desc"),
        security = {
                @SecurityRequirement(name = "req 1", scopes = {"a", "b"}),
                @SecurityRequirement(name = "req 2", scopes = {"b", "c"})
        }
        /*                   servers = {
                                   @Server(
                                           description = "server 1",
                                           url = "http://foo",
                                           variables = {
                                                   @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                                                   @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
                                           })
                           } */
)
@Tag(name="view")
public class ViewService {
    static Logger logger = LogManager.getLogger(ViewService.class);


    @GET
    @Path("/narrator")
    @Produces("text/html")
    @Operation(summary = "Signer entry point")
    public void signer(@Context HttpServletResponse response,
                       @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../../narrator.html").forward(request, response);
    }



    @GET
    @Path("/dashboard")
    @Produces("text/html")
    @Operation(summary = "dashboard entry point")
    public void dashboard(@Context HttpServletResponse response,
                          @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../../dashboard.html").forward(request,
                response);
    }



    @GET
    @Path("/editor")
    @Produces("text/html")
    @Operation(summary = "editor entry point")
    public void editor(@Context HttpServletResponse response,
                       @Context HttpServletRequest request)
            throws ServletException, IOException {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
        request.getRequestDispatcher("../editor.html").forward(request, response);
    }


    @GET
    @Path("/documents/{docId}/")
    @Produces("text/html")
    @Operation(summary = "Landing Page for a PROV Document",
            description = "This page links to other documents",
            responses = { @ApiResponse(responseCode = "404", description = "Document not found") })
    public void documentLandingPage(@Context HttpServletResponse response,
                                    @Context HttpServletRequest request,
                                    @PathParam("docId") String msg)
            throws ServletException, IOException {

        request.getRequestDispatcher("../../../document.jsp").forward(request, response);
    }



    @GET
    @Path("/api")
    @Produces("text/html")
    @Operation(summary = "API documentation")
    public void getApiDescription(@Context HttpServletResponse response,
                                  @Context HttpServletRequest request)
            throws ServletException, IOException {
        logger.debug(" api " +      request.getParameterMap());
        String uri=request.getRequestURI();
        int pos=uri.lastIndexOf("#");
        String suffix="";
        if (pos>0) {
            suffix=uri.substring(pos);
        }

        request.getRequestDispatcher("../../api.html" + suffix).forward(request, response);
    }

}