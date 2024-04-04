package org.openprovenance.prov.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.iobean.composite.SqlCompositeBeanEnactor3;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openprovenance.prov.service.Storage.getStringFromClasspath;
import static org.openprovenance.prov.template.library.plead.logger.Logger.initializeBeanTable;

@Path("")
public class TemplateService {
    static Logger logger = LogManager.getLogger(TemplateService.class);
    public static final String APPLICATION_VND_KCL_PROV_TEMPLATE_JSON = "application/vnd.kcl.prov-template+json";
    private final PostService ps;
    private final ServiceUtils utils;
    private final TemplatesDispatcher templateDispatcher;
    private final ObjectMapper om;
    private final Storage storage;
 //   private final Querier querier;
  //  private final QueryTemplate queryTemplate;
    final ProvFactory pf;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    public static final String EMS_HOST = "EMS_HOST";
    public static final String emsHost=getSystemOrEnvironmentVariableOrDefault(EMS_HOST, "http://localhost:8080/ems");
    public static final String EMS_API = "EMS_API";
    public static final String emsAPI=getSystemOrEnvironmentVariableOrDefault(EMS_API, "http://localhost:8080/ems/provapi");
    public static final String POSTGRES_HOST = "POSTGRES_HOST";
    public static final String postgresHost=System.getProperty(POSTGRES_HOST, "localhost");
    public static final String SUST_DB_USER = "SUST_DB_USER";
    public static final String postgresUsername=getSystemOrEnvironmentVariableOrDefault(SUST_DB_USER, "user");
    public static final String SUST_DB_PASS = "SUST_DB_PASS";
    public static final String postgresPassword=getSystemOrEnvironmentVariableOrDefault(SUST_DB_PASS,"password");
    private final TemplateLogic templateLogic;
    private final SqlCompositeBeanEnactor3 sqlCompositeBeanEnactor3;

    static final String getSystemOrEnvironmentVariableOrDefault(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null) {
            value = System.getenv(name);
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    static final List<String> sqlFilesToExecute = List.of("/utils.sql");

    public TemplateService(PostService ps) {
        this.pf = new ProvFactory();

        this.ps = ps;
        this.utils = ps.getServiceUtils();
        this.storage=new Storage();

        Connection conn=storage.setup(postgresHost, postgresUsername, postgresPassword);
        this.templateDispatcher=new TemplatesDispatcher(storage,conn);
       // this.querier =new Querier(storage,conn);
       // this.spreadsheetExporter=new SpreadsheetExporter(querier);
        this.om=new ObjectMapper();
        this.om.enable(SerializationFeature.INDENT_OUTPUT);
        this.om.registerModule(new JavaTimeModule());
//this.queryTemplate=new QueryTemplate(querier);

        this.sqlCompositeBeanEnactor3 =new SqlCompositeBeanEnactor3(storage,conn);


        if (conn!=null) {
            try {
                boolean anyResult=storage.initializeDB(conn);
                logger.info("DB initialized. Any results? " + anyResult);

                sqlFilesToExecute.forEach(file -> executeStatementsFromFile(conn,file));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        HashMap<String,String> map=new HashMap<>() {{
            put("EMS_HOST",emsHost);
            put("EMS_API",emsAPI);
        }};
        this.documentBuilderDispatcher=initializeBeanTable(new org.openprovenance.prov.template.library.plead.configurator.TableConfiguratorWithMap(map,pf));
        this.templateLogic=new TemplateLogic(pf,null,templateDispatcher,null,documentBuilderDispatcher, utils,om, sqlCompositeBeanEnactor3);
    }

    private void executeStatementsFromFile(Connection conn, String filename) {
        boolean anyResult;
        String statements=getStringFromClasspath(this.getClass(), filename);
        try {
            anyResult=storage.executeStatements(conn,statements);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.info("DB processing file " + filename + ". Any results? " + anyResult);
    }


    @POST
    @Path("/statements")
    @Tag(name = "ems_s")
    @Consumes({InteropMediaType.MEDIA_TEXT_CSV, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON})
    @Produces({APPLICATION_VND_KCL_PROV_TEMPLATE_JSON}) //InteropMediaType.MEDIA_TEXT_CSV,
    public Response submitStatements(@Context HttpServletResponse response,
                                      @Context HttpServletRequest request,
                                      @Context HttpHeaders headers,
                                      @Context UriInfo uriInfo,
                                     // @Parameter(name = "id", description = "session id", required = true) @PathParam("id") String sessionUUID,
                                      JsonOrCsv documentOrCsv) {
        Principal principal = request.getUserPrincipal();
        logger.debug("post statements id: principal " + principal);

        if (documentOrCsv==null) {
            return utils.composeResponseInternalServerError("null document", new NullPointerException());
        }
        if (documentOrCsv.csv!=null) {
            return processIncomingCsv(documentOrCsv.csv);
        } else if (documentOrCsv.json!=null) {
            return templateLogic.processIncomingJson(documentOrCsv.json);
        } else {
            return utils.composeResponseInternalServerError("unknown input document", new UnsupportedOperationException());
        }
    }

    private void debugDisplay(String msg, Object object) {
        try {
            System.out.println(msg + om.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }



    public Response processIncomingCsv(CSVParser csv) {
        return ps.getServiceUtils().composeResponseInternalServerError("not supported csv processing", new UnsupportedOperationException());
    }





    private InputStream getPart(List<InputPart> inputParts) {
        if (inputParts==null) return null;
        for (InputPart inputPart : inputParts) {
            try {
                //Use this header for extra processing if required
                @SuppressWarnings("unused")
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                // convert the uploaded file to inputstream
                return inputPart.getBody(InputStream.class, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private String getPartAsString(List<InputPart> inputParts) {
        if (inputParts==null) return null;
        for (InputPart inputPart : inputParts) {
            try {
                //Use this header for extra processing if required
                @SuppressWarnings("unused")
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                // convert the uploaded file to inputstream
                return inputPart.getBodyAsString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
