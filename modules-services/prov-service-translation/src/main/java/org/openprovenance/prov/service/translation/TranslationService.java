package org.openprovenance.prov.service.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.StreamingOutput;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.model.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.translation.actions.ActionMetrics;
import org.openprovenance.prov.service.translation.actions.ActionTranslate;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;



@Path("")
public class TranslationService implements Constants, InteropMediaType, SwaggerTags, ApiUriFragments {

    private static final Logger logger = LogManager.getLogger(TranslationService.class);
    private final ServiceUtils utils;


	private TranslationService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        utils=postService.getServiceUtils();
        postService.addToPerformers(PostService.addToList(new ActionTranslate(utils), performers));
        postService.addToPerformers(PostService.addToList(new ActionMetrics(utils), performers));
		postService.addOtherPerformer(otherPerformer);
	}


    public TranslationService(PostService postService) {
        this(postService, new LinkedList<>(),Optional.empty());
    }

    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId:  [a-zA-Z][a-zA-Z_0-9]*}")
    @Tag(name=DOCUMENTS)
    @Operation(summary = "Get Conceptual provenance document.  Use content negotiation to choose its representation", 
               description = "Content negotiation is expected to specify which representation to produce.  This is a non-information resource. Note that types are enumerated here for convenience: these are the types of the ProvToolbox, in addition of text/html used to access an html landing page",
               responses={
                       @ApiResponse( responseCode = "200", 
                                     content={//@Content(mediaType=MEDIA_TEXT_HTML),
                                              @Content(mediaType=MEDIA_TEXT_PROVENANCE_NOTATION),
                                              @Content(mediaType=MEDIA_APPLICATION_PROVENANCE_XML),
                                              @Content(mediaType=MEDIA_APPLICATION_JSON),
                                              @Content(mediaType=MEDIA_APPLICATION_JSONLD),
                                              @Content(mediaType=MEDIA_IMAGE_SVG_XML),
                                              @Content(mediaType=MEDIA_IMAGE_PNG),
                                              @Content(mediaType=MEDIA_IMAGE_JPEG),
                                              @Content(mediaType=MEDIA_APPLICATION_PDF)
				     }
                       ),
                       @ApiResponse( responseCode = "303", description = "See Other"),
                       @ApiResponse( responseCode = "404", description = DOCUMENT_NOT_FOUND),
                       @ApiResponse( responseCode = "406", description = "Not Acceptable")})
    @Produces({
            MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML,
            MEDIA_APPLICATION_JSON,
            MEDIA_APPLICATION_JSONLD,
            MEDIA_IMAGE_SVG_XML,
            MEDIA_IMAGE_PNG,
            MEDIA_IMAGE_JPEG,
            MEDIA_APPLICATION_PDF
    })

    public Response actionTranslate(@Context HttpServletResponse response,
                                    @Context Request request,
                                    @PathParam("docId") String msg) {
    	return utils.contentNegotiationForDocument(request, msg, ".");
    }
    




    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}.{extension}")
    @Tag(name=DOCUMENTS)
    @Operation(summary = "Representation of a document into given serialization format", 
               description = "No content negotiation allowed here. From a deployment of the service to the next, the actual serialization may change as translator library (ProvToolbox) may change.",
               responses = { @ApiResponse(responseCode = "200", description = "Representation of document"),
                             @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response actionTranslateAsType(@Context HttpServletResponse ignoredResponse,
                                          @Context HttpServletRequest ignoredRequest,
                                          @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String docId,
                                          @Parameter(
                                                  name = "extension",
                                                  description = "serialization type, expressed as file extension",
                                                  example = "provn",
                                                  schema=@Schema(allowableValues={"json", "provn", "provx", "jsonld", "svg", "png", "pdf", "jpg","jpeg"}),
                                                  required = true)
                                              @PathParam("extension") String extension,
                                          @Parameter(
                                                  name = HTTP_HEADER_PROVENANCE_ACCEPT_DATETIME_OPTION,
                                                  description = "clients preference for date encoding",
                                                  schema = @Schema(allowableValues={"PRESERVE","UTC","SYSTEM", "TIMEZONE"}))
                                              @HeaderParam(HTTP_HEADER_PROVENANCE_ACCEPT_DATETIME_OPTION) String datetimeOption,
                                          @HeaderParam(HTTP_HEADER_PROVENANCE_ACCEPT_TIMEZONE) String timeZone)
                                                  throws IOException {
        logger.debug("translate to " + extension + " with datetimeOption " + datetimeOption + " and timezone " + timeZone);
        //System.out.println("translate to " + extension + " with datetimeOption " + datetimeOption + " and timezone " + timeZone);


        if ((extension == null) || (!translationExtensions.contains(extension))) {
            return utils.composeResponseNotFOUND("Not supported serialization extension " + extension
                                                 + " for resource : " + docId);
        }

        ResourceIndex<DocumentResource> index=utils.getDocumentResourceIndex().getIndex();
        DocumentResource dr = index.get(docId);
        index.close();

        if (dr == null) {
            return utils.composeResponseNotFoundResource(docId);
        }
        return retrieveAndReturnDocument(docId, extension, dr, datetimeOption, timeZone);
    }

    final InteropFramework intF = new InteropFramework();

    public Response retrieveAndReturnDocument(String docId, String type, DocumentResource dr, String datetimeOption, String timeZone) throws IOException {
        DateTimeOption _dateTimeOption=(datetimeOption==null)?DateTimeOption.PRESERVE:DateTimeOption.valueOf(datetimeOption);
        TimeZone _timeZone=(timeZone==null)?null:TimeZone.getTimeZone(timeZone);

        Document doc;
        if (_dateTimeOption.equals(DateTimeOption.PRESERVE)) {
            doc=utils.getDocumentFromCacheOrStore(dr.getStorageId());
        } else {
            doc=utils.getDocumentFromStore(dr.getStorageId(), _dateTimeOption, _timeZone);
        }
        if (doc == null) {
            return utils.composeResponseNotFoundDocument(docId);
        }

        String mimeType = intF.convertExtensionToMediaType(type);
        Response.ResponseBuilder builder = ServiceUtils.composeResponseOK(doc).type(mimeType);
        builder.header(HTTP_HEADER_PROVENANCE_CONTENT_DATETIME_OPTION, _dateTimeOption);
        if (timeZone!=null) {
            builder.header(HTTP_HEADER_PROVENANCE_CONTENT_TIMEZONE, timeZone);
        }
        return builder.build();
    }

    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/original")
    @Tag(name=DOCUMENTS)
    @Operation(summary = "Original document, as posted in its original representation", 
               description = "No content negotiation allowed here. Mime type of result set to be the mime type of the original document.",
               responses = { @ApiResponse(responseCode = "200", description = "Representation of document"),
                             @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getOriginalDocumen(@Context HttpServletResponse response,
                                       @Context HttpServletRequest request,
                                       @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg) {

        ResourceIndex<DocumentResource> index=utils.getDocumentResourceIndex().getIndex();
        DocumentResource dr = index.get(msg);
        index.close();
        if (dr == null) {
            return utils.composeResponseNotFoundResource(msg);
        }
        String storageId = dr.getStorageId();
        String format= storageId.substring(storageId.lastIndexOf(".")+1).toUpperCase();
        String mimeType = intF.getMimeTypeMap().get(Formats.ProvFormat.valueOf(format));
        File f = new File(storageId);
        logger.debug("**** Reconstructing  mimeType " + mimeType + " for  original " + storageId);
        return ServiceUtils.composeResponseOK((Object) f).type(mimeType).build();
    }

    @GET
    @Path(FRAGMENT_METRICS+ "{docId}.json")
    @Tag(name=DOCUMENTS)
    @Produces(MEDIA_APPLICATION_JSON)
    @Operation(summary = "Metrics for document",
            description = "Metrics for document as a JSON object. The metrics are computed by the ProvToolbox. The metrics are not guaranteed to be stable from one version of the ProvToolbox to the next.",
            responses = { @ApiResponse(responseCode = "200", description = "Representation of document"),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getMetrics(@Context HttpServletResponse response,
                               @Context HttpServletRequest request,
                               @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String docId) throws IOException {

        ResourceIndex<DocumentResource> index=utils.getDocumentResourceIndex().getIndex();
        DocumentResource dr = index.get(docId);
        index.close();
        if (dr == null) {
            return utils.composeResponseNotFoundResource(docId);
        }
        DateTimeOption _dateTimeOption=DateTimeOption.PRESERVE;
        TimeZone _timeZone=null;

        Document doc;
        if (_dateTimeOption.equals(DateTimeOption.PRESERVE)) {
            doc=utils.getDocumentFromCacheOrStore(dr.getStorageId());
        } else {
            doc=utils.getDocumentFromStore(dr.getStorageId(), _dateTimeOption, _timeZone);
        }
        if (doc == null) {
            return utils.composeResponseNotFoundDocument(docId);
        }

        Rules rules=new Rules();
        Object o=rules.getMetrics(doc, utils.getProvFactory());

        StreamingOutput promise= out -> new ObjectMapper().writeValue(out, o);

        return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();


    }

}
