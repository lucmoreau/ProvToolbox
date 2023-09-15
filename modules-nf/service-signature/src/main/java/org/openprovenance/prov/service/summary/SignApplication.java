
package org.openprovenance.prov.service.summary;


import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.service.core.ByteArrayMessageBodyWriter;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtilsConfig;
import org.openprovenance.prov.service.signature.SignatureService;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

//import org.openprovenance.prov.service.core.JacksonJsonProvider;
//import org.openprovenance.prov.pservice.provider.List2MessageBodyWriter;
//import org.openprovenance.prov.pservice.provider.IncrementalDocumentMessageBodyWriter;

@ApplicationPath("/provapi")
public class SignApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();



	StorageConfiguration sc=new StorageConfiguration();


	public SignApplication() {

		org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();

		ServiceUtilsConfig config=sc.makeConfig(factory);


		PostService ps=new PostService(config);
		singletons.add(ps);
	    singletons.add(new SignatureService(ps));

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
