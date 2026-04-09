package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.interop.InteropMediaType;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.apache.logging.log4j.Logger;

import static org.openprovenance.prov.service.core.QueryConfiguration.*;
import static org.openprovenance.prov.service.core.ServiceUtils.getSystemOrEnvironmentVariableOrDefault;

@Path("")
public class QueryService {

    public static Logger logger = LogManager.getLogger(QueryService.class);
    private final PostService ps;
    private final TemplateQuery templateQuery;
    private QueryConfiguration configuration;
    private ServiceUtils utils;

    public static final String TPL_QUERY_CONFIG = "TPL_QUERY_CONFIG";
    public static final String NO_QUERY_CONFIG = "no-query-config";
    public static final String tplQueryConfig=getSystemOrEnvironmentVariableOrDefault(TPL_QUERY_CONFIG, NO_QUERY_CONFIG);

    private boolean enabled;

    public QueryService(PostService ps, TemplateQuery templateQuery) {
        this.ps=ps;
        this.templateQuery=templateQuery;
        if (tplQueryConfig.equals(NO_QUERY_CONFIG)) {
            this.configuration = DisabledConfig();
            this.enabled=false;

        } else {
            this.configuration = QueryConfiguration.newQueryConfiguration(tplQueryConfig) ;
            this.enabled=true;
        }
    }

    public void disable() {
        this.enabled=false;
    }




    @POST
    @Path("/query/configure")
    @Tag(name = "query")
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    @Produces({InteropMediaType.MEDIA_APPLICATION_JSON})
    public Response configure(@Context HttpServletRequest request,
                              @Context HttpHeaders headers,
                              QueryConfiguration configuration) {
        if (enabled) {
            this.configuration = newQueryConfiguration(configuration);
        } else {
            this.configuration = DisabledConfig();
        }
        return ServiceUtils
                .composeResponseOK(this.configuration)
                .type(InteropMediaType.MEDIA_APPLICATION_JSON)
                .build();
    }


    @POST
    @Path("/query/{query}/post")
    @Tag(name = "query")
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    @Produces({InteropMediaType.MEDIA_APPLICATION_JSON, InteropMediaType.MEDIA_TEXT_PLAIN})
    public Response doQuery(@Context HttpServletRequest request,
                            @Context HttpHeaders headers,
                            @Parameter(name = "query", description = "query name", required = true) @PathParam("query") String query,
                            Object parameters) {

        logger.debug("doQuery " + query + " with parameters: " + parameters);
        if (!enabled  || !this.configuration.enableExternalQueries) {
            return ServiceUtils.composeResponseNotFOUND("Query service is disabled");
        }

        Optional<String> sql = Optional.empty();
        // find file query.sql in query configuration path
        for (String path : this.configuration.externalQueryPath) {
            if (path == null) continue;
            File f = new File(path, query + ".sql");
            if (f.exists() && f.isFile()) {
                try {
                    sql = Optional.of(Files.readString(f.toPath()));
                    break;
                } catch (IOException e) {
                    logger.warn("Failed to read query file: " + f.getAbsolutePath() + "\n" + e.getMessage());
                    // continue with rest of path
                }
            }
        }
        if (sql.isEmpty()) {
            logger.info("No external query found for query: " + query);
            return ServiceUtils.composeResponseNotFOUND("Query not found: " + query + ".sql");
        } else {
            String theQuery=sql.get();
            logger.info("Query: " + theQuery);
            List<Object[]> resultRecords = templateQuery.queryFromSql(theQuery, true);
            StreamingOutput promise = (out) -> new ObjectMapper().writeValue(out, resultRecords);

            return ServiceUtils
                    .composeResponseOK(promise)
                    .type(InteropMediaType.MEDIA_APPLICATION_JSON)
                    .build();
        }
    }


    @POST
    @Path("/query.plot/{query}/post")
    @Tag(name = "query")
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    @Produces({InteropMediaType.MEDIA_APPLICATION_JSON})
    public Response doQueryPlot(@Context HttpServletRequest request,
                                @Context HttpHeaders headers,
                                @Parameter(name = "query", description = "query name", required = true) @PathParam("query") String query,
                                Object parameters) {

        logger.debug("doQueryPlot " + query + " with parameters: " + parameters);
        if (!enabled  || !this.configuration.enableExternalQueries) {
            return ServiceUtils.composeResponseNotFOUND("Query service is disabled");
        }

        Optional<String> json = Optional.empty();
        // find file query.sql in query configuration path
        for (String path : this.configuration.externalQueryPath) {
            if (path == null) continue;
            File f = new File(path, query + ".plotly.json");
            if (f.exists() && f.isFile()) {
                try {
                    json = Optional.of(Files.readString(f.toPath()));
                    break;
                } catch (IOException e) {
                    logger.warn("Failed to read query file: " + f.getAbsolutePath() + "\n" + e.getMessage());
                    // continue with rest of path
                }
            }
        }
        if (json.isEmpty()) {
            logger.info("No external plotly json file found for query: " + query);
            return ServiceUtils.composeResponseNotFOUND("No external plotly json file found for query: " + query );
        } else {
            String thePlotConfig=json.get();
            // not trying to parse it, just pass it ons
            return ServiceUtils
                    .composeResponseOK(thePlotConfig)
                    .type(InteropMediaType.MEDIA_APPLICATION_JSON)
                    .build();
        }
    }




    @Path("/query/list-of-queries")
    @GET
    @Produces({InteropMediaType.MEDIA_APPLICATION_JSON})
    public Response getListOfQueries() {
        logger.info("getListOfQueries");
        if (!enabled  || !this.configuration.enableExternalQueries) {
            return ServiceUtils.composeResponseOK("[]").build();
        }
        // find files *.sql in query configuration path
        List<String> queries = new java.util.ArrayList<>();
        for (String path : this.configuration.externalQueryPath) {
            if (path == null) continue;
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
                if (files != null) {
                    for (File f : files) {
                        String name = f.getName();
                        if (name.endsWith(".sql")) {
                            queries.add(name.substring(0, name.length() - 4));
                        }
                    }
                }
            }
        }
        StreamingOutput promise = (out) -> new ObjectMapper().writeValue(out, queries);
        return  ServiceUtils.composeResponseOK(promise).build();
    }


}
