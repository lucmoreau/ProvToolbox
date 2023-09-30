package org.openprovenance.prov.service.validation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.translation.actions.ActionTranslate;
import org.openprovenance.prov.service.core.SwaggerTags;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.validation.Gensym;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.io.*;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.openprovenance.prov.service.validation.ActionValidate.REPORT_KEY;


@Path("")
public class ValidationService  implements Constants,InteropMediaType, SwaggerTags {


    public static final MediaType IMAGE_PNG_TYPE = MediaType.valueOf("image/png");


    static Logger logger = LogManager.getLogger(ValidationService.class);


    public static String VS = "http://openprovenance.org/validation/service";
    public static String VS_PREFIX = "vs";

    public static String VSI = "http://openprovenance.org/validation/service/instance";
    public static String VSI_PREFIX = "vsi";


    final ProvFactory f;
    final ValidationServiceUtils utils;

    final ActionValidate actionValidate;
    final ActionTranslate actionTranslate;

    final Namespace namespace = new Namespace();

    public ValidationService(PostService postService) {
        this(postService, new LinkedList<>(),Optional.empty());
    }

    public ValidationService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        f=postService.getServiceUtils().getProvFactory();
        utils=new ValidationServiceUtils(postService, f, new Namespace(),postService.getServiceUtils(),postService.getServiceUtils().getConfig());

        postService.addToPerformers(PostService.addToList(new ActionValidate(utils), performers));

        postService.addOtherPerformer(Optional.of((otherPerformer.orElse(new EmptyOtherActionPerformer()))));

        actionValidate=new ActionValidate(utils);
        actionTranslate=new ActionTranslate(utils);

        namespace.addKnownNamespaces();
        namespace.register(Gensym.VAL_PREFIX, "http://openprovenance.org/validation#");
        namespace.register(VS_PREFIX, VS);
        namespace.register(VSI_PREFIX, VSI);


    }


    @GET
    @Path("/documents/{docId}/validation/normalForm")
    @Tag(name=VALIDATION)
    @Operation(summary = "Document in normal form",
            description = "It is expected that validation has taken place.",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getNormalized(@Context HttpServletResponse response,
                                  @Context Request request,
                                  @PathParam("docId") String msg,
                                  @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "application/json,text/provenance-notation,application/provenance+xml,image/svg+xml,image/png,image/jpeg,application/pdf",
                                          // "text/html,text/turtle,,application/trig,",
                                          required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {

        return utils.contentNegotiationForDocument(request, msg, "/validation/normalForm.");

    }

    @GET
    @Path("/documents/{docId}/validation/normalForm.{type}")
    @Tag(name=VALIDATION)
    @Operation(summary = "Normal form of a document into given representation",
            description = "Validation expected to have taken place. No content negotiation allowed here",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getNormalFormAsType(@Context HttpServletResponse response,
                                        @Context HttpServletRequest request,
                                        @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg,
                                        @Parameter(name = "type", description = "serialization type", example = ALLOWABLE_OUTPUT_DOCUMENT_EXTENSIONS, required = true) @PathParam("type") String type)
            throws FileNotFoundException,
            IOException,
            ServletException {
        logger.debug("normal form to " + type);

        if ((type == null) || (!translationExtensions.contains(type))) {
            return utils.composeResponseNotFoundResource(msg);
        }

        DocumentResource vr = utils.getValidationResourceIndex().get(msg);

        if (vr == null) {
            return utils.composeResponseNotFoundResource(msg);
        }

        Object o = utils.normalizedDocument(msg);
        if (!(o instanceof Document)) {
            return (Response) o;
        }

        Document doc = (Document) o;


        String outFile = vr.getStorageId() + "-normalForm" + "." + type;
        logger.warn("TODO: need to call storage functionality for " + outFile);


        InteropFramework intF = new InteropFramework();
        intF.writeDocument(outFile, doc);

        File f = new File(outFile);

        String mimeType = intF.convertExtensionToMediaType(type);
        logger.debug("setting mimeType " + mimeType + " for " + type);

        return ServiceUtils.composeResponseOK(f).type(mimeType).build();

    }


    @GET
    @Path("/documents/{docId}/validation/report")
    @Tag(name=VALIDATION)
    @Operation(summary = "Validation Report",
            description = "Use content negotiation to select representation",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getValidationReport(@Context HttpServletResponse response,
                                        @Context Request request,
                                        @PathParam("docId") String msg,
                                        @Parameter(name = HEADER_PARAM_ACCEPT,
                                                description = "Accept header parameter",
                                                example = MEDIA_TEXT_HTML +  "," + MEDIA_APPLICATION_JSON,
                                                //"text/html,text/xml,application/xml",
                                                required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {
        // http://docs.oracle.com/javaee/6/tutorial/doc/gkqbq.html
        List<Variant> vs = Variant.mediaTypes(MediaType.TEXT_HTML_TYPE,
                MediaType.APPLICATION_JSON_TYPE).build();
        Variant v = request.selectVariant(vs);
        if (v == null) {
            return utils.composeResponseNotAcceptable(vs);
        } else {
            MediaType mt = v.getMediaType();
            if (mt.equals(MediaType.TEXT_HTML_TYPE)) {
                return utils.composeResponseSeeOther(
                        "view/documents/"
                                + msg
                                + "/validation/report.html").build();
            } else if (mt.equals(MediaType.APPLICATION_JSON_TYPE)) {
                return utils.composeResponseSeeOther(
                        "documents/"
                                + msg
                                + "/validation/report.json").build();
            } else {
                String result = "Found MediaType not supported " + mt;
                return utils.composeResponseNotFOUND(result);
            }
        }
    }

    @GET
    @Path("/documents/{docId}/validation/report.json")
    @Produces({ MEDIA_APPLICATION_JSON })
    @Tag(name=VALIDATION)
    @Operation(summary = "Validation Report -- JSON representation",
            description = "",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getValidationReportAsJson(@PathParam("docId") String msg)
    {

        final ResourceIndex<ValidationResource> index = utils.getValidationResourceIndex().getIndex();
        try {
            ValidationResource vr = index.get(msg);

            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }


            if (!vr.getCompleted()) {
                return utils.composeResponseNotFoundConstraintResource(msg);
            }

            StreamingOutput out = outputStream -> utils.getGenericResourceStorageMap().get(REPORT_KEY).copyStoreToOutputStream(vr.getJsonReportStorageId(), outputStream);

            return ServiceUtils.composeResponseOK(out).type(MEDIA_APPLICATION_JSON).build();
        } finally {
            index.close();
        }
    }

    public String stackTraceToString(Throwable thrown) {
        String result = "";
        while (thrown != null) {
            StringWriter stringWriter = new StringWriter();

            PrintWriter pw = new PrintWriter(stringWriter);
            thrown.printStackTrace(pw);
            result = result + stringWriter.toString() + "<p><p>";
            thrown = thrown.getCause();
        }
        result = result + "</pre>";
        return result;
    }

    @GET
    @Path("/documents/{docId}/validation/matrix")
    @Tag(name=VALIDATION)
    @Operation(summary = "Event Matrix",
            description = "It is expected that validation has taken place; note that this is a non information resource and content negotiation should be used to retrieve a suitable representation.",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getMatrix(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "accept header parameter", example = "image/png,text/plain", required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {
        // http://docs.oracle.com/javaee/6/tutorial/doc/gkqbq.html
        List<Variant> vs = Variant.mediaTypes(MediaType.TEXT_PLAIN_TYPE,
                IMAGE_PNG_TYPE).build();
        Variant v = request.selectVariant(vs);
        if (v == null) {
            return utils.composeResponseNotAcceptable(vs);
        } else {
            MediaType mt = v.getMediaType();
            if (mt.equals(IMAGE_PNG_TYPE)) {
                return utils.composeResponseSeeOther(
                        "documents/" + msg
                                + "/validation/matrix.png").build();
            } else if (mt.equals(MediaType.TEXT_PLAIN_TYPE)) {
                return utils.composeResponseSeeOther(
                        "documents/" + msg
                                + "/validation/matrix.txt").build();
            } else {
                return null;
            }
        }
    }

    @GET
    @Path("/documents/{docId}/validation/matrix.txt")
    @Produces(MEDIA_TEXT_PLAIN)
    @Tag(name=VALIDATION)
    @Operation(summary = "Event Matrix -- Text Representation",
            description = "It is expected that validation has taken place. No content negotiation applies here.",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getMatrixAsText(@PathParam("docId") String msg) throws IOException {

        final ResourceIndex<ValidationResource> index = utils.getValidationResourceIndex().getIndex();

        try {
            ValidationResource vr = index.get(msg);


            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }


            if (!vr.getCompleted()) {
                return utils.composeResponseNotFoundConstraintResource(msg);
            }

            logger.info("Returning matrix from file: " + vr.getMatrix());

            // FIXME: this does not work for the file system
            //	String matrix= (String) utils.getGenericResourceStorageMap().get(ActionValidate.MATRIX_KEY).deserializeObjectFromStore(vr.getMatrix());


            StreamingOutput promise = out -> utils.getGenericResourceStorageMap().get(ActionValidate.MATRIX_KEY).copyStoreToOutputStream(vr.getMatrix(), out);

            return ServiceUtils.composeResponseOK(promise).type(MediaType.TEXT_PLAIN).build();
        } finally {
            index.close();
        }
    }

    @GET
    @Path("/documents/{docId}/validation/matrix.png")
    @Produces("image/png")
    @Tag(name=VALIDATION)
    @Operation(summary = "Event Matrix -- Image Representation",
            description = "It is expected that validation has taken place. No content negotiation applies here.",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getMatrixAsPng(@PathParam("docId") String msg) 		throws java.io.IOException {

        String pngFilename = ServiceUtils.UPLOADED_FILE_PATH + msg + ".png";

        ValidationResource vr = utils.getValidationResourceIndex().get(msg);


        if (vr == null) {
            return utils.composeResponseNotFoundResource(msg);
        }

        if (!vr.getCompleted()) {
            return utils.composeResponseNotFoundConstraintResource(msg);
        }

        // FIXME: see for matrix.txt, return contents directly

        String matrixBase64= (String) utils.getGenericResourceStorageMap().get(ActionValidate.MATRIX_KEY).deserializeObjectFromStore(vr.getPngMatrix());

        return ServiceUtils.composeResponseOK(Base64.getDecoder().decode(matrixBase64)).type("image/png").build();
    }




}
