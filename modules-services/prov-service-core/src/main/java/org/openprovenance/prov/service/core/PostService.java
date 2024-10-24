package org.openprovenance.prov.service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.exception.ParserException;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.service.core.rest.ForwardedUriInfo;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.quartz.SchedulerException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.io.InputStream;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Path("")
public class PostService implements Constants, InteropMediaType, SwaggerTags, ApiUriFragments {

    static Logger logger = LogManager.getLogger(PostService.class);

    private final JobManagement jobManager = new JobManagement();
    private final boolean autoDelete;

    public PostService(ServiceUtilsConfig config) {
        this(config, new LinkedList<>(), Optional.empty());
    }

    public JobManagement getJobManager() {
        return jobManager;
    }

    final protected ServiceUtils utils;

    private final List<ActionPerformer> performers;
    private Optional<OtherActionPerformer> otherPerformer;


    public static <E> List<E> addToList(E element, List<E> list) {
        List<E> result = new LinkedList<>();
        result.add(element);
        result.addAll(list);
        return result;
    }

    public PostService(ServiceUtilsConfig config, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        utils = new ServiceUtils(this, config);
        this.autoDelete = config.autoDelete;
        jobManager.setupScheduler();
        try {
            JobManagement.getScheduler().getContext().put(JobManagement.UTILS_KEY, utils);
            JobManagement.getScheduler().getContext().put(JobManagement.DURATION_KEY, config.deletePeriod);
        } catch (SchedulerException e) {
            logger.throwing(e);
        }
        this.performers = performers;
        this.otherPerformer = otherPerformer;
    }

    public ServiceUtils getServiceUtils() {
        return utils;
    }

    public void addToPerformers(List<ActionPerformer> newPerformers) {
        performers.addAll(newPerformers);
    }

    public void addOtherPerformer(Optional<OtherActionPerformer> newOtherPerformer) {
        otherPerformer = newOtherPerformer;
    }

    public Map<String, Object> addToConfiguration(String property, Object value) {
        this.configuration.put(property, value);
        return this.configuration;
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    private final Map<String,Object> configuration=new HashMap<>();


    @POST
    @Path(FRAGMENT_DOCUMENTS_FORM)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Tag(name = DOCUMENTS)

    @Operation(summary = "Post a document. Payload is a form that may contain a file, prov statements or a url",
            description = "Post a document in a form; indicate if validation or translation required; and redirects to the appropriate page. This method  is also designed for browser interaction, allowing the user to select a file, a url or statements for provenance.  The user may specify validate or translate directly.",
            responses = {@ApiResponse(responseCode = "303",
                    headers = @Header(name = "location", description = "Location of posted document"),
                    description = "See other url for serialization of posted resource as requested by accept header."),
                    @ApiResponse(responseCode = "404", description = "Provenance not found")})

    public Response submit(@Parameter(name = "form",
            description = "form to be posted",
            required = true,
            schema = @Schema(implementation = UploadForm.class))
                           MultipartFormDataInput input,
                           @Context HttpHeaders headers,
                           @Context HttpServletRequest ignoredRequestContext,
                           @Context UriInfo uriInfo) {
        MediaType mediaType = headers.getMediaType();

        Principal principal = ignoredRequestContext.getUserPrincipal();
        logger.info("principal is " + principal);

        if (mediaType.toString().startsWith("multipart/form-data")) {

            try {


                Map<String, List<InputPart>> formData = input.getFormDataMap();
                logger.debug("submitted " + formData);

                DocumentResource vr;
                vr = processFileInForm(formData);
                if (vr == null) vr = processUrlInForm(formData);
                if (vr == null) vr = processStatementsInForm(formData);

                boolean success = utils.doProcessFile(vr, true);

                if (vr == null) {
                    String result = "No provenance was found, and therefore failed to create resource for validation service";
                    return utils.composeResponseNotFOUND(result);
                }
                if (!success) {
                    String result = "Failed to parse provenance";
                    return utils.composeResponseBadRequest(result, vr.getThrown());
                }
                if (/*vr.bundle == null && */  !utils.documentCache.containsKey(vr.getStorageId())) {  //TODO: what about bundle?
                    String result = "No provenance was found (empty document), and therefore failed to create resource for validation service";
                    return utils.composeResponseNotFOUND(result);
                }

                Date date = autoDelete ? jobManager.scheduleJob(vr.getVisibleId()) : null;
                vr.setExpires(date);

                final ServiceUtils.Action action = utils.getAction(formData);
                doLog(action, vr, uriInfo, headers);

                logger.debug("trying performers " + performers);

                for (ActionPerformer performer : performers) {
                    logger.debug("trying performer " + performer.getAction());
                    if (performer.getAction().equals(action)) {
                        logger.debug("invoking performer " + performer.getAction());
                        return performer.doAction(formData, vr, date);
                    }

                }


                logger.debug("checking other performer ");
                if (otherPerformer.isPresent() && otherPerformer.get().otherAction(action, formData)) {
                    logger.debug("invoking other performer ");
                    return otherPerformer.get().doAction(action, formData, vr, date);
                }

                //default
                String location = "view/documents/" + vr.getVisibleId() + "/translation.html";
                // alternatively, go to document the landing page

                return utils.composeResponseSeeOther(location).header("Expires", date).build();

            } catch (UncheckedException e) {
                return utils.composeResponseBadRequest("URI problem (" + e.getCause() + ")", e);
            } catch (ParserException e) {
                return utils.composeResponseBadRequest("Parser problem", e);
            } catch (Throwable t) {
                logger.throwing(t);
                String result = "exception occurred";
                return utils.composeResponseInternalServerError(result, t);
            }
        } else {
            String result = "Media type " + mediaType + " Not Supported yet";
            return utils.composeResponseNotFOUND(result);
        }

    }


    @POST
    @Path(FRAGMENT_DOCUMENTS)
    @Tag(name = DOCUMENTS)
    @Consumes({
            MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_JSON})
    @Operation(
            summary = "Post a document, directly, creates a resource, supports content negotiation, redirects to URL providing serialization for the resource",
            description = "It supports the direct posting of documents using a prov serialization.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            //headers=@Header(name="location",description="Location of posted document"),
                            content = {
                                    @Content(mediaType = MEDIA_TEXT_TURTLE),
                                    @Content(mediaType = MEDIA_TEXT_PROVENANCE_NOTATION),
                                    @Content(mediaType = MEDIA_APPLICATION_PROVENANCE_XML),
                                    @Content(mediaType = MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(
                            responseCode = "303",
                            headers = @Header(name = "location", description = "Location of posted document"),
                            description = "See other url for serialization of posted resource as requested by accept header."),
                    @ApiResponse(
                            responseCode = "404", description = "Provenance not found")})
    public Response submit2(@Parameter(name = "input",
            description = "input file in a prov serialization",
            example = "document\n prefix ex <http://foo>\n entity(ex:e)\nendDocument") InputStream input,
                            @Context HttpHeaders headers,
                            //@Context HttpServletRequest requestContext,
                            @Context Request request) {

        MediaType mediaType = headers.getMediaType();
        String type = mediaType.getType() + "/" + mediaType.getSubtype();


        logger.debug(">>> post media type is " + type);

        logger.debug("accept header is" + headers.getAcceptableMediaTypes());

        DocumentResource vr = utils.doProcessFile(input, type);

        Date date = jobManager.scheduleJob(vr.getVisibleId());
        vr.setExpires(date);

        // TODO: maybe not return content directly

        if (true) {
            return utils.contentNegotiationForDocument(request, vr.getVisibleId(), ".");
        }

        return utils.composeResponseSeeOther("documents/" + vr.getVisibleId()).header("Expires", date).build();
    }

    @GET
    @Path(FRAGMENT_CONFIGURATION)
    @Tag(name = "configuration")
    @Operation(
            summary = "Get configuration",
            description = "Get configuration",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(mediaType = MEDIA_APPLICATION_JSON)})})
    public Response getConfiguration(@Context HttpHeaders headers) {
        StreamingOutput promise= out -> new ObjectMapper().writeValue(out, configuration);

        return ServiceUtils.composeResponseOK(promise).type(InteropMediaType.MEDIA_APPLICATION_JSON).build();
    }



    private DocumentResource processFileInForm(Map<String, List<InputPart>> formData) {
        DocumentResource vr = null;
        List<InputPart> inputParts = formData.get("file");
        if ((inputParts != null)) {
            vr = utils.doProcessFileForm(inputParts);
        }
        return vr;
    }

    private DocumentResource processUrlInForm(Map<String, List<InputPart>> formData) {
        DocumentResource vr = null;
        List<InputPart> inputParts = formData.get("url");
        if (inputParts != null) {
            vr = utils.doProcessURLForm(inputParts);
        }
        return vr;
    }

    private DocumentResource processStatementsInForm(Map<String, List<InputPart>> formData) {
        DocumentResource vr = null;
        List<InputPart> inputParts = formData.get("statements");
        List<InputPart> type = formData.get("type");
        if (inputParts != null) {
            vr = utils.doProcessStatementsForm(inputParts, type);
        }
        return vr;
    }

    private void doLog(ServiceUtils.Action action, DocumentResource vr, UriInfo uriInfo, HttpHeaders headers) {
        doLog(action.toString(), vr, uriInfo, headers);
    }

    private void doLog(String action, DocumentResource vr, UriInfo uriInfo, HttpHeaders headers) {
        logger.log(ProvLevel.PROV,
                action + ","
                        + vr.getVisibleId() + ","
                        + vr.getStorageId());

        JobManagement.scheduleCurlJob(vr.getVisibleId(),
                action + ","
                        + vr.getVisibleId() + ","
                        + vr.getStorageId() + ","
                        + new ForwardedUriInfo(uriInfo, new AtomicReference<>(headers)).getAbsolutePath() + ","
                        + "provapi");
    }


}
