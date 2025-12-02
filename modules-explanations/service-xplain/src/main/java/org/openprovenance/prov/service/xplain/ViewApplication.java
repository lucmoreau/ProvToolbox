package org.openprovenance.prov.service.xplain;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/view")
public class ViewApplication extends Application {
    private final Set<Object> singletons = new HashSet<Object>();

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


