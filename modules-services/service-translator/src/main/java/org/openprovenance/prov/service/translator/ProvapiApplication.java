
package org.openprovenance.prov.service.translator;

 
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.translation.*;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.info.Info;
import org.openprovenance.prov.xml.ProvFactory;

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
				@Tag(name = "documents",  description = "provenance api (documents)",     externalDocs = @ExternalDocumentation(description = "docs desc")),
			//	@Tag(name = "provapi",    description = "provenance api",                 externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = "vis",        description = "provenance api (visualisation)", externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = "random",     description = "provenance api (random)",        externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = "view",       description = "browsing interface",             externalDocs = @ExternalDocumentation(description = "NOTE: /provapi is incorrect and should be /view"))
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
						url = "http://localhost:{port}/",
						variables = {
								@ServerVariable(name = "port", description = "service port", defaultValue = "7070", allowableValues = {"7070", "8080"})
						})
		}
)

@ApplicationPath("/provapi")
public class ProvapiApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();


	StorageConfiguration sc=new StorageConfiguration();


	public ProvapiApplication() {
		final ProvFactory factory = ProvFactory.getFactory();
		ServiceUtilsConfig config=sc.makeFSConfig(factory);


		PostService ps=new PostService(config);
		singletons.add(ps);
		singletons.add(new TranslationService(ps));
		singletons.add(new TemplateService(ps));
		singletons.add(new RandomService(ps));

		singletons.add(new VisService(ps));
		//singletons.add(new VisService());
		singletons.add(new OpenApiResource());
		singletons.add(new AcceptHeaderOpenApiResource());
		
		//singletons.add(new io.swagger.jaxrs.listing.SwaggerSerializers());
		//singletons.add(new io.swagger.jaxrs.listing.ApiListingResource());
		singletons.add(new DocumentMessageBodyWriter(new InteropFramework()));
		singletons.add(new NodeMessageBodyWriter());			
		
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
