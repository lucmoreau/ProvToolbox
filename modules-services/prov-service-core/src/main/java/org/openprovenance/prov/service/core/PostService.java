package org.openprovenance.prov.service.core;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.exception.ParserException;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.quartz.SchedulerException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.util.*;

@Path("")
public class PostService implements Constants, InteropMediaType {

    static Logger logger = Logger.getLogger(PostService.class);

    private final JobManagement jobManager = new JobManagement();

    public JobManagement getJobManager() {
        return jobManager;
    }

    final protected ServiceUtils utils=new ServiceUtils();

    private final List<ActionPerformer> performers;
    private Optional<OtherActionPerformer> otherPerformer;

    public static <E> List<E> addToList(E element, List<E> list) {
        List<E> result=new LinkedList<>();
        result.add(element);
        result.addAll(list);
        return result;
    }

    public PostService(List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {

        jobManager.setupScheduler();
        try {
            jobManager.getScheduler().getContext().put(JobManagement.UTILS_KEY, utils);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        this.performers=performers;
        this.otherPerformer=otherPerformer;

    }

    public PostService() {
        this(new LinkedList<>(),Optional.empty());
    }


    public ServiceUtils getServiceUtils() {
        return utils;
    }

    public void addToPerformers(List<ActionPerformer> newPerformers) {
        performers.addAll(newPerformers);
    }


    public void addOtherPerformer(Optional<OtherActionPerformer> newOtherPerformer) {
      otherPerformer=newOtherPerformer;
    }


    @POST
    @Path("/documents/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Tag(name="documents")

    @Operation(summary = "Post a document. Payload is a form that may contain a file, prov statements or a url",
            description = "Post a document in a form; indicate if validation or translation required; and redirects to the appropriate page. This method  is also designed for browser interaction, allowing the user to select a file, a url or statements for provenance.  The user may specify validate or translate directly.",
            responses = {  @ApiResponse(responseCode = "303",
                    headers=@Header(name="location",description="Location of posted document"),
                    description = "See other url for serialization of posted resource as requested by accept header."),
                    @ApiResponse(responseCode = "404", description = "Provenance not found") })

    public Response submit(@Parameter(name = "form",
                                      description = "form to be posted",
                                        required=true,

                                        schema =@Schema(implementation = UploadForm.class))
                                                                   MultipartFormDataInput input,
                                                       @Context HttpHeaders headers,
                                                       @Context HttpServletRequest requestContext) {
        MediaType mediaType = headers.getMediaType();
        System.out.println("post media type is" + mediaType);

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

                if (/*vr.bundle == null && */vr.document() == null) {
                    String result = "No provenance was found (empty document), and therefore failed to create resource for validation service";
                    return utils.composeResponseNotFOUND(result);
                }



                Date date = jobManager.scheduleJob(vr.getVisibleId());
                vr.setExpires(date);

                final ServiceUtils.Action action = utils.getAction(formData);
                doLog(action,vr);

                logger.info("trying performers " + performers);

                for (ActionPerformer performer: performers) {
                    logger.info("trying performer " + performer.getAction());
                    if (performer.getAction().equals(action)) {
                        logger.info("invoking performer " + performer.getAction());
                        return performer.doAction(formData, vr, date);
                    }

                }


/*
                if (action.equals(ServiceUtils.Action.VALIDATE)) {
                    return actionValidate.doAction(null, vr, date);
                }
                if ((action.equals(ServiceUtils.Action.EXPAND))) {
                    return actionExpand.doAction(formData, vr, date);
                }
                if ((action.equals(ServiceUtils.Action.TRANSLATE))) {
                    return actionTranslate.doAction(formData, vr, date);
                }
                */


                logger.info("checking other performer " );
                if (otherPerformer.isPresent() && otherPerformer.get().otherAction(action,formData)) {
                    logger.info("invokign other performer " );
                    return otherPerformer.get().doAction(action,formData,vr, date);
                }

                //default
                String location = "view/documents/" + vr.getVisibleId() + "/translation.html";
                // alternatively, go to document the landing page

                return utils.composeResponseSeeOther(location).header("Expires", date).build();

            } catch (UncheckedException e) {
                return utils.composeResponseBadRequest("URI problem (" + e.getCause() + ")",e);
            } catch (ParserException e) {
                return utils.composeResponseBadRequest("Parser problem",e);
            }
            catch (Throwable t) {
                t.printStackTrace();
                String result = "exception occurred";
                return utils.composeResponseInternalServerError(result,t);
            }
        } else {
            String result = "Media type " + mediaType + " Not Supported yet";
            return utils.composeResponseNotFOUND(result);
        }

    }



    @POST
    @Path("/documents2/")
    @Tag(name="documents")
    @Consumes({ MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
            MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON })
    @Operation(summary = "Post a document, directly, creates a resource, supports content negotiation, redirects to URL providing serialization for the resource",
            description = "It supports the direct posting of documents using a prov serialization.",
            responses = { @ApiResponse( responseCode = "200",
                    //headers=@Header(name="location",description="Location of posted document"),
                    content={@Content(mediaType=MEDIA_TEXT_TURTLE),
                            @Content(mediaType=MEDIA_TEXT_PROVENANCE_NOTATION),
                            @Content(mediaType=MEDIA_APPLICATION_PROVENANCE_XML),
                            @Content(mediaType=MEDIA_APPLICATION_TRIG),
                            @Content(mediaType=MEDIA_APPLICATION_RDF_XML),
                            @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "303",
                            headers=@Header(name="location",description="Location of posted document"),
                            description = "See other url for serialization of posted resource as requested by accept header."),
                    @ApiResponse(responseCode = "404", description = "Provenance not found") })
    public Response submit2(@Parameter(name = "input",
            description = "input file in a prov serialization",
            example="document\n prefix ex <http://foo>\n entity(ex:e)\nendDocument") InputStream input,
                            @Context HttpHeaders headers,
                            //@Context HttpServletRequest requestContext,
                            @Context Request request) {

        MediaType mediaType = headers.getMediaType();
        String type = mediaType.getType() + "/" + mediaType.getSubtype();

        // System.out.println("buffer is " + slurp(input, 100));

        System.out.println(">>> post media type is " + type);
        //System.out.println("content type header is" + contentType);

        System.out.println("accept header is" + headers.getAcceptableMediaTypes());

        DocumentResource vr;

        vr = utils.doProcessFile(input, type);

        Date date = jobManager.scheduleJob(vr.getVisibleId());
        vr.setExpires(date);

        // TODO: maybe not return content directly

        if (true) {
            return utils.contentNegotiationForDocument(request,vr.getVisibleId(),".");
        }

        return utils.composeResponseSeeOther("documents/" + vr.getVisibleId()).header("Expires", date).build();
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
        List<InputPart> inputParts= formData.get("url");
        if (inputParts != null) {

            vr = utils.doProcessURLForm(inputParts);
        }
        return vr;
    }

    private DocumentResource processStatementsInForm(Map<String, List<InputPart>> formData) {
        DocumentResource vr=null;
        List<InputPart> inputParts = formData.get("statements");
        List<InputPart> type = formData.get("type");
        if (inputParts != null) {

            vr = utils.doProcessStatementsForm(inputParts,
                    type);
        }
        return vr;
    }

    private void doLog(ServiceUtils.Action action, DocumentResource vr) {
        doLog(action.toString(),vr);
    }

    private void doLog(String action, DocumentResource vr) {
        logger.log(ProvLevel.PROV,
                "" + action + ","
                        + vr.getVisibleId() + ","
                        + vr.getStorageId());
    }



}
