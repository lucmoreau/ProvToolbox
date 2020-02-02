
package org.openprovenance.prov.service.translator;


import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/view")
public class ViewApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
 
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
