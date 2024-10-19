package org.openprovenance.prov.service.metrics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.rules.SimpleMetrics;
import org.openprovenance.prov.scala.summary.TypePropagator;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.security.pac.SecurityConfiguration;
import org.openprovenance.prov.service.security.pac.Utils;
import org.openprovenance.prov.service.translation.TranslationService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.openprovenance.prov.interop.CommandLineArguments.METRICS;
import static org.openprovenance.prov.service.core.ServiceUtils.getSystemOrEnvironmentVariableOrDefault;

@Path("")
public class MetricsService extends TranslationService {
    static Logger logger = LogManager.getLogger(MetricsService.class);

    public static final String METRICS_SECURITY_CONFIG = "METRICS_SECURITY_CONFIG";
    public static final String NO_SECURITY_CONFIG = "no-security-config";
    public static final String metricsSecurityConfig =getSystemOrEnvironmentVariableOrDefault(METRICS_SECURITY_CONFIG, NO_SECURITY_CONFIG);
    public static final Utils secUtils=new Utils();
    public static final SecurityConfiguration securityConfiguration=(NO_SECURITY_CONFIG.equals(metricsSecurityConfig))?null:secUtils.readSecurityConfiguration(metricsSecurityConfig);
    public static final String POSTGRES_HOST = "POSTGRES_HOST";
    public static final String postgresHost=System.getProperty(POSTGRES_HOST, "localhost");
    public static final String DB_USER = "TPL_DB_USER";
    public static final String postgresUsername=getSystemOrEnvironmentVariableOrDefault(DB_USER, "user");
    public static final String DB_PASS = "TPL_DB_PASS";
    public static final String postgresPassword=getSystemOrEnvironmentVariableOrDefault(DB_PASS,"password");


    final MetricsCalculator metricsCalculator;
    private final PostService ps;
    private final ServiceUtils utils;
    private final Storage storage;
    private final MetricsQuery mq;

    public MetricsService(PostService ps) {
        super(ps);

        this.ps = ps;
        this.utils = ps.getServiceUtils();
        this.storage=new Storage();

        ps.addToConfiguration("security.config", securityConfiguration);
        Connection conn=storage.setup(postgresHost, postgresUsername, postgresPassword);

        if (conn!=null) {
            try {
                boolean anyResult=storage.initializeDB(conn);
                logger.info("DB initialized. Any results? " + anyResult);

                //sqlFilesToExecute.forEach(file -> executeStatementsFromFile(conn,file));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Querier querier=new Querier(storage, conn);
        this.mq=new MetricsQuery(querier);
        metricsCalculator = new MetricsCalculator(mq, ((ps instanceof MetricsPostService)?((MetricsPostService)ps).getSignatureService():null));
        setRulesFactory(() -> metricsCalculator);
        if (ps instanceof MetricsPostService) {
            ((MetricsPostService)ps).setRulesFactory(() -> metricsCalculator);
        }


    }


    @GET
    @Path(FRAGMENT_METRICS + "{id}.json")
    @Tag(name=METRICS)
    @Produces(MEDIA_APPLICATION_JSON)
    public Response getMetrics(@Context HttpServletResponse response,
                               @Context HttpServletRequest request,
                               @Parameter(name = "id", description = "id", required = true) @PathParam("id") String id) throws IOException {

        MetricsRecord metricsRecord = mq.getMetricsRecord(id);
        System.out.println("MetricsRecord: " + metricsRecord);
        StreamingOutput promise = out -> TypePropagator.om().writeValue  (out,metricsRecord);

        return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();
    }

    @GET
    @Path(FRAGMENT_PROVTYPEMAP + "/{depth}/{index}")
    @Tag(name=TYPES)
    @Produces(MEDIA_APPLICATION_JSON)
    @Operation(summary = "prov type for type coordinates",
            description = "ProvType as a JSON object. ",
            responses = { @ApiResponse(responseCode = "200", description = "Representation of provtype"),
                    @ApiResponse(responseCode = "404", description = "Type not found") })
    public Response getType(@Context HttpServletResponse response,
                            @Context HttpServletRequest request,
                            @Parameter(name = "depth", description = "depth", required = true) @PathParam("depth") int depth,
                            @Parameter(name = "index", description = "index", required = true) @PathParam("index") int index) throws IOException {

        StreamingOutput promise= out -> TypePropagator.om().writeValue(out, metricsCalculator.getType(depth, index));


        return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();


    }

    @GET
    @Path(FRAGMENT_PROVTYPEMAP)
    @Tag(name=TYPES)
    @Produces(MEDIA_APPLICATION_JSON)
    @Operation(summary = "prov type map",
            description = "Prov TypeMap as a JSON object. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Representation of typemap"),
                    @ApiResponse(responseCode = "404", description = "Type not found") })
    public Response getTypeMap(@Context HttpServletResponse response,
                               @Context HttpServletRequest request) throws IOException {

        StreamingOutput promise= out ->out.write(metricsCalculator.getTypeMap().getBytes());

        return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();


    }

    @GET
    @Path(FRAGMENT_PROVTYPEMAP + "/report.{ext}")
    @Tag(name=TYPES)
    @Produces({MEDIA_TEXT_CSV, MEDIA_APPLICATION_JSON})
    @Operation(summary = "prov type for type coordinates",
            description = "ProvType as a JSON object. ",
            responses = { @ApiResponse(responseCode = "200", description = "Representation of provtype"),
                    @ApiResponse(responseCode = "404", description = "Type not found") })
    public Response getTypeMapReport(@Context HttpServletResponse response,
                                     @Context HttpServletRequest request,
                                     @Parameter(name = "ext", description = "extension", required = true) @PathParam("ext") String extension) throws IOException {

        if ("csv".equals(extension)) {

            StreamingOutput promise = out -> {
                out.write(SimpleMetrics.headers().getBytes());
                out.write("\n".getBytes());
                metricsCalculator.getTypeMapReport().forEach(x -> {
                    try {
                        out.write(x.toCsv().getBytes());
                        out.write("\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            };


            return ServiceUtils.composeResponseOK(promise).type(MEDIA_TEXT_CSV).build();
        } else {
            StreamingOutput promise = out -> TypePropagator.om().writeValue(out, metricsCalculator.getTypeMapReport());
            return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();
        }


    }

    @GET
    @Path(FRAGMENT_PROVTYPEMAP + "/summary.json")
    @Tag(name=TYPES)
    @Produces({ MEDIA_APPLICATION_JSON})
    @Operation(summary = "prov type for type coordinates",
            description = "ProvType as a JSON object. ",
            responses = { @ApiResponse(responseCode = "200", description = "Representation of provtype"),
                    @ApiResponse(responseCode = "404", description = "Type not found") })
    public Response getTypeMapSummary(@Context HttpServletResponse response,
                                     @Context HttpServletRequest request) throws IOException {

        List<SimpleMetrics> report=metricsCalculator.getTypeMapReport();
        Map<String,List<Integer>> summary=new HashMap<>();
        summary.put("countEntities", report.stream().map(x -> x.countEntities).collect(Collectors.toList()));
        summary.put("countActivities", report.stream().map(x -> x.countActivities).collect(Collectors.toList()));
        summary.put("countAgents", report.stream().map(x -> x.countAgents).collect(Collectors.toList()));
        summary.put("countWasGeneratedBy", report.stream().map(x -> x.countWasGeneratedBy).collect(Collectors.toList()));
        summary.put("countUsed", report.stream().map(x -> x.countUsed).collect(Collectors.toList()));
        summary.put("countWasAssociatedWith", report.stream().map(x -> x.countWasAssociatedWith).collect(Collectors.toList()));
        summary.put("countWasAttributedTo", report.stream().map(x -> x.countWasAttributedTo).collect(Collectors.toList()));
        summary.put("countActedOnBehalfOf", report.stream().map(x -> x.countActedOnBehalfOf).collect(Collectors.toList()));
        summary.put("countWasDerivedFrom", report.stream().map(x -> x.countWasDerivedFrom).collect(Collectors.toList()));
        summary.put("countWasStartedBy", report.stream().map(x -> x.countWasStartedBy).collect(Collectors.toList()));
        summary.put("countWasEndedBy", report.stream().map(x -> x.countWasEndedBy).collect(Collectors.toList()));
        summary.put("countWasInformedBy", report.stream().map(x -> x.countWasInformedBy).collect(Collectors.toList()));
        summary.put("countWasInvalidatedBy", report.stream().map(x -> x.countWasInvalidatedBy).collect(Collectors.toList()));
        summary.put("countWasInfluencedBy", report.stream().map(x -> x.countWasInfluencedBy).collect(Collectors.toList()));
        summary.put("countSpecializationOf", report.stream().map(x -> x.countSpecializationOf).collect(Collectors.toList()));
        summary.put("countAlternateOf", report.stream().map(x -> x.countAlternateOf).collect(Collectors.toList()));
        summary.put("countHadMember", report.stream().map(x -> x.countHadMember).collect(Collectors.toList()));


        StreamingOutput promise = out -> TypePropagator.om().writeValue(out, summary);
        return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();



    }


}
