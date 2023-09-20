package org.openprovenance.prov.service.narrative;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.service.core.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.openprovenance.prov.service.core.SwaggerTags.NARRATIVE;


@Path("")
@Tag(name=NARRATIVE)
public class NarrativeService  implements Constants, InteropMediaType, SwaggerTags {

    public final NarrativeLogic narrativeLogic ;

    public NarrativeService(PostService postService) {
        this(postService, new LinkedList<>(), Optional.empty());

    }

    public NarrativeService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        this.narrativeLogic= new NarrativeLogic(postService, performers, otherPerformer);
    }


    @GET
    @Path("/documents/{docId}/random_narrative")
    @Tag(name= NARRATIVE)
    @Operation(summary = "Random Narrative of Document", description = "",
           // , responseClass = "org.openprovenance.prov.xml.Document"
           responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response random_nar(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "text/plain,text/xml",
                              required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        return narrativeLogic.random_nar(response, request, msg, accept);
    }


    @GET
    @Path("/documents/{docId}/explanation/{expid}")
    @Tag(name= NARRATIVE)
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

        return narrativeLogic.explanations(response, request, msg, explanationId, accept);
    }

    @GET
    @Path("/documents/{docId}/explanationdetails/{expid}")
    @Tag(name=NARRATIVE)
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


        return narrativeLogic.explanationDetails(response, request, msg, explanationId, accept);
    }

    @GET
    @Path("/documents/{docId}/animation.svg")
    @Tag(name=NARRATIVE)
    @Produces({InteropMediaType.MEDIA_IMAGE_SVG_XML})
    @Operation(summary = "SVG for animation from Document", description = "",
              responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response svg4anim(@Context HttpServletResponse ignoredResponse,
                             @Context Request ignoredRequest,
                             @PathParam("docId") String msg,
                             @Parameter(name = HEADER_PARAM_ACCEPT,
                                         description = "Accept header parameter",
                                         example = "text/plain,text/xml",
                                         required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String ignoredAccept) throws IOException {

        return narrativeLogic.svg4anim(ignoredResponse, ignoredRequest, msg, ignoredAccept);
    }


    @GET
    @Path("/documents/{docId}/explanationdetails/{expid}/config")
    @Tag(name=NARRATIVE)
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Configuration of Explanation for Document", description = "",
            responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationDetailsConfig(@Context HttpServletResponse response,
                                             @Context Request request,
                                            @PathParam("docId") String msg,
                                            @PathParam("expid") String explanationId) {
        return narrativeLogic.explanationDetailsConfig(response, request, msg, explanationId);
    }

    @GET
    @Path("/documents/{docId}/explanation/{expid}/config")
    @Tag(name=NARRATIVE)
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Configuration of Explanation for Document", description = "",
              responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response explanationConfig(@Context HttpServletResponse response,
                                      @Context Request request,
                                      @PathParam("docId") String msg,
                                      @PathParam("expid") String explanationId) {


        return narrativeLogic.explanationConfig(response, request, msg, explanationId);
    }

    @POST
    @Path("/documents/{docId}/explanation/")
    @Tag(name=NARRATIVE)
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
        return narrativeLogic.explanationsForDoc(response, request, msg, tap, uriInfo, accept);
    }

    @POST
    @Path("/documents/{docId}/explanationdetails/")
    @Tag(name=NARRATIVE)
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
        return narrativeLogic.explanationsDetailsForDoc(response, request, msg, tap, uriInfo, accept);
    }



    
    @GET
    @Path("/documents/{docId}/linear_narrative")
    @Tag(name=NARRATIVE)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Operation(summary = "Linear Narrative of Document", description = "",
               responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response linear_narrative(@Context HttpServletResponse response,
                                     @Context Request request,
                                     @PathParam("docId") String msg,
                                     @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "text/plain,text/xml",
                                             required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        return narrativeLogic.linear_narrative(response, request, msg, accept);
    }





}
