package org.openprovenance.prov.service.summary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.scala.immutable.Document;
import org.openprovenance.prov.scala.immutable.Indexer;
import org.openprovenance.prov.scala.summary.SummaryAPI;
import org.openprovenance.prov.scala.summary.SummaryConfig;
import org.openprovenance.prov.scala.summary.*;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.service.core.memory.DocumentResourceInMemory;
import org.openprovenance.prov.storage.api.*;
import scala.Tuple2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Path("")
public class SummaryService implements Constants, InteropMediaType {
    static Logger logger = LogManager.getLogger(SummaryService.class);

    final private ServiceUtils utils;
    private static final String KEYSTORE_PATH = "keystore_path";
    private static final String KEYSTORE_PASS = "keystore_pass";
    private static final String KEYSTORE_KEY = "keystore_key";
    private static final String KEYSTORE_KEYPASS = "keystore_keypass";
    private final JobManagement jobManager;
    private final NonDocumentResourceStorage ndStorage;

    public SummaryService(PostService postService) {
        this(postService, new LinkedList<ActionPerformer>(), Optional.empty());
    }

    public SummaryService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        this.utils=postService.getServiceUtils();
        postService.addToPerformers(PostService.addToList(new ActionUpload(utils),
                PostService.addToList(new ActionSummary(utils), performers)));
        postService.addOtherPerformer(Optional.of((otherPerformer.orElse(new EmptyOtherActionPerformer()))));
        this.jobManager = postService.getJobManager();
        ndStorage=utils.getNonDocumentResourceStorage();

    }


    @POST
    @Path("/documents/{docId}/summary")
    @Tag(name = "summary")
    @Consumes({MEDIA_APPLICATION_JSON})
    @Operation(summary = "Post a summary configuration document, directly, creates a resource, supports content negotiation, redirects to URL providing serialization for the resource",
            description = "It supports the direct posting of configuration documents in json format.",
            responses = {@ApiResponse(responseCode = "200",
                    //headers=@Header(name="location",description="Location of posted document"),
                    content = {@Content(mediaType = MEDIA_TEXT_TURTLE),
                               @Content(mediaType = MEDIA_TEXT_PROVENANCE_NOTATION),
                               @Content(mediaType = MEDIA_APPLICATION_PROVENANCE_XML),
                               @Content(mediaType = MEDIA_APPLICATION_TRIG),
                               @Content(mediaType = MEDIA_APPLICATION_RDF_XML),
                               @Content(mediaType = MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "303",
                                 headers = @Header(name = "location", description = "Location of posted document"),
                                 description = "See other url for serialization of posted resource as requested by accept header."),
                    @ApiResponse(responseCode = "404", description = "Provenance not found")})
    public Response submit(@Parameter(name = "input",
                                      description = "input file in a prov serialization",
                                      example = "{\"level\": \"2\", \"level0\": {\"mapper\":{},\"ignore\":[]}}") InputStream input,
                           @PathParam("docId") String msg,
                           @Context HttpHeaders headers,
                           //@Context HttpServletRequest requestContext,
                           @Context Request request) throws IOException {
        System.out.println("in submit summary configuration for " + msg);

        SummaryConfiguration sc=TypePropagator.om().readValue(input, SummaryConfiguration.class);

        final Level0Mapper level0 = new Level0Mapper(sc.level0());
        final int level = sc.level();
        final boolean aggregated=sc.aggregated();

        System.out.println("summary level is " + level);

        SummaryIndex ndx = getSummary(msg, level, level0, aggregated);

        ResourceIndex resourceIndexer=utils.getDocumentResourceIndex();
        ResourceIndex index=resourceIndexer.getIndex();
        NonDocumentResourceIndex<NonDocumentResource> ndIndex=utils.getNonDocumentResourceIndex();
        try {


            String serialConfigId = storeSummaryConfiguration(sc, ndIndex);

            String ptypesId = storeSummaryDescription(ndx, ndIndex);

            String storageId = storeSummaryDocument(ndx);

            String configId = index.newId();
            SummaryDocumentResource sdr = new SummaryDocumentResourceInMemory(new DocumentResourceInMemory()); // FIXME
            sdr.setVisibleId(configId);
            sdr.setStorageId(storageId);
       //     sdr.setLevel(level);
         //   sdr.setConfig(level0);
            sdr.setConfigId(serialConfigId);
            sdr.setPtypesId(ptypesId);
            utils.getDocumentResourceIndex().put(configId, sdr);

            Date date = jobManager.scheduleJob(sdr.getVisibleId());
            sdr.setExpires(date);

            return utils.composeResponseSeeOther("documents/" + sdr.getVisibleId()).header("Expires", date).build();
        } finally {
            index.close();
        }


    }

    public String storeSummaryDocument(SummaryIndex ndx) throws IOException {
        final ResourceStorage storageManager = utils.getStorageManager();
        String storageId = storageManager.newStore(Formats.ProvFormat.PROVN);
        final Document summaryDocument = ndx.document();
        utils.documentCache.put(storageId, summaryDocument);
        storageManager.writeDocument(storageId, Formats.ProvFormat.PROVN, summaryDocument);
        return storageId;
    }

    public String storeSummaryDescription(SummaryIndex ndx, NonDocumentResourceIndex<NonDocumentResource> ndIndex) throws IOException {
        NonDocumentResource aResource2=ndIndex.newResource();
        String ptypesId=aResource2.getVisibleId();
        aResource2.setMediaType(MEDIA_APPLICATION_JSON);
        String resourceName2=ndStorage.newStore("json",MEDIA_APPLICATION_JSON);
        ndStorage.serializeObjectToStore(TypePropagator.om(),ndx.summaryDescription(),resourceName2);
        aResource2.setStorageId(resourceName2);
        ndIndex.put(ptypesId,aResource2);
        return ptypesId;
    }

    public String storeSummaryConfiguration(SummaryConfiguration sc, NonDocumentResourceIndex<NonDocumentResource> ndIndex) throws IOException {
        NonDocumentResource aResource1 = ndIndex.newResource();
        String serialConfigId = aResource1.getVisibleId();
        aResource1.setMediaType(MEDIA_APPLICATION_JSON);
        String resourceName1 = ndStorage.newStore("json", MEDIA_APPLICATION_JSON);
        ndStorage.serializeObjectToStore(TypePropagator.om(), sc, resourceName1);
        aResource1.setStorageId(resourceName1);
        ndIndex.put(serialConfigId, aResource1);
        return serialConfigId;
    }

    public SummaryIndex getSummary(String docId, int level, Level0Mapper level0, boolean aggregated) throws IOException {
        return getSummaryIndex(docId, level, level0, false, aggregated);
    }

    public SummaryIndex getProvenanceKernel(String docId, int level, Level0Mapper level0, boolean aggregated) throws IOException {
        return getSummaryIndex(docId, level, level0, true, aggregated);
    }

    public SummaryIndex getSummaryIndex(String docId, int level, Level0Mapper level0, boolean kernel, boolean aggregated) throws IOException {
        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(docId);

            org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(dr.getStorageId());
            org.openprovenance.prov.model.Document d2 = org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(doc);
            Document d3 = (Document) d2;
            SummaryConfig config = new SummaryConfig(level, kernel, aggregated);
            Tuple2<Indexer, TypePropagator> pair = SummaryAPI.sum(d3, config, level0);
            Indexer ind = pair._1;
            TypePropagator tp = pair._2;
            SummaryIndex indexed = SummaryAPI.makeSummaryIndex(config, tp, ind, level, null, null);
            return indexed;
        } finally {
            index.close();
        }
    }
/*
    @GET
    @Path("/documents/{docId}/summary")
    @Operation(summary = "Summarise Document", description = "",
            // , responseClass = "org.openprovenance.prov.xml.Document"
            responses = {@ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND)})
    @Tag(name = "summary")
    public Response summarise(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "application/json,text/xml,application/xml",
                                      required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) throws IOException {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource vr = index.get(msg);


            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            if (!(vr instanceof SummaryDocumentResource)) {
                return utils.composeResponseNotFoundType(msg);
            }


            SummaryDocumentResource vr2 = (SummaryDocumentResource) vr;


            if (vr2.getsIndex() == null) {
                vr2.setsIndex(getSummary(vr2));
            }


            Document doc = vr2.getsIndex().document();


            return utils.composeResponseOK(true).type("application/json").build();
        } finally {
            index.close();
        }


    }

    public SummaryIndex getSummary(SummaryDocumentResource vr) throws IOException {
        org.openprovenance.prov.model.Document doc=utils.getDocumentFromCacheOrStore(vr.getStorageId());

        org.openprovenance.prov.model.Document d2 = org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(doc);
        Document d3 = (Document) d2;
        Config config = new Config(vr.getLevel(), false, false);
        Tuple2<Indexer, TypePropagator> pair = CommandLine.sum(d3, config, vr.getConfig());
        Indexer ind = pair._1;
        TypePropagator tp = pair._2;
        SummaryIndex indexed = CommandLine.makeSummaryIndex(config, tp, ind, vr.getLevel(), null);

        return indexed;
    }

 */


    @GET
    @Path("/documents/{docId}/config")
    @Operation(summary = "Summary Configuration Document",
            description = "",
            responses = {@ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND)})
    @Produces({MEDIA_APPLICATION_JSON})
    @Tag(name = "summary")

    public Response getConfiguration(@Context HttpServletResponse response,
                                     @Context Request request,
                                     @PathParam("docId") String msg) {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();

        try {
            DocumentResource dr = index.get(msg);

            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            if (!(dr instanceof SummaryDocumentResource)) {
                return utils.composeResponseNotFoundSummarisation(msg);
            }


            SummaryDocumentResource sdr = (SummaryDocumentResource) dr;
            NonDocumentResourceIndex<NonDocumentResource>  ndIndex=utils.getNonDocumentResourceIndex();
            NonDocumentResource ndr=ndIndex.get(sdr.getConfigId());

            SummaryConfiguration sc=ndStorage.deserializeObjectFromStore(TypePropagator.om(),ndr.getStorageId(), SummaryConfiguration.class);



            StreamingOutput so=(out) -> TypePropagator.om().writeValue(out,sc);

            return utils.composeResponseOK(so).type("application/json").build();
        } catch (IOException e) {
            e.printStackTrace();
            return utils.composeResponseInternalServerError("deserialization from store", e);
        } finally {
            index.close();
        }
    }


    @GET
    @Path("/documents/{docId}/details")
    @Operation(summary = "Summary Prov Types",
            description = "",
            responses = {@ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND)})
    @Produces({MEDIA_APPLICATION_JSON})
    @Tag(name = "summary")

    public Response getSummaryProvTypes(@Context HttpServletResponse response,
                                     @Context Request request,
                                     @PathParam("docId") String msg) {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();

        try {
            DocumentResource dr = index.get(msg);

            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            if (!(dr instanceof SummaryDocumentResource)) {
                return utils.composeResponseNotFoundSummarisation(msg);
            }


            SummaryDocumentResource sdr = (SummaryDocumentResource) dr;

            System.out.println("Found ptypes " + sdr.getPtypesId());

            NonDocumentResourceIndex<NonDocumentResource>  ndIndex=utils.getNonDocumentResourceIndex();
            NonDocumentResource ndr=ndIndex.get(sdr.getPtypesId());

            System.out.println("Found ptypes storage id" + ndr.getStorageId());

            SummaryDescriptionJson sdj=ndStorage.deserializeObjectFromStore(TypePropagator.om(),ndr.getStorageId(),SummaryDescriptionJson.class);

            StreamingOutput so=(out) -> TypePropagator.om().writeValue(out,sdj);

            return utils.composeResponseOK(so).type("application/json").build();
        } catch (IOException e) {
            e.printStackTrace();
            return utils.composeResponseInternalServerError("storage issue",e);
        } finally {
            index.close();
        }
    }

    @POST
    @Path("/documents/{docId}/provtypes")
    @Tag(name = "summary")
    @Consumes({MEDIA_APPLICATION_JSON})
    @Operation(summary = "Post a PROV Type configuration document, directly, creates a resource, redirects to URL providing serialization for the resource",
            description = "It supports the direct posting of configuration documents in json format.",
            responses = {@ApiResponse(responseCode = "200",
                    //headers=@Header(name="location",description="Location of posted document"),
                    content = {
                            @Content(mediaType = MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "303",
                            headers = @Header(name = "location", description = "Location of posted document"),
                            description = "See other url for serialization of posted resource as requested by accept header."),
                    @ApiResponse(responseCode = "404", description = "Provenance not found")})
    public Response submitProvtypesConfig(@Parameter(name = "input",
                                                  description = "input file in a prov serialization",
                                                  example = "{\"level\": \"2\", \"level0\": {\"mapper\":{},\"ignore\":[]}}") InputStream input,
                                          @PathParam("docId") String msg,
                                          @Context HttpHeaders headers,
                                          //@Context HttpServletRequest requestContext,
                                          @Context Request request) throws IOException {
        System.out.println("in submit prov types configuration for " + msg);

        SummaryConfiguration sc=TypePropagator.om().readValue(input, SummaryConfiguration.class);

        //Level0Mapper level0 = new Level0Mapper(sc.level0());

        System.out.println("submit " + sc.level());


        NonDocumentResourceIndex<NonDocumentResource>  ndIndex=utils.getNonDocumentResourceIndex();

        System.out.println(ndIndex);

        String visibleId = storeSummaryConfiguration(sc, ndIndex);


        return utils.composeResponseSeeOther("documents/" + msg + "/provtypes/" + visibleId + ".json").build();


    }


    @GET
    @Path("/documents/{docId}/provtypes/{configId}.json")
    @Tag(name = "summary")
    @Consumes({MEDIA_APPLICATION_JSON})
    @Operation(summary = "Returns the prov type mapping as json file",
            description = "Returns the prov type mapping as json file.",
            responses = {@ApiResponse(responseCode = "200",
                                      content = {@Content(mediaType = MEDIA_APPLICATION_JSON)}),
                         @ApiResponse(responseCode = "404", description = "Provenance not found")})
    public Response getProvtypes(
            @PathParam("docId") String msg,
            @PathParam("configId") String configId,
                                          @Context HttpHeaders headers,
                                          //@Context HttpServletRequest requestContext,
                                          @Context Request request) throws IOException {
        System.out.println("in get prov types configuration for " + msg + " " + configId);



        NonDocumentResourceIndex<NonDocumentResource> index=utils.getNonDocumentResourceIndex();


        NonDocumentResource nd=index.get(configId);
        System.out.println("nd resource " + configId + " " + nd.getStorageId());

        SummaryConfiguration sc=ndStorage.deserializeObjectFromStore(TypePropagator.om(),nd.getStorageId(), SummaryConfiguration.class);

        Level0MapperJson level0=sc.level0();

        System.out.println("level 0 " + level0);

        SummaryIndex ndx = getProvenanceKernel(msg, sc.level(), new Level0Mapper(level0), sc.aggregated());


        StreamingOutput promise2=(out) -> ndx.exportToJsonDescription(out);


        return utils.composeResponseOK(promise2).type(MediaType.APPLICATION_JSON_TYPE).build();


    }

    @GET
    @Path("/documents/{docId}/provtypes/{configId}/config")
    @Tag(name = "summary")
    @Consumes({MEDIA_APPLICATION_JSON})
    @Operation(summary = "Returns the prov type mapping as json file",
            description = "Returns the prov type mapping as json file.",
            responses = {@ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "404", description = "Provenance not found")})
    public Response getProvtypesConfiguration(
            @PathParam("docId") String msg,
            @PathParam("configId") String configId,
            @Context HttpHeaders headers,
            //@Context HttpServletRequest requestContext,
            @Context Request request) throws IOException {
        System.out.println("in get prov types configuration for " + msg + " " + configId);



        NonDocumentResourceIndex<NonDocumentResource> index=utils.getNonDocumentResourceIndex();


        NonDocumentResource nd=index.get(configId);
        System.out.println("nd resource " + configId + " " + nd.getStorageId());

        SummaryConfiguration sc=ndStorage.deserializeObjectFromStore(TypePropagator.om(),nd.getStorageId(), SummaryConfiguration.class);

        Level0MapperJson level0=sc.level0();

        System.out.println("level 0 " + level0);


        StreamingOutput promise2=(out) -> TypePropagator.om().writeValue(out,sc);


        return utils.composeResponseOK(promise2).type(MediaType.APPLICATION_JSON_TYPE).build();


    }


}

    

    

