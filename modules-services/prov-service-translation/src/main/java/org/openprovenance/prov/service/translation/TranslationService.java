package org.openprovenance.prov.service.translation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Path("")
//@Api(value = "", description = "Provenance API")
public class TranslationService implements Constants, InteropMediaType {
	
    private static Logger logger = LogManager.getLogger(TranslationService.class);
    private final ServiceUtils utils;


	private TranslationService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        utils=postService.getServiceUtils();
        postService.addToPerformers(PostService.addToList(new ActionTranslate(utils), performers));
		postService.addOtherPerformer(otherPerformer);
	}


    public TranslationService(PostService postService) {
        this(postService, new LinkedList<>(),Optional.empty());
    }
	

    @GET
    @Path("/documents/{docId:  [a-zA-Z][a-zA-Z_0-9]*}")
    @Tag(name="documents")
    @Operation(summary = "Get Conceptual provenance document.  Use content negotiation to choose its representation", 
               description = "Content negotiation is expected to specify which representation to produce.  This is a non-information resource. Note that types are enumerated here for convenience: these are the types of the ProvToolbox, in addition of text/html used to access an html landing page",
               responses={
                       @ApiResponse( responseCode = "200", 
                                     content={//@Content(mediaType=MEDIA_TEXT_HTML),
                                              @Content(mediaType=MEDIA_TEXT_TURTLE),
                                              @Content(mediaType=MEDIA_TEXT_PROVENANCE_NOTATION),
                                              @Content(mediaType=MEDIA_APPLICATION_PROVENANCE_XML),
                                              @Content(mediaType=MEDIA_APPLICATION_TRIG),	
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
    @Produces({ MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
        MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
        MEDIA_APPLICATION_JSON, MEDIA_APPLICATION_JSONLD,
        MEDIA_IMAGE_SVG_XML, MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG, MEDIA_APPLICATION_PDF })

    public Response actionTranslate(@Context HttpServletResponse response,
                                    @Context Request request,
                                    @PathParam("docId") String msg) {
    	return utils.contentNegotiationForDocument(request, msg, ".");
    }
    




    @GET
    @Path("/documents/{docId}.{type}")
    @Tag(name="documents")
    @Operation(summary = "Representation of a document into given serialization format", 
               description = "No content negotiation allowed here. From a deployment of the service to the next, the actual serialization may change as translator library (ProvToolbox) may change.",
               responses = { @ApiResponse(responseCode = "200", description = "Representation of document"),
                             @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response actionTranslateAsType(@Context HttpServletResponse response,
                                          @Context HttpServletRequest request,
                                          @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg,
                                          @Parameter(name = "type", description = "serialization type", example = "provn", 
                                                     schema=@Schema(allowableValues={"json","ttl","provn","provx","trig","svg","png","pdf","jpg","jpeg", "jsonld"}), required = true) @PathParam("type") String type)
                                                  throws IOException {
        logger.debug("translate to " + type);


        if ((type == null) || (!translationExtensions.contains(type))) {
            return utils.composeResponseNotFOUND("Not supported serialization type " + type
                                                 + " for resource : " + msg);
        }

        ResourceIndex<DocumentResource> index=utils.getDocumentResourceIndex().getIndex();
        DocumentResource dr = index.get(msg);
        index.close();


        if (dr == null) {
            return utils.composeResponseNotFoundResource(msg);
        }

        return retrieveAndReturnDocument(msg, type, dr);

    }
    final InteropFramework intF = new InteropFramework();

    public Response retrieveAndReturnDocument(String msg, String type, DocumentResource dr) throws IOException {
        Document doc=utils.getDocumentFromCacheOrStore(dr.getStorageId());
        if (doc == null) {
            return utils.composeResponseNotFoundDocument(msg);
        }

        String mimeType = intF.convertExtensionToMediaType(type);
        return utils.composeResponseOK(doc).type(mimeType).build();
    }

    @GET
    @Path("/documents/{docId}/original")
    @Tag(name="documents")
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

       // if (dr.document() == null) {
       //     return utils.composeResponseNotFoundDocument(msg);
       // }


        String format=dr.getStorageId().substring(0, dr.getStorageId().lastIndexOf(".")+1);



        String mimeType = intF.mimeTypeMap.get(format); //TODO: fix me, domain is not a string

        File f = new File(dr.getStorageId());

        logger.debug("**** Reconstructing  mimeType " + mimeType + " for  original " + dr.getStorageId());

        return utils.composeResponseOK((Object) f).type(mimeType).build();
    }

}
