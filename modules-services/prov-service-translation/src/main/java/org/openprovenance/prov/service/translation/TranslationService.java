package org.openprovenance.prov.service.translation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.openprovenance.prov.interop.InteropMediaType;


@Path("")
//@Api(value = "", description = "Provenance API")
public class TranslationService implements Constants, InteropMediaType {
	
    static Logger logger = Logger.getLogger(TranslationService.class);
    static final TranslationServiceUtils utils=new TranslationServiceUtils();


	public TranslationService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
		postService.addToPerformers(PostService.addToList(new ActionTranslate(new ServiceUtils()), performers));
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
                                              @Content(mediaType=MEDIA_APPLICATION_RDF_XML),
                                              @Content(mediaType=MEDIA_APPLICATION_JSON),
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
        MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON,
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
                                                     schema=@Schema(allowableValues={"json","ttl","provn","provx","rdf","trig","svg","png","pdf","jpg","jpeg"}), required = true) @PathParam("type") String type)
                                                  throws FileNotFoundException,
                                                  JAXBException,
                                                  IOException,
                                                  ServletException {
        logger.debug("translate to " + type);


        if ((type == null) || (!translationExtensions.contains(type))) {
            return utils.composeResponseNotFOUND("Not supported serialization type " + type
                                                 + " for resource : " + msg);
        }

        DocumentResource vr = DocumentResource.table.get(msg);

        if (vr == null) {
            return utils.composeResponseNotFoundResource(msg);
        }

        if (vr.document == null) {
            return utils.composeResponseNotFoundDocument(msg);
        }

        vr.dotFilepath = vr.graphpath + ".dot";
        vr.svgFilepath = vr.graphpath + ".svg";
        vr.pdfFilepath = vr.graphpath + ".pdf";

        if (respondWithFile && vr.document instanceof org.openprovenance.prov.xml.Document) {

            return getResponseThroughFile(type, vr);
        } else {
            InteropFramework intF = new InteropFramework();
            String mimeType = intF.convertExtensionToMediaType(type);
            return utils.composeResponseOK(vr.document).type(mimeType).build();
        }
    }

    final boolean respondWithFile=false;

    public Response getResponseThroughFile(String type, DocumentResource vr) {
        String outFile = vr.graphpath + "." + type;
        InteropFramework intF = new InteropFramework();
        intF.writeDocument(outFile, vr.document);

        File f = new File(outFile);

        String mimeType = intF.convertExtensionToMediaType(type);
        logger.debug("setting mimeType " + mimeType + " for " + type);

        return utils.composeResponseOK((Object) f).type(mimeType).build();
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
                                       @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg)
                                               throws FileNotFoundException,
																	JAXBException,
																	IOException,
																	ServletException {

	DocumentResource vr = DocumentResource.table.get(msg);

	if (vr == null) {
	    return utils.composeResponseNotFoundResource(msg);
	}

	if (vr.document == null) {
	    return utils.composeResponseNotFoundDocument(msg);
	}

	InteropFramework intf = new InteropFramework();

	String mimeType = intf.mimeTypeMap.get(vr.format);

	File f = new File(vr.filepath);

	logger.debug("setting mimeType " + mimeType + " for  original "
		+ vr.filepath);

	return utils.composeResponseOK((Object) f).type(mimeType).build();
    }




}
