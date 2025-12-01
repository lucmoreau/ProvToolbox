
package org.openprovenance.prov.service.summary;


import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.core.config.StorageConfiguration;
import org.openprovenance.prov.service.core.writers.ByteArrayMessageBodyWriter;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.signature.SignatureService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.openprovenance.prov.service.translation.TranslationService;
import org.openprovenance.prov.service.translation.storage.StorageSetup;

import java.util.HashSet;
import java.util.Set;

import static org.openprovenance.prov.model.interop.ApiUriFragments.FRAGMENT_PROVAPI;

//import org.openprovenance.prov.service.core.JacksonJsonProvider;
//import org.openprovenance.prov.pservice.provider.List2MessageBodyWriter;
//import org.openprovenance.prov.pservice.provider.IncrementalDocumentMessageBodyWriter;

@ApplicationPath(FRAGMENT_PROVAPI)
public class SignApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();



	StorageConfiguration sc= StorageConfiguration.loadConfiguration();
    public final StorageSetup storageSetup = new StorageSetup();


	public SignApplication() {

		org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();

        ServiceUtilsConfig config= storageSetup.makeConfig(factory,sc);


		PostService ps=new PostService(config);
		singletons.add(ps);
        singletons.add(new TranslationService(ps));
	    singletons.add(new SignatureService(ps));

        singletons.add(new VanillaDocumentMessageBodyWriter(new InteropFramework()));
        singletons.add(new NFXMLDocumentMessageBodyWriter());
        singletons.add(new ByteArrayMessageBodyWriter());
        singletons.add(new OpenApiResource());
        singletons.add(new AcceptHeaderOpenApiResource());

     //   singletons.add(new JacksonJsonProvider());

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
