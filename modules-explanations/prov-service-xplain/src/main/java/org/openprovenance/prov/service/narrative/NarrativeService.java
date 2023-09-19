package org.openprovenance.prov.service.narrative;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.scala.immutable.Document;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.scala.nlg.Narrative;
import org.openprovenance.prov.scala.nlg.Narrator;
import org.openprovenance.prov.scala.wrapper.IO;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.memory.LRUHashMap;
import org.openprovenance.prov.service.summary.ActionUpload;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.io.IOException;
import java.util.*;


@Path("")
@Tag(name="narrative")
public class NarrativeService  implements Constants, InteropMediaType {

    public static final String _LOAN_TEMPLATE_LIBRARY_JSON = "/nlg/templates/loan/template-library.json";
    public static final String PLEAD_TEMPLATE_LIBRARY_JSON = "/nlg/templates/plead/plead-template-library.json";
    public static final String PLEAD_CS_TEMPLATE_LIBRARY_JSON = "/nlg/templates/plead.cs/plead-template-library.json";
    private final NarrativeServiceUtils utils;
    private final LRUHashMap<String, Document> scalaDocumentCache;

    int format_option=1;

    public NarrativeService(PostService postService) {
        this(postService, new LinkedList<ActionPerformer>(), Optional.<OtherActionPerformer>empty());

    }

    public String the_template_library=PLEAD_CS_TEMPLATE_LIBRARY_JSON;

    public NarrativeService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        this.utils=new NarrativeServiceUtils(postService,postService.getServiceUtils().getConfig());
        postService.addToPerformers(
                PostService.addToList(new ActionRandom(utils),
                        PostService.addToList(new ActionLinear(utils),
                                PostService.addToList(new ActionExplain(utils),
                                        PostService.addToList(new ActionUpload(utils), performers)))));
        postService.addOtherPerformer(Optional.of(otherPerformer.orElse(new EmptyOtherActionPerformer())));
        scalaDocumentCache =new LRUHashMap<>(postService.getServiceUtils().getConfig().documentCacheSize);

    }


    private static Logger logger = LogManager.getLogger(NarrativeService.class);



    @GET
    @Path("/documents/{docId}/random_narrative")
    @Tag(name="narrative")
    @Operation(summary = "Random Narrative of Document", description = "",
           // , responseClass = "org.openprovenance.prov.xml.Document"
           responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response random_nar(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "text/plain,text/xml",
                              required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {

            DocumentResource vr = index.get(msg);

            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            try {
                Variant v = request.selectVariant(narrativeVariants);
                final MediaType mediaType = v.getMediaType();
                Object textDocument = getRandomNarrative(vr, mediaType);

                //System.out.println("debug --> " + textDocument.toString());

                return utils.composeResponseOK(textDocument).type(mediaType).build();
            } catch (Throwable e) {
                e.printStackTrace();
                String result = "Failed to create a narrative";
                return utils.composeResponseBadRequest(result, e);

            }
        } finally {
            index.close();
        }

    }


    @GET
    @Path("/documents/{docId}/explanation/{expid}")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Explanation from Document", description = "",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanations(@Context HttpServletResponse response,
                                 @Context Request request,
                                 @PathParam("docId") String msg,
                                 @PathParam("expid") String explanationId,
                                 @Parameter(name = HEADER_PARAM_ACCEPT,
                                            description = "Accept header parameter",
                                            example = "text/plain,text/xml",
                                            required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {

        return getExplanationForId(msg, explanationId, true);

    }

    @GET
    @Path("/documents/{docId}/explanationdetails/{expid}")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Explanation from Document", description = "",
               responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationDetails(@Context HttpServletResponse response,
                                       @Context Request request,
                                       @PathParam("docId") String msg,
                                       @PathParam("expid") String explanationId,
                                       @Parameter(name = HEADER_PARAM_ACCEPT,
                                                  description = "Accept header parameter",
                                                  example = "text/plain,text/xml",
                                                  required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        return getExplanationForId(msg, explanationId, false);

    }

    public Response getExplanationForId(String msg,
                                        String explanationId,
                                        boolean partial) {
        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);

            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            Map<String, Map<String, String>> details = utils.getDetails(index, msg, dr);

            Map<String, String> entry = details.get(explanationId);

            String templates = entry.get(NarrativeServiceUtils.KEY_TEMPLATES);
            String profiles = entry.get(NarrativeServiceUtils.KEY_PROFILES);

            //logger.debug("found templates: " + templates);
            //logger.debug("found profiles: " + profiles);


            try {
                org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(dr.getStorageId());

                ServiceConfig config = new ServiceConfig(templates, profiles, the_template_library, format_option);

                Document doc1 = getScalaDocument(msg, doc);

                StreamingOutput promise;

                if (partial) {
                    scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = Narrator.getTextOnly2(Narrator.explain(doc1, config));
                    promise = out -> IO.mapper().writeValue(out,result);
                } else {
                    scala.collection.immutable.Map<String, Narrative> result = Narrator.explain(doc1, config);
                    promise = out -> IO.mapper().writeValue(out,result);
                }
                return utils.composeResponseOK(promise).type(MediaType.APPLICATION_JSON).build();
            } catch (Throwable e) {
                e.printStackTrace();
                String result = "Failed to create an explanation";
                return utils.composeResponseBadRequest(result, e);
            }
        } finally {
            index.close();
        }
    }

    ProvFactory pFactoryS = ProvFactory.pf();

    public Document getScalaDocument(String id,  org.openprovenance.prov.model.Document doc) {
        Document cached;
        synchronized (this) {
            cached=scalaDocumentCache.get(id);
        }
        if (cached==null) {
            Document doc2= (Document) new BeanTraversal(pFactoryS, pFactoryS).doAction(doc);
            synchronized (this) {
                scalaDocumentCache.put(id,doc2);
            }
            cached=doc2;
        }
        return cached;
    }

    @GET
    @Path("/documents/{docId}/animation.svg")
    @Tag(name="narrative")
    @Produces({InteropMediaType.MEDIA_IMAGE_SVG_XML})
    @Operation(summary = "SVG for animation from Document", description = "",
              responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response svg4anim(@Context HttpServletResponse response,
                             @Context Request request,
                             @PathParam("docId") String msg,
                             @Parameter(name = HEADER_PARAM_ACCEPT,
                                         description = "Accept header parameter",
                                         example = "text/plain,text/xml",
                                         required = false)@HeaderParam(HEADER_PARAM_ACCEPT) String accept) throws IOException {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource vr = index.get(msg);

            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(vr.getStorageId());

            Document sdoc = getScalaDocument(msg, doc);

            return utils.composeResponseOK(sdoc).type(InteropMediaType.MEDIA_IMAGE_SVG_XML).build();
        } finally {
            index.close();
        }

    }


    @GET
    @Path("/documents/{docId}/explanationdetails/{expid}/config")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Configuration of Explanation for Document", description = "",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationDetailsConfig(@Context HttpServletResponse response,
                                             @Context Request request,
                                            @PathParam("docId") String msg,
                                            @PathParam("expid") String explanationId) {
        return explanationConfig(response,request,msg,explanationId);
    }

    @GET
    @Path("/documents/{docId}/explanation/{expid}/config")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Configuration of Explanation for Document", description = "",
              responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationConfig(@Context HttpServletResponse response,
                                      @Context Request request,
                                      @PathParam("docId") String msg,
                                      @PathParam("expid") String explanationId) {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);

            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            Map<String, Map<String, String>> details = utils.getDetails(index, msg, dr);

            Map<String, String> entry = details.get(explanationId);

            String templates = entry.get(NarrativeServiceUtils.KEY_TEMPLATES);
            String profiles = entry.get(NarrativeServiceUtils.KEY_PROFILES);

            //logger.debug("found templates: " + templates);
            //logger.debug("found profiles: " + profiles);

            TemplateAndProfile tap = new TemplateAndProfile();
            tap.profile = profiles;
            tap.template = templates;

            return utils.composeResponseOK(tap).type(MediaType.APPLICATION_JSON).build();
        } finally {
            index.close();
        }
    }

    @POST
    @Path("/documents/{docId}/explanation/")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create explanation from existing Document",
               description = "",
               responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationsForDoc(@Context HttpServletResponse response,
                                       @Context Request request,
                                       @PathParam("docId") String msg,
                                       TemplateAndProfile tap,
                                       @Context UriInfo uriInfo,
                                       @Parameter(name = HEADER_PARAM_ACCEPT,
                                                  description = "Accept header parameter",
                                                  example = "text/plain,text/xml",
                                                  required = false)@HeaderParam(HEADER_PARAM_ACCEPT) String accept) {
        return explanationsForDoc1(response, request, msg, tap.template, tap.profile, true, uriInfo, accept);
    }

    @POST
    @Path("/documents/{docId}/explanationdetails/")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create explanation, and returns all details for existing Document",
            description = "",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationsDetailsForDoc(@Context HttpServletResponse response,
                                       @Context Request request,
                                       @PathParam("docId") String msg,
                                       TemplateAndProfile tap,
                                       @Context UriInfo uriInfo,
                                       @Parameter(name = HEADER_PARAM_ACCEPT,
                                                  description = "Accept header parameter",
                                                  example = "text/plain,text/xml",
                                                  required = false)@HeaderParam(HEADER_PARAM_ACCEPT) String accept) {
        return explanationsForDoc1(response, request, msg, tap.template, tap.profile, false, uriInfo, accept);
    }

    public Response explanationsForDoc1(HttpServletResponse response,
                                        Request request,
                                        String msg,
                                        String template,
                                        String profile,
                                        boolean partial,
                                        UriInfo uriInfo,
                                        String accept) {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);


            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            Map<String, Map<String, String>> details = utils.getDetails(index, msg, dr);

            String explanationId = utils.createEntryForTemplateAndProfile(index, msg, dr, template, profile);

            Map<String, String> entry = details.get(explanationId);

            String templates = entry.get(NarrativeServiceUtils.KEY_TEMPLATES);
            String profiles = entry.get(NarrativeServiceUtils.KEY_PROFILES);

            index.put(msg, dr);

            logger.debug("found templates: " + templates);
            logger.debug("found profiles: " + profiles);
            logger.debug("explanation id: " + explanationId);


            try {
                org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(dr.getStorageId());

                ServiceConfig config = new ServiceConfig(templates, profiles, the_template_library, format_option);

                Document doc1 = getScalaDocument(msg, doc);

                String s;

                if (partial) {
                    scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = Narrator.getTextOnly2(Narrator.explain(doc1, config));
                    s = IO.mapper().writeValueAsString(result);
                } else {
                    scala.collection.immutable.Map<String, Narrative> result = Narrator.explain(doc1, config);
                    s = IO.mapper().writeValueAsString(result);
                }

                return utils.composeResponseCreated(s, explanationId, uriInfo).type(MediaType.APPLICATION_JSON).build();
            } catch (Throwable e) {
                e.printStackTrace();
                String result = "Failed to create an explanation";
                return utils.composeResponseBadRequest(result, e);

            }
        } finally {
            index.close();
        }
    }



    
    @GET
    @Path("/documents/{docId}/linear_narrative")
    @Tag(name="narrative")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(summary = "Linear Narrative of Document", description = "",
               responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response linear_narrative(@Context HttpServletResponse response,
                                     @Context Request request,
                                     @PathParam("docId") String msg,
                                     @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "text/plain,text/xml",
                                             required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);


            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            try {
                Variant v = request.selectVariant(narrativeVariants);
                final MediaType mediaType = v.getMediaType();
                Object textDocument = getLinearNarrative(dr, mediaType);

                System.out.println("debug --> " + textDocument.toString());

                return utils.composeResponseOK(textDocument).type(mediaType).build();
            } catch (Throwable e) {
                e.printStackTrace();
                String result = "Failed to create a narrative";
                return utils.composeResponseBadRequest(result, e);
            }
        } finally {
            index.close();
        }

    }



    final List<Variant> narrativeVariants =Arrays.asList(new Variant(MediaType.APPLICATION_JSON_TYPE,(java.util.Locale)null,null),
            new Variant(MediaType.TEXT_PLAIN_TYPE,(java.util.Locale)null,null));

    ObjectMapper mapper = org.openprovenance.prov.scala.summary.TypePropagator.om();
    public Object getLinearNarrative(DocumentResource vr, MediaType variant) throws IOException {
        return getNarrative(vr,Narrator.linearConfig(),variant);
    }
    public Object getRandomNarrative(DocumentResource vr, MediaType variant) throws IOException {
        return getNarrative(vr,Narrator.randomConfig(),variant);
    }

    public Object getNarrative(DocumentResource vr, org.openprovenance.prov.scala.nlg.Config config, MediaType variant) throws IOException {
        org.openprovenance.prov.model.Document doc=utils.getDocumentFromCacheOrStore(vr.getStorageId());
        org.openprovenance.prov.model.Document d2=org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(doc);
        Document d3=(Document)d2;


        logger.info("variant: " + variant);
        if (MediaType.TEXT_PLAIN_TYPE.equals(variant)) {
            final scala.collection.immutable.Map<String, String> result = Narrator.narrate2string(d3, config);
            return result.mkString("", "\n", "");
        } else {
            final scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = Narrator.narrate2(d3, config);
            StreamingOutput promise=(out) -> mapper.writeValue(out,result);
            return promise;
        }
    }





    }
