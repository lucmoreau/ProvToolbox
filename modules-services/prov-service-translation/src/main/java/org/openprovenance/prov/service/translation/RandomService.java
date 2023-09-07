package org.openprovenance.prov.service.translation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.generator.GeneratorDetails;
import org.openprovenance.prov.generator.GraphGenerator;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.model.ProvUtilities;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Path("")
public class RandomService implements Constants, InteropMediaType {

    private final ServiceUtils utils;
    ProvUtilities u = new ProvUtilities();

    static Logger logger = LogManager.getLogger(RandomService.class);



    final ProvFactory f;

    final ActionTranslate actionTranslate;

	public RandomService(PostService postService) {
		this(postService, new LinkedList<>(),Optional.empty());
	}

    public RandomService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
		utils=postService.getServiceUtils();

		postService.addOtherPerformer(Optional.of((otherPerformer.orElse(new EmptyOtherActionPerformer()))));

        actionTranslate=new ActionTranslate(utils);
        f=utils.getProvFactory();

    }

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

        InteropFramework intF = new InteropFramework(utils.getProvFactory());
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



    

}
