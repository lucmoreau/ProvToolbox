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


    @GET
    @Path("/dashboard")
    @Produces("text/html")
    @Operation(summary = "dashboardr entry point")
    public void dashboard(@Context HttpServletResponse response,
                          @Context HttpServletRequest request)
            throws ServletException, IOException {
        request.getRequestDispatcher("../dashboard.html").forward(request,response);
    }





}
