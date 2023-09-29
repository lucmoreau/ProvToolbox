
package org.openprovenance.prov.service.summary;

 
import java.util.HashSet;

import java.util.Set;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ApplicationPath;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;




//@ApplicationPath("view")
abstract public class ViewApplication extends Application {
	private Set<Object> singletons = new HashSet<>();
 
	public ViewApplication() {
		singletons.add(new ViewService());

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
