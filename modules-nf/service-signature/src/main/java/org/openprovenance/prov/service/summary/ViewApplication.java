
package org.openprovenance.prov.service.summary;


import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;




//@ WebFilter(asyncSupported = true, urlPatterns = { "/*" })
@ApplicationPath("view")
public class ViewApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
 
	public ViewApplication() {
		//singletons.add(new ValidationService());  // this includes TranslationService
		//singletons.add(new TemplateManagementService());
		singletons.add(new ViewService());
		//singletons.add(new io.swagger.jaxrs.listing.SwaggerSerializers());
		//singletons.add(new io.swagger.jaxrs.listing.ApiListingResource());
		
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
