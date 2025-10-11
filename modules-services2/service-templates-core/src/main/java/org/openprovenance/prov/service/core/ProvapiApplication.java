
package org.openprovenance.prov.service.core;


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
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.service.core.writers.NodeMessageBodyWriter;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.core.readers.*;
import org.openprovenance.prov.service.translation.TranslationService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.openprovenance.prov.service.translation.storage.StorageSetup;


import java.util.HashSet;
import java.util.Set;

import static org.openprovenance.prov.model.interop.ApiUriFragments.FRAGMENT_PROVAPI;
import static org.openprovenance.prov.service.core.SwaggerTags.*;

@OpenAPIDefinition(
		info = @Info(
				title = "Provenance API",
				version = "0.0",
				description = "An API to process provenance",
				license = @License(name = "MIT License for ProvToolbox",
						           url = "https://github.com/lucmoreau/ProvToolbox/blob/master/license.txt"),
				contact = @Contact(url = "https://openprovenance.org/",
						           name = "Luc Moreau",
						           email = "luc.moreau@sussex.ac.uk")
		),
		tags = {
				@Tag(name = DOCUMENTS,  description = "provenance documents",     externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = TEMPLATE,   description = "provenance templates",     externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = VALIDATION, description = "provenance validation",    externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = RESOURCES, 	description = "static resources",    	externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = RANDOM,     description = "provenance random generation",        externalDocs = @ExternalDocumentation(description = "docs desc")),
				@Tag(name = VIEW,       description = "browsing interface",             externalDocs = @ExternalDocumentation(description = "docs desc"))
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


@ApplicationPath(FRAGMENT_PROVAPI)
public class ProvapiApplication extends Application implements ApiUriFragments {

	static Logger logger = LogManager.getLogger(ProvapiApplication.class);


	private final Set<Object> singletons = new HashSet<>();

	public final StorageSetup storageSetup = new StorageSetup();

	public ProvapiApplication() {
		logger.info("ProvapiApplication constructor ... start");
		InteropFramework intF=new InteropFramework();
		final ProvFactory factory = InteropFramework.getDefaultFactory();


		StorageConfiguration sc = StorageConfiguration.loadConfiguration();
        System.out.println("===== Storage configuration: " + sc);

		ServiceUtilsConfig config= storageSetup.makeConfig(factory, sc);

		PostService ps=new PostService(config);

		ps.addToConfiguration("storage.config", config.configuration);
		ps.addToConfiguration("cli.config", intF.getConfig());
		ps.addToConfiguration("version", Configuration.toolboxVersion);
		ps.addToConfiguration("long.version", Configuration.longToolboxVersion);



		singletons.add(ps);
		singletons.add(new TranslationService(ps));
		singletons.add(new org.openprovenance.prov.service.translation.TemplateService(ps));
		singletons.add(new TemplateService(ps));
		singletons.add(new ResourcesService());


		singletons.add(new OpenApiResource());
		singletons.add(new AcceptHeaderOpenApiResource());
		singletons.add(new JsonOrCsvMessageBodyReader());
		singletons.add(new TableKeyListMessageBodyReader());
		singletons.add(new TemplatesVizConfigMessageBodyReader());
		singletons.add(new SearchConfigMessageBodyReader());



		singletons.add(new VanillaDocumentMessageBodyWriter(new InteropFramework()));
		singletons.add(new NodeMessageBodyWriter());

		//singletons.add(new HasProvenanceHeaderFilter());
		
	    CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        singletons.add(corsFilter);



		logger.info("ProvapiApplication constructor ... completion");


	}


	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	
}
