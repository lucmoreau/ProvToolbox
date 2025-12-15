package org.openprovenance.prov.template.service;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.template.library.plead.CatalogueDispatcher;

import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;


@Path("")
public class TemplateService extends org.openprovenance.prov.service.core.TemplateService {
    static Logger logger = LogManager.getLogger(TemplateService.class);
    private final String jdbcURL;


    public TemplateService(PostService ps) {
        super(ps);

        ps.addToConfiguration("security.config", securityConfiguration);

        String sqlInitializer=templateConfiguration.get("sql.initializer");
        String jdbcURL=templateConfiguration.get("jdbc.url");

        this.jdbcURL=jdbcURL;


    }

    @Override
    public Connection storageSetup(String jdbcURL) {
        return storage.setup(jdbcURL, postgresUsername, postgresPassword);
    }


    @Override
    public CatalogueDispatcherInterface<FileBuilder> getCatalogueDispatcher(BiFunction<Object, ProvFactory, CatalogueDispatcherInterface> factory, HashMap<String, String> map, Function<String, ResultSet> queryExecutor) {
        // hard coded catalogue here
        CatalogueDispatcherInterface<FileBuilder> catalogueDispatcher=new CatalogueDispatcher(map,pf);
        catalogueDispatcher.initEnactorConverter(queryExecutor,this::submitPostProcessing, principalManager::getPrincipal);
        catalogueDispatcher.initCompositeEnactorConverter(queryExecutor,this::submitPostProcessing, principalManager::getPrincipal);
        return catalogueDispatcher;
    }


    @Override
    public HashMap<String, String> createMapForVariableSubstitution() {
        return new HashMap<>() {{
            put("PROV_HOST", provHost);
            put("PROV_API", provAPI);
        }};
    }

}
