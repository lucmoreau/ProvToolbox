package org.openprovenance.prov.service.translation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.translation.actions.ActionExpand;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;
import org.openprovenance.prov.template.core.Instantiater;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.template.core.Bindings;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.openprovenance.prov.service.core.SwaggerTags.TEMPLATE;

@Path("")
public class TemplateService  implements Constants, InteropMediaType, ApiUriFragments {

    ProvUtilities u = new ProvUtilities();

    private static Logger logger = LogManager.getLogger(TemplateService.class);

    final ActionExpand actionExpand;

    private final ServiceUtils utils;
    private final ResourceIndex<TemplateResource> resourceIndex;

    final ProvFactory provFactory;


    public TemplateService (PostService postService) {
        this(postService, new LinkedList<>(), Optional.empty());
    }

    public TemplateService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        utils=postService.getServiceUtils();
        this.provFactory =utils.getProvFactory();
        postService.addToPerformers(PostService.addToList(new ActionExpand(utils),performers));
        postService.addOtherPerformer(Optional.of((otherPerformer.orElse(new EmptyOtherActionPerformer()))));
        actionExpand=new ActionExpand(utils);
        ResourceIndex<?> indexer=utils.getExtensionMap().get(TemplateResource.getResourceKind());
        this.resourceIndex=(ResourceIndex<TemplateResource>) indexer;

    }


    static ObjectMapper mapper = new ObjectMapper();

    @GET
    @Path("/bindings/{name}")
    @Produces({ MEDIA_APPLICATION_JSON})
    @Tag(name = TEMPLATE)

    public Response getBindings(@Context HttpServletResponse response,
                                @Context Request request,
                                @PathParam("name") String name,
                                @Context UriInfo info) throws IOException {

        final MultivaluedMap<String, String> valueMap = info.getQueryParameters();
        String bindingsUrl="https://nms.kcl.ac.uk/luc.moreau/dev/bindings/" + name + ".json";
        JsonNode bindings_schema = mapper.readTree(new URL(bindingsUrl));
        expandBindingsSchema(bindings_schema, valueMap);
        return ServiceUtils.composeResponseOK(bindings_schema).build();
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
    @Path(FRAGMENT_DOCUMENTS + "bindings/{name}")
    @Produces({ MEDIA_TEXT_TURTLE, MEDIA_TEXT_PROVENANCE_NOTATION,
            MEDIA_APPLICATION_PROVENANCE_XML, MEDIA_APPLICATION_TRIG,
             MEDIA_APPLICATION_JSON, MEDIA_IMAGE_SVG_XML, MEDIA_APPLICATION_PDF,MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG})
    @Tag(name = TEMPLATE)
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
        boolean addOrderp=false;

        Instantiater myInstantiater =new Instantiater(provFactory, addOrderp,allExpanded);

        InteropFramework interop = new InteropFramework();

        Document document= interop.readDocumentFromURL(the_template);

        Bindings bindings = Bindings.importBindings(bindings_schema);

        Document expanded = myInstantiater.instantiate(document, bindings);

        return ServiceUtils.composeResponseOK(expanded).build();

    }




    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/template.{type}")
    @Tag(name=TEMPLATE)
    @Operation(summary = "Representation of the template used to generate the current document into given serialization format",
            description = "No content negotiation allowed here. From a deployment of the service to the next, the actual serialization may change as translator library (ProvToolbox) may change.",
            responses = { @ApiResponse(responseCode = "200", description = "Representation of document"),
                          @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getTemplate(@Context HttpServletResponse response,
                                @Context HttpServletRequest request,
                                @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg,
                                @Parameter(name = "type", description = "serialization type", example = "provn",
                                        schema=@Schema(allowableValues={"json","ttl","provn","provx","rdf","trig","svg","png","pdf","jpg","jpeg"}), required = true) @PathParam("type") String type)
    throws IOException {

        logger.debug("Retrieving template");

        ResourceIndex<TemplateResource> index=resourceIndex.getIndex();

        try {
            TemplateResource tr = index.get(msg);


            if (tr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            String template_storageId = tr.getTemplateStorageId();

            Document doc = utils.getDocumentFromCacheOrStore(template_storageId);
            if (doc == null) {
                return utils.composeResponseNotFoundDocument(msg);
            }
            InteropFramework intF = new InteropFramework();
            String mimeType = intF.convertExtensionToMediaType(type);
            return ServiceUtils.composeResponseOK(doc).type(mimeType).build();
        } finally {
            index.close();
        }

    }


    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/bindings")
    @Tag(name=TEMPLATE)
    @Operation(summary = "Representation of the bindings used to generate the current document into given serialization format",
            description = "No content negotiation allowed here. From a deployment of the service to the next, the actual serialization may change as translator library (ProvToolbox) may change.",
            responses = { @ApiResponse(responseCode = "200", description = "Representation of document"),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response getBindings(@Context HttpServletResponse response,
                                @Context HttpServletRequest request,
                                @Parameter(name = "docId", description = "document id", required = true) @PathParam("docId") String msg)
            throws IOException {

        logger.debug("Retrieving template");

        ResourceIndex<TemplateResource> index=resourceIndex.getIndex();
        try {
            TemplateResource tr=index.get(msg);



            if (tr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            String bindings_Id=tr.getBindingsStorageId();
            logger.debug("Retrieving template, found bindings resource " + bindings_Id);


            StreamingOutput promise= out -> utils.getGenericResourceStorageMap().get(ActionExpand.BINDINGS_KEY).copyStoreToOutputStream(bindings_Id,out);


            return ServiceUtils.composeResponseOK(promise).type(MediaType.APPLICATION_JSON_TYPE).build();
        } finally {
            index.close();
        }
    }

}
