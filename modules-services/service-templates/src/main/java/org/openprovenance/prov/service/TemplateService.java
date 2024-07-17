package org.openprovenance.prov.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.readers.*;

import org.openprovenance.prov.service.security.pac.RoleAuthorizationGenerator;
import org.openprovenance.prov.service.security.pac.SecurityConfiguration;
import org.openprovenance.prov.service.security.pac.Utils;
import org.openprovenance.prov.template.library.plead.configurator.TableConfiguratorForTypesWithMap;
import org.openprovenance.prov.template.library.plead.sql.access_control.SqlCompositeBeanEnactor4;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.vanilla.ProvUtilities;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.profile.BasicUserProfile;
import org.pac4j.core.profile.Pac4JPrincipal;
import org.pac4j.core.profile.UserProfile;

import java.io.*;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import static org.openprovenance.prov.interop.InteropMediaType.MEDIA_IMAGE_SVG_XML;
import static org.openprovenance.prov.interop.InteropMediaType.MEDIA_TEXT_HTML;
import static org.openprovenance.prov.service.Storage.getStringFromClasspath;
import static org.openprovenance.prov.service.core.ServiceUtils.getSystemOrEnvironmentVariableOrDefault;
import static org.openprovenance.prov.template.library.plead.client.logger.Logger.initializeBeanTable;
import static org.openprovenance.prov.template.library.plead.sql.integration.BeanEnactor2WithPrincipal.setPrincipal;

@Path("")
public class TemplateService {
    static Logger logger = LogManager.getLogger(TemplateService.class);
    public static final String APPLICATION_VND_KCL_PROV_TEMPLATE_JSON = "application/vnd.kcl.prov-template+json";
    private final PostService ps;
    private final ServiceUtils utils;
    private final TemplateDispatcher templateDispatcher;
    private final ObjectMapper om;
    private final Storage storage;
    final ProvFactory pf;
    private final Map<String, FileBuilder> documentBuilderDispatcher;
    public static final String TPL_HOST = "TPL_HOST";
    public static final String provHost =getSystemOrEnvironmentVariableOrDefault(TPL_HOST, "http://localhost:8080/ems");
    public static final String PROV_API = "PROV_API";
    public static final String provAPI =getSystemOrEnvironmentVariableOrDefault(PROV_API, "http://localhost:8080/ems/provapi");
    public static final String POSTGRES_HOST = "POSTGRES_HOST";
    public static final String postgresHost=System.getProperty(POSTGRES_HOST, "localhost");
    public static final String DB_USER = "TPL_DB_USER";
    public static final String postgresUsername=getSystemOrEnvironmentVariableOrDefault(DB_USER, "user");
    public static final String DB_PASS = "TPL_DB_PASS";
    public static final String postgresPassword=getSystemOrEnvironmentVariableOrDefault(DB_PASS,"password");

    //public static final String TPL_KEYCLOAK = "TPL_KEYCLOAK";
    //public static final String tplKeycloak=getSystemOrEnvironmentVariableOrDefault(TPL_KEYCLOAK, "http://localhost:8080/auth/realms/ems");
    //public static final String TPL_KEYCLOAK_BASEURI = "TPL_KEYCLOAK_BASEURI";
    //public static final String tplKeycloakBaseuri=getSystemOrEnvironmentVariableOrDefault(TPL_KEYCLOAK_BASEURI, "http://localhost:8080/auth");
    //public static final String TPL_KEYCLOAK_USERNAME = "TPL_KEYCLOAK_USERNAME";
    //public static final String tplKeycloakUsername=getSystemOrEnvironmentVariableOrDefault(TPL_KEYCLOAK_USERNAME, "user");
    //public static final String TPL_KEYCLOAK_CLIENTID = "TPL_KEYCLOAK_CLIENTID";
    //public static final String tplKeycloakClientId=getSystemOrEnvironmentVariableOrDefault(TPL_KEYCLOAK_CLIENTID, "clientid");
    //public static final String TPL_KEYCLOAK_CLIENTID_DIRECT = "TPL_KEYCLOAK_CLIENTID_DIRECT";
    //public static final String getTplKeycloakClientidDirect=getSystemOrEnvironmentVariableOrDefault(TPL_KEYCLOAK_CLIENTID_DIRECT, "clientid-direct");
    //public static final String TPL_KEYCLOAK_REALM = "TPL_KEYCLOAK_REALM";
    //public static final String tplKeycloakRealm=getSystemOrEnvironmentVariableOrDefault(TPL_KEYCLOAK_REALM, "realm");

    public static final String TPL_SECURITY_CONFIG = "TPL_SECURITY_CONFIG";
    public static final String NO_SECURITY_CONFIG = "no-security-config";
    public static final String tplSecurityConfig=getSystemOrEnvironmentVariableOrDefault(TPL_SECURITY_CONFIG, NO_SECURITY_CONFIG);
    public static final Utils secUtils=new Utils();
    public static final SecurityConfiguration securityConfiguration=(NO_SECURITY_CONFIG.equals(tplSecurityConfig))?null:secUtils.readSecurityConfiguration(tplSecurityConfig);


    private final TemplateLogic templateLogic;
    private final SqlCompositeBeanEnactor4 sqlCompositeBeanEnactor4;

    private final Querier querier;
    private final TemplateQuery queryTemplate;
    private final Map<String, Linker> compositeLinker;
    private final Map<String, Map<String, Set<String>>> typeAssignment;
    private final Map<String, Function<Object[], Object[]>> recordMaker;




    static final List<String> sqlFilesToExecute = List.of("/utils.sql");

    public static class Linker {
        public final String table;
        public final String linked;

        public Linker(String table, String linked) {
            this.table = table;
            this.linked = linked;
        }
    }

    public TemplateService(PostService ps) {
        this.pf = new ProvFactory();

        this.ps = ps;
        this.utils = ps.getServiceUtils();
        this.storage=new Storage();

        ps.addToConfiguration("security.config", securityConfiguration);

        Connection conn=storage.setup(postgresHost, postgresUsername, postgresPassword);
        this.templateDispatcher=new TemplateDispatcher(storage,conn);
        this.querier =new Querier(storage,conn);
       // this.spreadsheetExporter=new SpreadsheetExporter(querier);
        this.om=new ObjectMapper();
        this.om.enable(SerializationFeature.INDENT_OUTPUT);
        this.om.registerModule(new JavaTimeModule());

        this.sqlCompositeBeanEnactor4 =new SqlCompositeBeanEnactor4(storage.getQuerier(conn), templateDispatcher.postProcessing);

        this.compositeLinker=new HashMap<>() {{
            put("plead_transforming_composite", new Linker("plead_transforming_composite_linker", "plead_transforming"));
        }};



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
            put("PROV_HOST", provHost);
            put("PROV_API", provAPI);
        }};
        this.documentBuilderDispatcher=initializeBeanTable(new org.openprovenance.prov.template.library.plead.configurator.TableConfiguratorWithMap(map,pf));
        this.queryTemplate=new TemplateQuery(querier, templateDispatcher, compositeLinker, om, documentBuilderDispatcher, org.openprovenance.prov.template.library.plead.client.logger.Logger.ioMap);
        this.typeAssignment = initializeBeanTable(new TableConfiguratorForTypesWithMap(new HashMap<>(),templateDispatcher.getPropertyOrder(),this.documentBuilderDispatcher,null));
        this.recordMaker=initializeBeanTable(new TableConfiguratorForObjectRecordMaker(documentBuilderDispatcher));

        this.templateLogic=new TemplateLogic(pf,queryTemplate,templateDispatcher,null,documentBuilderDispatcher, utils,om, sqlCompositeBeanEnactor4, this.typeAssignment);


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
    @Tag(name = "template")
    @Consumes({InteropMediaType.MEDIA_TEXT_CSV, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON})
    @Produces({InteropMediaType.MEDIA_TEXT_CSV, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON}) //InteropMediaType.MEDIA_TEXT_CSV,
    public Response submitStatements(@Context HttpServletRequest request,
                                      @Context HttpHeaders headers,
                                      @Context UriInfo uriInfo,
                                     // @Parameter(name = "id", description = "session id", required = true) @PathParam("id") String sessionUUID,
                                      JsonOrCsv documentOrCsv) {
        if (documentOrCsv==null) {
            return utils.composeResponseInternalServerError("null document", new NullPointerException());
        }

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);
        // set thread specific variable
        setPrincipal(principalAsPreferredUsername);

        logger.info("post statements id: principal " + principal);


        List<Object> result;
        if (documentOrCsv.csv!=null) {
            result=templateLogic.processIncomingCsv(documentOrCsv.csv);
        } else if (documentOrCsv.json!=null) {
            result=templateLogic.processIncomingJson(documentOrCsv.json);
        } else {
            return utils.composeResponseInternalServerError("unknown input document", new UnsupportedOperationException());
        }
        switch (request.getHeader(HttpHeaders.ACCEPT).toLowerCase()) {
            case InteropMediaType.MEDIA_TEXT_CSV:
                return ServiceUtils.composeResponseOK(templateLogic.streamOutRecordsToCSV(result)).type(InteropMediaType.MEDIA_TEXT_CSV).build();
            case APPLICATION_VND_KCL_PROV_TEMPLATE_JSON:
                StreamingOutput promise= out -> om.writeValue(out,result);
                return ServiceUtils.composeResponseOK(promise).type(APPLICATION_VND_KCL_PROV_TEMPLATE_JSON).build();
        }
        return utils.composeResponseBadRequest("unknown accept header " + request.getHeader(HttpHeaders.ACCEPT), new UnsupportedOperationException(request.getHeader(HttpHeaders.ACCEPT)));
    }

    @GET
    @Path("/template/{template}/{id}.{extension}")
    @Tag(name = "template")
    @Produces({ InteropMediaType.MEDIA_APPLICATION_JSONLD, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_IMAGE_SVG_XML })
    public Response getTemplateInstanceWithId(@Context HttpServletResponse response,
                                                 @Context HttpServletRequest request,
                                                 @Context HttpHeaders headers,
                                                 @Context UriInfo uriInfo,

                                                 @Parameter(name = "template", description = "template name", required = true) @PathParam("template") String template,
                                                 @Parameter(name = "id", description = "record id", required = true) @PathParam("id") Integer id,
                                                 @Parameter(name = "extension", description = "extension", required = true) @PathParam("extension") String extension) {

        logger.info("getTemplateInstanceWithId " + template + " " + id + " " + extension);

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);


        List<Object[]> records = queryTemplate.query(template, id, false, principalAsPreferredUsername);
        debugDisplay("records.size ", records.size());

        Document result=queryTemplate.constructDocument(documentBuilderDispatcher,records);

        switch (extension) {
            case "jsonld":
                return ServiceUtils.composeResponseOK(result).type(InteropMediaType.MEDIA_APPLICATION_JSONLD).build();
            case "provn":
                return ServiceUtils.composeResponseOK(result).type(InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION).build();
            case "svg":
                return ServiceUtils.composeResponseOK(result).type(MEDIA_IMAGE_SVG_XML).build();

        }
        return utils.composeResponseBadRequest("unknown extension " + extension, new UnsupportedOperationException(extension));
    }

    public String getPrincipalAsPreferredUsername(Principal principal) {
        String principalAsPreferredUsername= principal.getName();
        UserProfile profile = RoleAuthorizationGenerator.getProfile(principal.getName());
        if (profile!=null) {
            String tmp= (String) profile.getAttribute("preferred_username");
            if (tmp!=null) principalAsPreferredUsername=tmp;
        }
        return principalAsPreferredUsername;
    }


    @GET
    @Path("/template/{template}/{id}/{variable:\\w+}{extension:(\\.\\w+)?}")
    @Tag(name = "template")
    @Produces({ InteropMediaType.MEDIA_APPLICATION_JSONLD, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_IMAGE_SVG_XML })
    public Response getTemplatePropertyInstanceWithId(@Context HttpServletResponse response,
                                                      @Context HttpServletRequest request,
                                                      @Context HttpHeaders headers,
                                                      @Context UriInfo uriInfo,
                                                      @Parameter(name = "template", description = "template name", required = true) @PathParam("template") String template,
                                                      @Parameter(name = "id", description = "record id", required = true) @PathParam("id") Integer id,
                                                      @Parameter(name = "variable", description = "variable", required = true) @PathParam("variable") String variable,
                                                      @Parameter(name = "extension", description = "extension", required = true) @PathParam("extension") String extension) {

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);


        logger.info("getTemplatePropertyInstanceWithId " + template + " " + id + " " + variable);

        List<Object[]> records = queryTemplate.query(template, id, false, principalAsPreferredUsername);
        //debugDisplay("records.size ", records.size());

        Document result=queryTemplate.constructDocument(documentBuilderDispatcher,records);


        List<Object> selections=new LinkedList<>();
        for (Object[] record: records) {
            int index=java.util.Arrays.asList(templateDispatcher.getPropertyOrder().get(template)).indexOf(variable);
            Object[] objectRecord=recordMaker.get(template).apply(record);
            selections.add(objectRecord[index]);
        }

        Document newDoc=pf.newDocument();
        newDoc.setNamespace(result.getNamespace());
        for (Object selection: selections) {
            QualifiedName qn=(QualifiedName)selection;
            new ProvUtilities().getEntity(result).stream().filter(e -> qn.getUri().equals(e.getId().getUri())).forEach(e -> newDoc.getStatementOrBundle().add(e));
            new ProvUtilities().getAgent(result).stream().filter(e -> qn.getUri().equals(e.getId().getUri())).forEach(e -> newDoc.getStatementOrBundle().add(e));
            new ProvUtilities().getActivity(result).stream().filter(e -> qn.getUri().equals(e.getId().getUri())).forEach(e -> newDoc.getStatementOrBundle().add(e));
        }

        System.out.println("selections " + selections);


        extension = determineOptionalExtension(headers, extension);


        switch (extension) {
            case "jsonld":
                return ServiceUtils.composeResponseOK(newDoc).type(InteropMediaType.MEDIA_APPLICATION_JSONLD).build();
            case "provn":
                return ServiceUtils.composeResponseOK(newDoc).type(InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION).build();
            case "svg":
                return ServiceUtils.composeResponseOK(newDoc).type(MEDIA_IMAGE_SVG_XML).build();

        }
        return utils.composeResponseBadRequest("unknown extension " + extension, new UnsupportedOperationException(extension));
    }


    private void debugDisplay(String msg, Object object) {
        try {
            System.out.println(msg + om.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




    @POST
    @Path("/templates.{extension}")
    @Tag(name = "template")
    @Produces({ InteropMediaType.MEDIA_APPLICATION_JSONLD, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_IMAGE_SVG_XML })
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    public Response getTemplates(@Context HttpServletResponse response,
                                 @Context HttpServletRequest request,
                                 @Context HttpHeaders headers,
                                 @Context UriInfo uriInfo,
                                 @Parameter(name = "extension", description = "extension", required = true) @PathParam("extension") String extension,
                                 TableKeyList tableKey) {

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);

        List<Object[]> records = queryTemplate.queryTemplates(tableKey, false, principalAsPreferredUsername);

        Document result=queryTemplate.constructDocument(documentBuilderDispatcher,records);

        switch (extension) {
            case "jsonld":
                return ServiceUtils.composeResponseOK(result).type(InteropMediaType.MEDIA_APPLICATION_JSONLD).build();
            case "provn":
                return ServiceUtils.composeResponseOK(result).type(InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION).build();
            case "svg":
                return ServiceUtils.composeResponseOK(result).type(MEDIA_IMAGE_SVG_XML).build();
        }
        return utils.composeResponseBadRequest("unknown extension " + extension, new UnsupportedOperationException(extension));


    }

    @POST
    @Path("/templates/records")
    @Tag(name = "template")
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    @Produces({InteropMediaType.MEDIA_TEXT_CSV})
    public Response getTemplatesRecords(@Context HttpServletResponse response,
                                        @Context HttpServletRequest request,
                                        @Context HttpHeaders headers,
                                        @Context UriInfo uriInfo,
                                        SearchConfig searchConfig) {

        logger.info("getTemplatesRecords " + searchConfig);

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);


        List<TemplateQuery.RecordEntry2> records = queryTemplate.queryTemplatesRecords(searchConfig, principalAsPreferredUsername);

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader();
        StreamingOutput promise= out -> {
            OutputStreamWriter out2 = new OutputStreamWriter(out);
            try {
                CSVPrinter csvFilePrinter = new CSVPrinter(out2, csvFileFormat);
                csvFilePrinter.printComment("Generated by TemplateService");
                csvFilePrinter.printRecord("id", "created_at", "base_relation", "table_name", "key", "principal", "authorized");

                for (TemplateQuery.RecordEntry2 record : records) {
                    csvFilePrinter.printRecord(record.id,  record.created_at, record.base_relation, record.table_name, record.key,record.principal, record.authorized);
                }
                csvFilePrinter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        return ServiceUtils.composeResponseOK(promise).type(InteropMediaType.MEDIA_TEXT_CSV).build();

    }


    @POST
    @Path("/templates/viz")
    @Tag(name = "template")
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    @Produces({MEDIA_IMAGE_SVG_XML})
    public Response getTemplatesViz(@Context HttpServletResponse response,
                                    @Context HttpServletRequest request,
                                    @Context HttpHeaders headers,
                                    @Context UriInfo uriInfo,
                                    TemplatesVizConfig config) {

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);


        StreamingOutput promise= out -> templateLogic.generateViz(config, principalAsPreferredUsername, out);

        return ServiceUtils.composeResponseOK(promise).type(InteropMediaType.MEDIA_IMAGE_SVG_XML).build();

    }

    @GET
    @Path("/live/{relation}/{id:\\d+}{extension:(\\.\\w+)?}")
    @Tag(name = "template")
    @Produces({ InteropMediaType.MEDIA_APPLICATION_JSONLD, InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_IMAGE_SVG_XML })
    @Consumes({InteropMediaType.MEDIA_APPLICATION_JSON})
    public Response getLiveNode(@Context HttpServletResponse response,
                                @Context HttpServletRequest request,
                                @Context HttpHeaders headers,
                                @Context UriInfo uriInfo,
                                @Parameter(name = "relation", description = "relation", required = true) @PathParam("relation") String relation,
                                @Parameter(name = "id", description = "id", required = true) @PathParam("id") Integer id,
                                @Parameter(name = "extension", description = "extension", required = false) @PathParam("extension") String extension){
        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);


        logger.info("getLiveNode " + relation + " " + id);

        //logger.info("accept header " + headers.getAcceptableMediaTypes());

        List<TemplateQuery.RecordEntry2> ll=templateLogic.generateLiveNode(relation, id, principalAsPreferredUsername);
        TableKeyList tableKeyList=new TableKeyList();
        ll.forEach(e -> tableKeyList.key.add(new TableKey() {{
            isA=e.table_name;
            ID=e.key;
        }}));

        logger.info("getLiveNode " + tableKeyList);

        List<Object[]> records = queryTemplate.queryTemplates(tableKeyList, false, principalAsPreferredUsername);

        Document result=queryTemplate.constructDocument(documentBuilderDispatcher,records);

        Set<Object> selections=new HashSet<>();
        for (int i=0; i<records.size(); i++) {
            Object[] record=records.get(i);
            String property=ll.get(i).property;
            String template=(String)record[0];
            //logger.info("template " + template);
            //logger.info("property " + property);
            //logger.info("record " + java.util.Arrays.asList(record));

            int index=java.util.Arrays.asList(templateDispatcher.getPropertyOrder().get(template)).indexOf(property);
            Object[] objectRecord=recordMaker.get(template).apply(record);
            selections.add(objectRecord[index]);
        }

        logger.info("selections " + selections);


        Document newDoc=pf.newDocument();
        newDoc.setNamespace(result.getNamespace());
        for (Object selection: selections) {
            QualifiedName qn=(QualifiedName)selection;
            new ProvUtilities().getEntity(result).stream().filter(e -> qn.getUri().equals(e.getId().getUri())).forEach(e -> newDoc.getStatementOrBundle().add(e));
            new ProvUtilities().getAgent(result).stream().filter(e -> qn.getUri().equals(e.getId().getUri())).forEach(e -> newDoc.getStatementOrBundle().add(e));
            new ProvUtilities().getActivity(result).stream().filter(e -> qn.getUri().equals(e.getId().getUri())).forEach(e -> newDoc.getStatementOrBundle().add(e));
        }

        extension = determineOptionalExtension(headers, extension);

        switch (extension) {
            case "jsonld":
                return ServiceUtils.composeResponseOK(newDoc).type(InteropMediaType.MEDIA_APPLICATION_JSONLD).build();
            case "provn":
                return ServiceUtils.composeResponseOK(newDoc).type(InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION).build();
            case "svg":
                return ServiceUtils.composeResponseOK(newDoc).type(MEDIA_IMAGE_SVG_XML).build();
        }
        return utils.composeResponseBadRequest("unknown extension " + extension, new UnsupportedOperationException(extension));


    }

    @GET
    @Path("/template/{template}/{id}/hash")
    @Tag(name = "template")
    @Produces({ InteropMediaType.MEDIA_TEXT_PLAIN})
    public Response getHash(@Context HttpServletResponse response,
                                              @Context HttpServletRequest request,
                                              @Context HttpHeaders headers,
                                              @Context UriInfo uriInfo,

                                              @Parameter(name = "template", description = "template name", required = true) @PathParam("template") String template,
                                              @Parameter(name = "id", description = "record id", required = true) @PathParam("id") Integer id) {

        logger.info("getHash " + template + " " + id );

        Principal principal = request.getUserPrincipal();
        String principalAsPreferredUsername = getPrincipalAsPreferredUsername(principal);


        List<Object[]> records = queryTemplate.query(template, id, false, principalAsPreferredUsername);
        if (records.size()!=1) {
            return utils.composeResponseBadRequest("record not found", new IllegalArgumentException("size not 1:" + records.size()));
        }

        Object[] record=records.get(0);


        Object hash=queryTemplate.getHash(template,record);



        return ServiceUtils.composeResponseOK(hash).type(InteropMediaType.MEDIA_TEXT_PLAIN).build();

    }

    private String determineOptionalExtension(HttpHeaders headers, String extension) {
        if (extension ==null || extension.isEmpty()) {
            if (headers.getAcceptableMediaTypes().contains(MediaType.valueOf(InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION))) {
                extension ="provn";
            } else if (headers.getAcceptableMediaTypes().contains(MediaType.valueOf(InteropMediaType.MEDIA_APPLICATION_JSONLD))) {
                extension ="jsonld";
            } else if (headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MEDIA_IMAGE_SVG_XML))) {
                extension ="svg";
            } else if (headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MEDIA_TEXT_HTML))) {
                extension ="svg";
            } else {
                extension ="jsonld";
            }
        } else {
            extension = extension.substring(1);
        }
        return extension;
    }


}
