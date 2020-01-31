package org.openprovenance.prov.service.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.openprovenance.prov.generator.GeneratorDetails;
import org.openprovenance.prov.generator.GraphGenerator;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.service.core.Constants;
import org.openprovenance.prov.service.core.PostService;
import org.openprovenance.prov.service.core.ServiceUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class VisService implements Constants, InteropMediaType {

    ProvUtilities u = new ProvUtilities();

    static Logger logger = Logger.getLogger(VisService.class);


    private final ServiceUtils utils;

    final ProvFactory f;

    public VisService(PostService postService) {
        this.utils=postService.getServiceUtils();
        this.f=utils.getProvFactory();
    }



    public final String HIVE_SERVICE = "http://provenance.ecs.soton.ac.uk/vis/hive/";

    @GET
    @Path("/documents/{docId}/vis/hive")
    @Produces(MEDIA_TEXT_HTML)
    @Tag(name="vis")
    @Operation(summary = "Visualization: hive plot", description = "Redirects to visualization tool page")
    public Response visHive(@Context HttpServletResponse response,
                            @PathParam("docId") String msg,
                            @Context HttpServletRequest request)
            throws ServletException,
            IOException {

        return utils.composeResponseSeeOther(
                HIVE_SERVICE
                        + "?url="
                        + ServiceUtils.getRequestURL(request,
                        PROVAPI,
                        msg,
                        ".json")).build();
    }

    public final String WHEEL_SERVICE = "http://provenance.ecs.soton.ac.uk/vis/wheel/";

    @GET
    @Path("/documents/{docId}/vis/wheel")
    @Produces(MEDIA_TEXT_HTML)
    @Tag(name="vis")
    @Operation(summary = "Visualization: wheel plot", description = "Redirects to visualization tool page")
    public Response visWheel(@Context HttpServletResponse response,
                             @PathParam("docId") String msg,
                             @Context HttpServletRequest request)
            throws ServletException,
            IOException {

        return utils.composeResponseSeeOther(
                WHEEL_SERVICE
                        + "?url="
                        + ServiceUtils.getRequestURL(request,
                        PROVAPI,
                        msg,
                        ".json")).build();
    }

    public final String GANTT_SERVICE = "http://provenance.ecs.soton.ac.uk/vis/gantt/";

    @GET
    @Path("/documents/{docId}/vis/gantt")
    @Produces(MEDIA_TEXT_HTML)
    @Tag(name="vis")
    @Operation(summary = "Visualization: gantt plot", description = "Redirects to visualization tool page")
    public Response visGantt(@Context HttpServletResponse response,
                             @PathParam("docId") String msg,
                             @Context HttpServletRequest request)
            throws ServletException,
            IOException {

        return utils.composeResponseSeeOther(
                GANTT_SERVICE
                        + "?url="
                        + ServiceUtils.getRequestURL(request,
                        PROVAPI,
                        msg,
                        ".json")).build();

    }

    public static final String [] ALL_OUTPUT_MEDIA
            =new String[] { MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
            MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON, MEDIA_IMAGE_SVG_XML, MEDIA_APPLICATION_PDF, MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG};
    public static final String ALL_OUTPUT_MEDIA_AS_STRING= Arrays.toString(ALL_OUTPUT_MEDIA);

    @GET
    @Path("/documents/random/{nodes}/{degree}")
    @Produces({ MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
            MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON, MEDIA_IMAGE_SVG_XML, MEDIA_APPLICATION_PDF,MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG})
    @Tag(name="random")
    @Operation(summary = "Randomly generated Document",
            description = "Random generation of PROV document, with a set number of nodes and a maximum degree",
            responses = { @ApiResponse(responseCode = "200",
                    content={@Content(mediaType=MEDIA_TEXT_TURTLE),
                            @Content(mediaType=MEDIA_TEXT_PROVENANCE_NOTATION),
                            @Content(mediaType=MEDIA_APPLICATION_PROVENANCE_XML),
                            @Content(mediaType=MEDIA_APPLICATION_TRIG),
                            @Content(mediaType=MEDIA_APPLICATION_RDF_XML),
                            @Content(mediaType=MEDIA_APPLICATION_JSON),
                            @Content(mediaType=MEDIA_IMAGE_SVG_XML),
                            @Content(mediaType=MEDIA_IMAGE_PNG),
                            @Content(mediaType=MEDIA_IMAGE_JPEG),
                            @Content(mediaType=MEDIA_APPLICATION_PDF)}),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getRandom(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("nodes") Integer nodes,
                              @PathParam("degree") Integer degree) {
        return getRandom(response, request, nodes, degree, null);
    }


    @GET
    @Path("/documents/random/{nodes}/{degree}/{seed}")
    @Produces({ MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
            MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON, MEDIA_IMAGE_SVG_XML, MEDIA_APPLICATION_PDF,MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG})
    @Tag(name="random")
    @Operation(summary = "Randomly generated Document",
            description = "andom generation of PROV document, with a set number of nodes,  a maximum degree and a set seed for the random generato",
            responses = { @ApiResponse(responseCode = "200",
                    content={@Content(mediaType=MEDIA_TEXT_TURTLE),
                            @Content(mediaType=MEDIA_TEXT_PROVENANCE_NOTATION),
                            @Content(mediaType=MEDIA_APPLICATION_PROVENANCE_XML),
                            @Content(mediaType=MEDIA_APPLICATION_TRIG),
                            @Content(mediaType=MEDIA_APPLICATION_RDF_XML),
                            @Content(mediaType=MEDIA_APPLICATION_JSON),
                            @Content(mediaType=MEDIA_IMAGE_SVG_XML),
                            @Content(mediaType=MEDIA_IMAGE_PNG),
                            @Content(mediaType=MEDIA_IMAGE_JPEG),
                            @Content(mediaType=MEDIA_APPLICATION_PDF)}),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getRandom(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("nodes") Integer nodes,
                              @PathParam("degree") Integer degree,
                              @PathParam("seed") Long seed) {

        InteropFramework intF = new InteropFramework();
        List<Variant> vs = intF.getVariants();
        Variant v = request.selectVariant(vs);
        MediaType mt = v.getMediaType();


        GeneratorDetails gd=new GeneratorDetails(nodes, degree, GraphGenerator.FIRST_NODE_AS_ENTITY, "http://example.org/", seed, "e1");
        GraphGenerator gg=new GraphGenerator(gd, f);
        gg.generateElements();
        Document doc=gg.getDocument();
        Namespace.withThreadNamespace(doc.getNamespace());
        return utils.composeResponseOK(doc).type(mt).build();

    }

    static ObjectMapper mapper = new ObjectMapper();


}
