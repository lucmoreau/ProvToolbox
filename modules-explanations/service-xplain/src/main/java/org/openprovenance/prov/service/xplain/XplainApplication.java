
package org.openprovenance.prov.service.xplain;


import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.*;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.service.core.readers.VanillaDocumentMessageBodyReader;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.summary.writers.ScalaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.translation.TranslationService;

import jakarta.ws.rs.core.Application;
import org.openprovenance.prov.service.translation.storage.StorageSetup;
import org.openprovenance.prov.service.xplain.readers.PhraseEnvironmentReader;
import org.openprovenance.prov.service.xplain.readers.PhraseReader;
import org.openprovenance.prov.service.xplain.readers.PhraseReader2;
import org.openprovenance.prov.service.xplain.readers.TemplateAndProfileReader;
import org.openprovenance.prov.service.xplain.writers.NarrativeWriter;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/provapi")
public class XplainApplication extends Application {
	private final Set<Object> singletons = new HashSet<>();

    StorageConfiguration sc= StorageConfiguration.loadConfiguration();
    public final StorageSetup storageSetup = new StorageSetup();



    public XplainApplication() {

		InteropFramework intF=new InteropFramework();
		org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();
        ServiceUtilsConfig config= storageSetup.makeConfig(factory,sc);



		PostService ps=new PostService(config);
		ps.addToConfiguration("storage.config", config.configuration);
		ps.addToConfiguration("cli.config", intF.getConfig());
		ps.addToConfiguration("version", Configuration.toolboxVersion);
		ps.addToConfiguration("long.version", Configuration.longToolboxVersion);


		singletons.add(ps);
		singletons.add(new NarrativeService(ps));
		singletons.add(new TranslationService(ps));

		singletons.add(new VanillaDocumentMessageBodyWriter(new InteropFramework(factory)));
		singletons.add(new VanillaDocumentMessageBodyReader(factory));
		singletons.add(new ScalaDocumentMessageBodyWriter());
		singletons.add(new NarrativeWriter());

		singletons.add(new OpenApiResource());
		singletons.add(new AcceptHeaderOpenApiResource());

		singletons.add(new TemplateAndProfileReader());
		singletons.add(new PhraseReader());
		singletons.add(new PhraseReader2());
		singletons.add(new PhraseEnvironmentReader());
		singletons.add(new NlgService(ps));

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
