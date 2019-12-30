package org.openprovenance.prov.service.translation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.translation.memory.TemplateResourceIndexInMemory;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.openprovenance.prov.template.expander.Expand;
import org.openprovenance.prov.xml.ProvUtilities;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TemplateService  implements Constants, InteropMediaType {

    ProvUtilities u = new ProvUtilities();

    static Logger logger = Logger.getLogger(TemplateService.class);

    final ActionExpand actionExpand;

    private final ServiceUtils utils;

    static final ProvFactory f=org.openprovenance.prov.xml.ProvFactory.getFactory();


    public TemplateService (PostService postService) {
        this(postService, new LinkedList<>(), Optional.empty());
    }

    public TemplateService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        utils=postService.getServiceUtils();
        logger.info("FIXME FIXME: registering template index (in memory)");
        utils.getExtensionMap().put(TemplateResource.getResourceKind(), TemplateResourceIndexInMemory.factory);
        postService.addToPerformers(PostService.addToList(new ActionExpand(utils),performers));
        postService.addOtherPerformer(Optional.of((otherPerformer.orElse(new EmptyOtherActionPerformer()))));
        actionExpand=new ActionExpand(utils);
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

    @GET
    @Path("/bindings/{name}")
    @Produces({ MEDIA_APPLICATION_JSON})

    public Response getBindings(@Context HttpServletResponse response,
                                @Context Request request,
                                @PathParam("name") String name,
                                @Context UriInfo info) throws IOException {

        final MultivaluedMap<String, String> valueMap = info.getQueryParameters();

        String bindingsUrl="https://nms.kcl.ac.uk/luc.moreau/dev/bindings/" + name + ".json";

        JsonNode bindings_schema = mapper.readTree(new URL(bindingsUrl));

        expandBindingsSchema(bindings_schema, valueMap);

        return utils.composeResponseOK(bindings_schema).build();
    }


    public void expandBindingsSchema(JsonNode bindings_schema,
                                     final MultivaluedMap<String, String> valueMap) {
        JsonNode the_var=bindings_schema.get("var");
        JsonNode the_context=bindings_schema.get("context");

        for (String key: valueMap.keySet()) {
            String value=valueMap.getFirst(key);
            JsonNode locatedNode=the_var.path(key);

            if (locatedNode!=null) {
                //System.out.println(locatedNode);
                for (int i=0; i<locatedNode.size(); i++) {
                    Object o=locatedNode.get(i);
                    if (o instanceof ObjectNode) {
                        ObjectNode locatedNode_i=(ObjectNode)locatedNode.get(i);
                        replaceValueInNodeWithTag(value, locatedNode_i,"@id");
                        replaceValueInNodeWithTag(value, locatedNode_i,"@value");
                        replaceValueInNodeWithTag(value, locatedNode_i,"@type");
                    }
                    // TODO: expansion for attributes
                }
            }
        }

        for (String key: valueMap.keySet()) {
            String value=valueMap.getFirst(key);
            replaceValueInNodeWithTag(value, (ObjectNode)the_context,key);
        }
    }


    public void replaceValueInNodeWithTag(String value, ObjectNode locatedNode, String tag) {
        if (locatedNode==null) return;
        JsonNode the_locatedValue=locatedNode.get(tag);
        if (the_locatedValue!=null) {
            if (the_locatedValue.isTextual()) {
                String s=the_locatedValue.textValue();
                String s2=s.replace("*",value);
                if (!(s.equals(s2))) {
                    locatedNode.put(tag,s2);
                }
            }
            System.out.println(locatedNode);
        }
    }


    @GET
    @Path("/documents/bindings/{name}")
    @Produces({ MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
            MEDIA_APPLICATION_RDF_XML, MEDIA_APPLICATION_JSON, MEDIA_IMAGE_SVG_XML, MEDIA_APPLICATION_PDF,MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG})
    public Response getDocumentsFromBindingsSchema(@Context HttpServletResponse response,
                                                   @Context Request request,
                                                   @PathParam("name") String name,
                                                   @Context UriInfo info) throws IOException {

        final MultivaluedMap<String, String> valueMap = info.getQueryParameters();

        String bindingsUrl="https://nms.kcl.ac.uk/luc.moreau/dev/bindings/" + name + ".json";

        JsonNode bindings_schema = mapper.readTree(new URL(bindingsUrl));

        expandBindingsSchema(bindings_schema, valueMap);

        String the_template=bindings_schema.get("template").asText();

        boolean allExpanded=false;
        ProvFactory pFactory=org.openprovenance.prov.xml.ProvFactory.getFactory();
        boolean addOrderp=false;

        Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);
        Document expanded;
        Document document;

        InteropFramework interop = new InteropFramework();

        document= (Document) interop.readDocument(the_template);

        Bindings bb= BindingsJson.fromBean(BindingsJson.importBean(bindings_schema),pFactory);

        expanded = myExpand.expander(document,
                bb);

        return utils.composeResponseOK(expanded).build();

    }


}
