
package org.openprovenance.prov.service.summary;


import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.summary.writers.Level0MessageBodyWriter;
import org.openprovenance.prov.service.summary.writers.ScalaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.translation.TranslationService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.openprovenance.prov.service.translation.storage.StorageConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("/provapi")
public class SummariseApplication extends Application {
	private final Set<Object> singletons = new HashSet<>();


	public SummariseApplication() {
		InteropFramework intF=new InteropFramework();
		org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();

		StorageConfiguration sc = new MyStorageConfiguration();
		ServiceUtilsConfig config= sc.makeConfig(factory);

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


	public static class MyStorageConfiguration extends StorageConfiguration {
		@Override
		public Map<String,String> theDefaultConfiguration() {
			Map<String,String> config=super.theDefaultConfiguration();
			// not ready for redis, as extension field is being used
			config.put(PSERVICE_INDEX,      "fs");
			return config;
		}

	}
}
