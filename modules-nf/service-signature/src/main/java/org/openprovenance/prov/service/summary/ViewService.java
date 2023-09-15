package org.openprovenance.prov.service.summary;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

//import org.openprovenance.validation.Validate;
//import org.openprovenance.prov.service.validation.ValidationResource;
//import org.openprovenance.prov.service.validation.ValidationServiceUtils;



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
                            @Tag(name = "sig", description = "signature api", externalDocs = @ExternalDocumentation(description = "docs desc")),
                            @Tag(name = "documents", description = "provenance api (documents)", externalDocs = @ExternalDocumentation(description = "docs desc")),
                            @Tag(name = "provapi", description = "provenance api", externalDocs = @ExternalDocumentation(description = "docs desc")),
                           // @Tag(name = "vis", description = "provenance api (visualisation)", externalDocs = @ExternalDocumentation(description = "docs desc")),
                           // @Tag(name = "random", description = "provenance api (random)", externalDocs = @ExternalDocumentation(description = "docs desc")),
                            @Tag(name = "view", description = "browsing interface", externalDocs = @ExternalDocumentation(description = "NOTE: /provapi is incorrect and should be /view"))
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


    public static final String longVersion =  ServiceUtils.longContainerVersion;


    public ViewService() {
    }


    @GET
    @Path("/signer")
    @Produces("text/html")
    @Operation(summary = "Signer entry point")
    public void signer(@Context HttpServletResponse response,
                       @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../signer.html").forward(request,response);
    }
    

    @GET
    @Path("/checker")
    @Produces("text/html")
    @Operation(summary = "Signer entry point")
    public void checker(@Context HttpServletResponse response,
                        @Context HttpServletRequest request)
            throws ServletException, IOException {

        request.getRequestDispatcher("../checker.html").forward(request,response);
    }


    @GET
    @Path("/api")
    @Produces("text/html")
    @Operation(summary = "API documentation")
    public void getApiDescription(@Context HttpServletResponse response,
                                  @Context HttpServletRequest request)
            throws ServletException, IOException {
        logger.debug(" api " +      request.getParameterMap());

        request.getRequestDispatcher("../api.html").forward(request, response);
    }


}
