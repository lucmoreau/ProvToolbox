
package org.openprovenance.prov.service;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.openprovenance.prov.service.core.SwaggerTags;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/static")
public class StaticApplication extends Application implements SwaggerTags {
	private final Set<Object> singletons = new HashSet<>();

	public StaticApplication() {

		singletons.add(new StaticService());


		CorsFilter corsFilter = new CorsFilter();
		corsFilter.getAllowedOrigins().add("*");
		corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
		singletons.add(corsFilter);

	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}


	@Path("")
	@Tag(name=STATIC)
	public static class StaticService {
		@GET
		@Path("{path:.*}")
		@Operation(summary = "static resource entry point")
		public void staticResource(@Context HttpServletResponse response,
								   @Context HttpServletRequest request,
								   @PathParam("path")  String path)
				throws ServletException, IOException {
			request.getRequestDispatcher("/" + path).forward(request, response);
		}

	}
}
