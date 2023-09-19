package org.openprovenance.prov.service.narrative;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import nlg.wrapper.Block;
import nlg.wrapper.Root;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.scala.nlg.RealiserFactory;
import org.openprovenance.prov.scala.nlgspec_transformer.SpecLoader;
import org.openprovenance.prov.scala.nlgspec_transformer.specTypes;
import org.openprovenance.prov.scala.wrapper.BlocklySerializer;
import org.openprovenance.prov.scala.wrapper.defs;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.vanilla.Document;
import scala.Function0;
import scala.Option;
import scala.Tuple3;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.reflect.ClassTag;
import simplenlg.framework.NLGElement;
import simplenlg.lexicon.XMLLexicon;
import simplenlg.realiser.english.Realiser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Path("")
@Tag(name="nlg")
public class NlgService implements Constants, InteropMediaType {

    ObjectMapper mapper=new ObjectMapper();

    private final ServiceUtils utils;

    public NlgService(PostService ps) {
        this.utils=new ServiceUtils(ps,ps.getServiceUtils().getConfig());
    }


    static final String PHRASE_EXAMPLE="{\n" +
            "  \"type\" : \"clause\",\n" +
            "  \"subject\" : {\n" +
            "    \"type\" : \"noun_phrase\",\n" +
            "    \"head\" : \"Luc\",\n" +
            "    \"modifiers\" : [ ],\n" +
            "    \"pre-modifiers\" : [ ],\n" +
            "    \"post-modifiers\" : [ ],\n" +
            "    \"specifier\" : null,\n" +
            "    \"complements\" : [ ],\n" +
            "    \"features\" : { }\n" +
            "  },\n" +
            "  \"verb\" : {\n" +
            "    \"type\" : \"verb_phrase\",\n" +
            "    \"head\" : \"eats\",\n" +
            "    \"modifiers\" : [ ],\n" +
            "    \"pre-modifiers\" : [ ],\n" +
            "    \"post-modifiers\" : [ ],\n" +
            "    \"features\" : { }\n" +
            "  },\n" +
            "  \"object\" : {\n" +
            "    \"type\" : \"coordinated_phrase\",\n" +
            "    \"coordinates\" : [ {\n" +
            "      \"type\" : \"noun_phrase\",\n" +
            "      \"head\" : \"cabbage\",\n" +
            "      \"modifiers\" : [ ],\n" +
            "      \"pre-modifiers\" : [ ],\n" +
            "      \"post-modifiers\" : [ ],\n" +
            "      \"specifier\" : null,\n" +
            "      \"complements\" : [ ],\n" +
            "      \"features\" : { }\n" +
            "    }, {\n" +
            "      \"type\" : \"noun_phrase\",\n" +
            "      \"head\" : \"lettuce\",\n" +
            "      \"modifiers\" : [ ],\n" +
            "      \"pre-modifiers\" : [ ],\n" +
            "      \"post-modifiers\" : [ ],\n" +
            "      \"specifier\" : null,\n" +
            "      \"complements\" : [ ],\n" +
            "      \"features\" : { }\n" +
            "    } ],\n" +
            "    \"conjunction\" : \"and\",\n" +
            "    \"features\" : {\n" +
            "      \"markup_element\" : \"ol\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"indirect_object\" : null,\n" +
            "  \"complements\" : [ ],\n" +
            "  \"modifiers\" : [ ],\n" +
            "  \"features\" : {\n" +
            "    \"markup_element\" : \"span\",\n" +
            "    \"markup_attributes\" : \"class=\\\"explanation\\\"\"\n" +
            "  }\n" +
            "}";


    static Logger logger = LogManager.getLogger(NlgService.class);


    static final Realiser realiserWithMarkups = defs.withMarkupFormatter();
    static final Realiser realiserDefault = defs.theRealiser();

    static XMLLexicon lexicon=new XMLLexicon();
    
    @POST
    @Path("/nlg/realiser/")
    @Tag(name="nlg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
    @Operation(summary = "Posts a sentence description. Payload is 'Phrase' in json format. ",
               description = "Construct english sentence from sentence description",
           requestBody =  @RequestBody (content = { @Content( mediaType = MediaType.APPLICATION_JSON,
                                                              examples =  { @ExampleObject(name="cabbage", summary="a simple example", value=PHRASE_EXAMPLE) } ) }),
           responses = { @ApiResponse(   responseCode = "200",
                                         content={ @Content(mediaType=MEDIA_TEXT_HTML),
                                                   @Content(mediaType=MEDIA_TEXT_PLAIN),
                                                   @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                         @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nlgRealiser(@Context HttpServletResponse response,
                                @Context Request request,
                                @Parameter(name = "phrase",
                                          description = "description of sentence to be constructed",
                                          required=true)
                                defs.Phrase phrase,
                                @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        System.out.println(phrase);

        NLGElement spec= (NLGElement) phrase.expand();

        System.out.println(spec.printTree("  "));

        switch(accept){
            case MediaType.APPLICATION_JSON: {
                logger.debug("producing json");
                String result = defs.realise(spec, realiserWithMarkups);
                HashMap<String, String> map= new HashMap<>();
                map.put("result",result);
                return utils.composeResponseOK(map).type(accept).build();
            }
            case MediaType.TEXT_HTML: {
                logger.debug("producing html");
                String result = defs.realise(spec, realiserWithMarkups);
                return utils.composeResponseOK(result).type(accept).build();
            }
            case MediaType.TEXT_PLAIN: {
                logger.debug("producing plain text");
                String result = realiserDefault.realiseSentence(spec);
                return utils.composeResponseOK(result).type(accept).build();
            }
            default: throw new UnsupportedOperationException("media type not supported: " + accept);

        }

    }



    @POST
    @Path("/nlg/expander/")
    @Tag(name="nlg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
    @Operation(summary = "Posts a sentence description. Payload is 'Phrase' in json format. ",
            description = "Construct english sentence from sentence description",
            requestBody =  @RequestBody (content = { @Content( mediaType = MediaType.APPLICATION_JSON,
                    examples =  { @ExampleObject(name="cabbage", summary="a simple example", value=PHRASE_EXAMPLE) } ) }),
            responses = { @ApiResponse(   responseCode = "200",
                    content={ @Content(mediaType=MEDIA_TEXT_HTML),
                            @Content(mediaType=MEDIA_TEXT_PLAIN),
                            @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nlgExpander(@Context HttpServletResponse response,
                                @Context Request request,
                                @Parameter( name = "phrase",
                                            description = "description of sentence to be expanded",
                                            required=true)
                                            //org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase phrase,
                                            PhraseEnvironment phraseEnvironment,
                                @HeaderParam(HEADER_PARAM_ACCEPT) String accept,
                                @HeaderParam("prov_xpand_only") String expandOnlyParam) {

        try {

            Document doc=phraseEnvironment.getDocument();
            String [] query={
                    "prefix cs <http://openprovenance.org/ns/creditscoring#>",
                    "prefix pl <http://openprovenance.org/ns/plead#>",
                    "prefix app <https://explain.openprovenance.org/creditscoring/>",
                    "select * from decision a prov:Entity",
                    "from gen a prov:WasGeneratedBy",
                    " join decision.id = gen.entity",
                    "from processing a prov:Activity",
                    " join gen.activity = processing.id",
                    "from usd a prov:Used",
                    " join processing.id = usd.activity",
                    "from application a prov:Entity",
                    " join usd.entity = application.id",
                    "from wat1 a prov:WasAttributedTo",
                    " join application.id = wat1.entity",
                    "from applicant a prov:Agent",
                    " join wat1.agent = applicant.id",
                    "where decision[prov:type] >= 'pl:RejectionDecision'",
                    "and application[prov:type] >= 'pl:Request'",
                    "and applicant[prov:type] >= 'pl:DataSubject'" };

            specTypes.Phrase phrase=phraseEnvironment.getPhrase();


            specTypes.TransformEnvironment te
                    = specTypes.convertToTransformEnvironment(phraseEnvironment.getDocument(),
                                                                phraseEnvironment.getDictionary(),
                                                                phraseEnvironment.getContext(),
                                                                phraseEnvironment.getProfiles(),
                                                                phraseEnvironment.getTheProfile());

            System.out.println(phrase);
            boolean expandOnly=false;
            if (("true".equals(expandOnlyParam) || "TRUE".equals(expandOnlyParam))) {
                expandOnly=true;
            }

            // TODO: check if this works
            ClassTag<specTypes.Phrase> tag = scala.reflect.ClassTag$.MODULE$.apply(te.getClass());
            Option<specTypes.Phrase> transformedPhrase=phrase.transform(te, tag);

            if (!transformedPhrase.isDefined()) {
                return utils.composeResponseOK("empty").type(MediaType.APPLICATION_JSON_TYPE).build();
            } else {


                specTypes.Phrase phrase2 = transformedPhrase.get();

                if (expandOnly) {
                    System.out.println(phrase2);
                    StreamingOutput promise = (out) -> SpecLoader.phraseExport(out, phrase2);
                    return utils.composeResponseOK(promise).type(MediaType.APPLICATION_JSON_TYPE).build();
                }

                NLGElement spec = (NLGElement) phrase2.expand();

                System.out.println(spec.printTree("  "));

                switch (accept) {
                    case MediaType.APPLICATION_JSON: {
                        logger.debug("producing json");
                        String result = defs.realise(spec, realiserWithMarkups);
                        HashMap<String, String> map = new HashMap<>();
                        map.put("result", result);
                        return utils.composeResponseOK(map).type(accept).build();
                    }
                    case MediaType.TEXT_HTML: {
                        logger.debug("producing html");
                        String result = defs.realise(spec, realiserWithMarkups);
                        return utils.composeResponseOK(result).type(accept).build();
                    }
                    case MediaType.TEXT_PLAIN: {
                        logger.debug("producing plain text");
                        String result = realiserDefault.realiseSentence(spec);
                        return utils.composeResponseOK(result).type(accept).build();
                    }
                    default:
                        throw new UnsupportedOperationException("media type not supported: " + accept);

                }
            }
        } catch (Throwable re) {
            return utils.composeResponseInternalServerError("issue with expansion", re);
        }
    }




    @POST
    @Path("/nlg/expander2/")
    @Tag(name="nlg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
    @Operation(summary = "Posts a sentence description. Payload is 'Phrase' in json format. ",
            description = "Construct english sentence from sentence description",
            requestBody =  @RequestBody (content = { @Content( mediaType = MediaType.APPLICATION_JSON,
                    examples =  { @ExampleObject(name="cabbage", summary="a simple example", value=PHRASE_EXAMPLE) } ) }),
            responses = { @ApiResponse(   responseCode = "200",
                    content={ @Content(mediaType=MEDIA_TEXT_HTML),
                            @Content(mediaType=MEDIA_TEXT_PLAIN),
                            @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nlgExpanderWithQuery(@Context HttpServletResponse response,
                                @Context Request request,
                                @Parameter( name = "phrase",
                                        description = "description of sentence to be expanded",
                                        required=true)
                                        //org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase phrase,
                                        PhraseEnvironment phraseEnvironment,
                                @HeaderParam(HEADER_PARAM_ACCEPT) String accept,
                                @HeaderParam("prov_xpand_only") String expandOnlyParam) {

        try {


            final specTypes.Phrase the_request_phrase=phraseEnvironment.getPhrase();
            final scala.collection.immutable.Map<String, String> the_request_context = phraseEnvironment.getContext();
            final Document the_request_document = phraseEnvironment.getDocument();
            final String the_request_query = phraseEnvironment.getQuery();
            final scala.collection.immutable.Map<String, scala.collection.immutable.Map<String, String>> the_request_select = phraseEnvironment.getSelect();
            final scala.collection.immutable.Map<String, Object> the_request_profiles = phraseEnvironment.getProfiles();
            final org.openprovenance.prov.scala.nlgspec_transformer.defs.Dictionary the_request_dictionary=phraseEnvironment.getDictionary();
            final String the_request_the_profile = phraseEnvironment.getTheProfile();
            final int the_request_format=phraseEnvironment.getFormat();


            ProvFactory pf=new ProvFactory();

            org.openprovenance.prov.scala.immutable.Document document=pf.newDocument(the_request_document);


            org.openprovenance.prov.scala.nlgspec_transformer.defs.Template templateFromRequest
                    = new org.openprovenance.prov.scala.nlgspec_transformer.defs.Template("internalQuery",the_request_select, the_request_query, the_request_phrase, the_request_context, null);
            //logger.debug("templateFromQuery " + templateFromRequest);


            RealiserFactory factory=new RealiserFactory(templateFromRequest,the_request_dictionary,the_request_profiles);



            RealiserFactory.Realiser realiser=factory.make(document);

            //logger.debug("realiser " + realiser);


            Seq<Tuple3<String, String, Function0<String>>> realisations
                    =realiser.realise_plan(templateFromRequest, the_request_the_profile,the_request_format);

            //logger.debug("realisations " + realisations);

            Option<Tuple3<String, String, Function0<String>>> head=realisations.headOption();

            //logger.debug("head " + head);

            if (head.nonEmpty()) {
                String theString=head.get()._1();

                switch (accept) {
                    case MediaType.APPLICATION_JSON: {
                        logger.debug("producing json");

                        HashMap<String, String> map = new HashMap<>();
                        map.put("result", theString);
                        return utils.composeResponseOK(map).type(accept).build();
                    }
                    case MediaType.TEXT_HTML: {
                        logger.debug("producing html");
                        return utils.composeResponseOK(theString).type(accept).build();
                    }
                    case MediaType.TEXT_PLAIN: {
                        logger.debug("producing plain text");
                        return utils.composeResponseOK(theString).type(accept).build();
                    }
                    default:
                        throw new UnsupportedOperationException("media type not supported: " + accept);

                }
            } else {
                return utils.composeResponseOK("empty").type(MediaType.APPLICATION_JSON_TYPE).build();
            }
        } catch (Throwable re) {
            return utils.composeResponseInternalServerError("issue with expansion", re);
        }
    }

    final ObjectMapper objectMapper = new ObjectMapper();

    public Map doPost(String url, String requestBody) throws IOException {
        // @Deprecated HttpClient httpClient = new DefaultHttpClient();
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(url);
        StringEntity responseBody = new StringEntity(requestBody);
        request.addHeader("content-type",  "application/json");
        request.addHeader("accept",  "application/json");
        request.setEntity(responseBody);
        HttpResponse response = httpClient.execute(request);
        return objectMapper.readValue(response.getEntity().getContent(), Map.class);

    }


    @GET
    @Path("/authenticate")
    @Tag(name="nlg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response authenticate(@Context HttpServletResponse response,

                                 @Context HttpServletRequest request,
                                 @QueryParam("code") String code) throws ServletException, IOException {

        logger.debug("Authenticate: received code "+ code);

        Map<String,String> m=new HashMap<>();
        m.put("client_id","Iv1.c3c8c66c0772c07b");
        m.put("client_secret", "55baa57aaf140aaa5db05ecfbc32a994c61c9dbd");
        m.put("code", code);

        final String requestBody = objectMapper.writeValueAsString(m);

        logger.debug("Getting access token: "+ requestBody);

        Map result=doPost("https://github.com/login/oauth/access_token", requestBody);


        logger.debug("Obtained " + result);

        String access_token= (String) result.get("access_token");

        logger.debug("Obtained access_token " + access_token);

        logger.debug("Authenticate: now redirecting ");

        return utils.composeResponseSeeOther("../../xplain/view/editor?github_access_token="+access_token).build();


    }


    @POST
    @Path("/xplan")
    @Tag(name="nlg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_XML)

    public Response blockly(@Context HttpServletResponse response,
                            @Context HttpServletRequest request,
                            specTypes.Phrase phrase) throws ServletException, IOException {

        logger.debug("toBlocky");


        Root root;
        if (phrase instanceof org.openprovenance.prov.scala.nlgspec_transformer.defs.Paragraph) {
            List<specTypes.Phrase> ll= JavaConverters.seqAsJavaList(((org.openprovenance.prov.scala.nlgspec_transformer.defs.Paragraph)phrase).items());
            List<Block>res=new LinkedList<>();
            for (specTypes.Phrase p: ll) {
                res.add(p.toBlockly());
            }
            root=new Root(res);

        } else {
            root=new Root(phrase.toBlockly());
        }
        StreamingOutput promise = (out) -> new BlocklySerializer(true).serialiseBlock(out, root, true);
        return utils.composeResponseOK(promise).type(MediaType.TEXT_XML).build();

    }



    @POST
    @Path("/conversion")
    @Tag(name="nlg")
    @Consumes({ MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_APPLICATION_JSONLD,
                MEDIA_APPLICATION_JSON})
    @Produces({ MEDIA_TEXT_PROVENANCE_NOTATION,
                MEDIA_APPLICATION_JSON, MEDIA_APPLICATION_JSONLD,
                MEDIA_IMAGE_SVG_XML, MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG, MEDIA_APPLICATION_PDF })

    public Response conversion(@Context HttpServletResponse response,
                               @Context Request request,
                               Document document) throws ServletException, IOException {

        logger.debug("conversion");
        org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();
        InteropFramework interop=new InteropFramework(factory);

        List<Variant> vs = interop.getVariants();
        Variant v = request.selectVariant(vs);

        if (v == null) {
            return utils.composeResponseNotAcceptable(vs);
        } else {
            String mediaType = v.getMediaType().toString();
            StreamingOutput promise = (out) -> interop.serialiseDocument(out,document,mediaType,true);
            return utils.composeResponseOK(promise).type(mediaType).build();
        }
    }


}






