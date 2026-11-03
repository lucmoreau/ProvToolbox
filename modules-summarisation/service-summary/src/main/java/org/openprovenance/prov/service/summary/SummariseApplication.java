
package org.openprovenance.prov.service.summary;


import tools.jackson.jakarta.rs.json.JacksonJsonProvider;
import io.swagger.v3.rest.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.rest.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.summary.writers.Level0MessageBodyWriter;
import org.openprovenance.prov.service.summary.writers.ScalaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.translation.TranslationService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.openprovenance.prov.service.translation.storage.StorageSetup;

import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/provapi")
public class SummariseApplication extends Application {
	private final Set<Object> singletons = new HashSet<>();


	public SummariseApplication() {
		InteropFramework intF=new InteropFramework();
		org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();

		StorageSetup storageSetup = new StorageSetup();
		StorageConfiguration sc = StorageConfiguration.loadConfiguration();
		ServiceUtilsConfig config= storageSetup.makeConfig(factory, sc);

		System.out.println("Configuration --- " + sc);

		PostService ps=new PostService(config);

		ps.addToConfiguration("storage.config", config.configuration);
		ps.addToConfiguration("cli.config", intF.getConfig());
		ps.addToConfiguration("version", Configuration.toolboxVersion);
		ps.addToConfiguration("long.version", Configuration.longToolboxVersion);


		singletons.add(ps);
		singletons.add(new TranslationService(ps));
		singletons.add(new SummaryService(ps));

		singletons.add(new VanillaDocumentMessageBodyWriter(new InteropFramework(factory)));
		singletons.add(new ScalaDocumentMessageBodyWriter());
		singletons.add(new Level0MessageBodyWriter());

        singletons.add(new OpenApiResource());
        // Jackson 3: the JSON provider uses a mapper with Jackson 2 defaults (docs/JACKSON3_MIGRATION.md, D2)
        singletons.add(new org.openprovenance.prov.service.core.rest.JsonMapperResolver());
        singletons.add(new AcceptHeaderOpenApiResource());

        singletons.add(new JacksonJsonProvider());

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
