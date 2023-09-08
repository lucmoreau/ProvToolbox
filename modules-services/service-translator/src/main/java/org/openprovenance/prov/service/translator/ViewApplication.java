
package org.openprovenance.prov.service.translator;


import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@OpenAPIDefinition(
		info = @Info(
				title = "Provenance API",
				version = "0.0",
				description = "An API to process provenance",
				license = @License(name = "MIT License for ProvToolbox",
						url = "https://github.com/lucmoreau/ProvToolbox/blob/master/license.txt"),
				contact = @Contact(url = "https://openprovenance.org/",
						name = "Luc Moreau",
						email = "provenance@kcl.ac.uk")
		),
		tags = {
				@Tag(name = "view",       description = "browsing interface",             externalDocs = @ExternalDocumentation(description = "docs desc"))
		},
		externalDocs = @ExternalDocumentation(description = "definition docs desc"),
		security = {
				@SecurityRequirement(name = "req 1", scopes = {"a", "b"}),
				@SecurityRequirement(name = "req 2", scopes = {"b", "c"})
		},
		servers = {
				@Server(
						description = "production",
						url = "https://openprovenance.org/services/"
				),
				@Server(
						description = "dev",
						url = "http://localhost:{port}/{context}/",
						variables = {
								@ServerVariable(name = "port", description = "service port", defaultValue = "7071", allowableValues = {"7070", "7071", "8080"}),
								@ServerVariable(name = "context", description = "service context", defaultValue = "service", allowableValues = {"service", "context"})
						})
		}
)

@ApplicationPath("/view")
public class ViewApplication extends Application {

	private final Set<Object> singletons = new HashSet<>();
 
	public ViewApplication() {

		singletons.add(new ViewService());
		singletons.add(new OpenApiResource());
		singletons.add(new AcceptHeaderOpenApiResource());
        
		CorsFilter corsFilter = new CorsFilter();
		corsFilter.getAllowedOrigins().add("*");
		corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
		singletons.add(corsFilter);

	}
 
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	
}
