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
import scala.jdk.CollectionConverters;
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
import java.util.*;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.openprovenance.prov.service.core.ServiceUtils.*;
import static org.openprovenance.prov.service.core.SwaggerTags.NLG;


@Path("")
@Tag(name= NLG)
public class NlgService implements Constants, InteropMediaType, SwaggerTags {
    static final Logger logger = LogManager.getLogger(NlgService.class);
    private final ServiceUtils utils;
    private final String client_id;
    private final String client_secret;
    private final Map<String, String> config;

    public Map<String,String> defaultConfiguration=theDefaultConfiguration();

    public Map<String,String> theDefaultConfiguration() {
        Map<String,String> config=new HashMap<>();
        config.put(GITHUB_CLIENT_SECRET,     "**secret_needed_to_be_provided**");
        config.put(GITHUB_CLIENT_ID,      "Iv1.c3c8c66c0772c07b");
        return config;
    }

    public NlgService(PostService ps) {
        this.utils=new ServiceUtils(ps,ps.getServiceUtils().getConfig());
        Map<String,String> config=new HashMap<>();
        config.putAll(defaultConfiguration);
        config.putAll(loadConfigFromSystem(defaultConfiguration));
        config.putAll(loadConfigFromEnvironment(defaultConfiguration));
        logger.info("Configuration: " + config);
        System.out.println("Configuration: " + config);
        System.out.println("Configuration: " + GITHUB_CLIENT_SECRET);
        this.config=config;
        this.client_id=config.get(GITHUB_CLIENT_ID);
        this.client_secret=config.get(GITHUB_CLIENT_SECRET);
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




    static final Realiser realiserWithMarkups = defs.withMarkupFormatter();
    static final Realiser realiserDefault = defs.theRealiser();

    static XMLLexicon lexicon=new XMLLexicon();
    
    @POST
    @Path("/nlg/realiser/")
    @Tag(name= NLG)
    @Consumes(APPLICATION_JSON)
    @Produces({APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
    @Operation(summary = "Posts a sentence description. Payload is 'Phrase' in json format. ",
               description = "Construct english sentence from sentence description",
           requestBody =  @RequestBody (content = { @Content( mediaType = APPLICATION_JSON,
                                                              examples =  { @ExampleObject(name="cabbage", summary="a simple example", value=PHRASE_EXAMPLE) } ) }),
           responses = { @ApiResponse(   responseCode = "200",
                                         content={ @Content(mediaType=MEDIA_TEXT_HTML),
                                                   @Content(mediaType=MEDIA_TEXT_PLAIN),
                                                   @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                         @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nlgRealiser(@Context HttpServletResponse ignoredResponse,
                                @Context Request ignoredRequest,
                                @Parameter(name = "phrase",
                                          description = "description of sentence to be constructed",
                                          required=true)
                                defs.Phrase phrase,
                                @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        System.out.println(phrase);

        NLGElement spec= (NLGElement) phrase.expand();

        System.out.println(spec.printTree("  "));

        switch(accept){
            case APPLICATION_JSON: {
                logger.debug("producing json");
                String result = defs.realise(spec, realiserWithMarkups);
                HashMap<String, String> map= new HashMap<>();
                map.put("result",result);
                return ServiceUtils.composeResponseOK(map).type(accept).build();
            }
            case MediaType.TEXT_HTML: {
                logger.debug("producing html");
                String result = defs.realise(spec, realiserWithMarkups);
                return ServiceUtils.composeResponseOK(result).type(accept).build();
            }
            case MediaType.TEXT_PLAIN: {
                logger.debug("producing plain text");
                String result = realiserDefault.realiseSentence(spec);
                return ServiceUtils.composeResponseOK(result).type(accept).build();
            }
            default: throw new UnsupportedOperationException("media type not supported: " + accept);

        }

    }



    @POST
    @Path("/nlg/expander/")
    @Tag(name= NLG)
    @Consumes(APPLICATION_JSON)
    @Produces({APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
    @Operation(summary = "Posts a sentence description. Payload is 'Phrase' in json format. ",
            description = "Construct english sentence from sentence description",
            requestBody =  @RequestBody (content = { @Content( mediaType = APPLICATION_JSON,
                    examples =  { @ExampleObject(name="cabbage", summary="a simple example", value=PHRASE_EXAMPLE) } ) }),
            responses = { @ApiResponse(   responseCode = "200",
                    content={ @Content(mediaType=MEDIA_TEXT_HTML),
                            @Content(mediaType=MEDIA_TEXT_PLAIN),
                            @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nlgExpander(@Context HttpServletResponse ignoredResponse,
                                @Context Request ignoredRequest,
                                @Parameter( name = "phrase",
                                            description = "description of sentence to be expanded",
                                            required=true)
                                            //org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase phrase,
                                            PhraseEnvironment phraseEnvironment,
                                @HeaderParam(HEADER_PARAM_ACCEPT) String accept,
                                @HeaderParam("prov_xpand_only") String expandOnlyParam) {

        specTypes.Phrase phrase=phraseEnvironment.getPhrase();


        specTypes.TransformEnvironment te
                = specTypes.convertToTransformEnvironment(phraseEnvironment.getDocument(),
                                                            phraseEnvironment.getDictionary(),
                                                            phraseEnvironment.getContext(),
                                                            phraseEnvironment.getProfiles(),
                                                            phraseEnvironment.getTheProfile());

        System.out.println(phrase);
        boolean expandOnly= "true".equals(expandOnlyParam) || "TRUE".equals(expandOnlyParam);

        // TODO: check if this works
        ClassTag<specTypes.Phrase> tag = scala.reflect.ClassTag$.MODULE$.apply(te.getClass());
        Option<specTypes.Phrase> transformedPhrase=phrase.transform(te, tag);

        if (!transformedPhrase.isDefined()) {
            return ServiceUtils.composeResponseOK("empty").type(MediaType.APPLICATION_JSON_TYPE).build();
        } else {


            specTypes.Phrase phrase2 = transformedPhrase.get();

            if (expandOnly) {
                System.out.println(phrase2);
                StreamingOutput promise = (out) -> SpecLoader.phraseExport(out, phrase2);
                return ServiceUtils.composeResponseOK(promise).type(MediaType.APPLICATION_JSON_TYPE).build();
            }

            NLGElement spec = (NLGElement) phrase2.expand();

            System.out.println(spec.printTree("  "));

            switch (accept) {
                case APPLICATION_JSON: {
                    logger.debug("producing json");
                    String result = defs.realise(spec, realiserWithMarkups);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("result", result);
                    return ServiceUtils.composeResponseOK(map).type(accept).build();
                }
                case MediaType.TEXT_HTML: {
                    logger.debug("producing html");
                    String result = defs.realise(spec, realiserWithMarkups);
                    return ServiceUtils.composeResponseOK(result).type(accept).build();
                }
                case MediaType.TEXT_PLAIN: {
                    logger.debug("producing plain text");
                    String result = realiserDefault.realiseSentence(spec);
                    return ServiceUtils.composeResponseOK(result).type(accept).build();
                }
                default:
                    throw new UnsupportedOperationException("media type not supported: " + accept);

            }
        }
    }




    @POST
    @Path("/nlg/expander2/")
    @Tag(name= NLG)
    @Consumes(APPLICATION_JSON)
    @Produces({APPLICATION_JSON, MediaType.TEXT_HTML, MediaType.TEXT_PLAIN})
    @Operation(summary = "Posts a sentence description. Payload is 'Phrase' in json format. ",
            description = "Construct english sentence from sentence description",
            requestBody =  @RequestBody (content = { @Content( mediaType = APPLICATION_JSON,
                    examples =  { @ExampleObject(name="cabbage", summary="a simple example", value=PHRASE_EXAMPLE) } ) }),
            responses = { @ApiResponse(   responseCode = "200",
                    content={ @Content(mediaType=MEDIA_TEXT_HTML),
                            @Content(mediaType=MEDIA_TEXT_PLAIN),
                            @Content(mediaType=MEDIA_APPLICATION_JSON)}),
                    @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nlgExpanderWithQuery(@Context HttpServletResponse ignoredResponse,
                                         @Context Request ignoredRequest,
                                         @Parameter( name = "phrase",
                                                 description = "description of sentence to be expanded",
                                                 required=true)
                                         //org.openprovenance.prov.scala.nlgspec_transformer.specTypes.Phrase phrase,
                                         PhraseEnvironment phraseEnvironment,
                                         @HeaderParam(HEADER_PARAM_ACCEPT) String accept,
                                         @HeaderParam("prov_xpand_only") String ignoredExpandOnlyParam) {


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
                case APPLICATION_JSON: {
                    logger.debug("producing json");

                    HashMap<String, String> map = new HashMap<>();
                    map.put("result", theString);
                    return ServiceUtils.composeResponseOK(map).type(accept).build();
                }
                case MediaType.TEXT_HTML: {
                    logger.debug("producing html");
                    return ServiceUtils.composeResponseOK(theString).type(accept).build();
                }
                case MediaType.TEXT_PLAIN: {
                    logger.debug("producing plain text");
                    return ServiceUtils.composeResponseOK(theString).type(accept).build();
                }
                default:
                    throw new UnsupportedOperationException("media type not supported: " + accept);

            }
        } else {
            return ServiceUtils.composeResponseOK("empty").type(MediaType.APPLICATION_JSON_TYPE).build();
        }

    }

    final ObjectMapper objectMapper = new ObjectMapper();

    public Map<?,?> doPost(String url, String requestBody) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost request = new HttpPost(url);
        StringEntity responseBody = new StringEntity(requestBody);
        request.addHeader("content-type",  APPLICATION_JSON);
        request.addHeader("accept",  APPLICATION_JSON);
        request.setEntity(responseBody);
        HttpResponse response = httpClient.execute(request);
        return objectMapper.readValue(response.getEntity().getContent(), Map.class);

    }


    @GET
    @Path("/authenticate")
    @Tag(name= NLG)
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)

    public Response authenticate(@Context HttpServletResponse ignoredResponse,
                                 @Context HttpServletRequest ignoredRequest,
                                 @QueryParam("code") String code) throws IOException {

        logger.debug("Authenticate: received code "+ code);

        Map<String,String> m=new HashMap<>();
        m.put("client_id",client_id);
        m.put("client_secret", client_secret);
        m.put("code", code);

        final String requestBody = objectMapper.writeValueAsString(m);

        logger.debug("Getting access token: "+ requestBody);
        System.out.println("Getting access token: "+ requestBody);

        Map<?,?> result=doPost("https://github.com/login/oauth/access_token", requestBody);


        logger.debug("Obtained " + result);

        String access_token= (String) result.get("access_token");

        logger.debug("Obtained access_token " + access_token);

        logger.debug("Authenticate: now redirecting ");

        return utils.composeResponseSeeOther("../../xplain/view/editor?github_access_token="+access_token).build();
    }


    @POST
    @Path("/xplan")
    @Tag(name= NLG)
    @Consumes(APPLICATION_JSON)
    @Produces(MediaType.TEXT_XML)

    public Response blockly(@Context HttpServletResponse ignoredResponse,
                            @Context HttpServletRequest ignoredRequest,
                            specTypes.Phrase phrase) throws ServletException, IOException {

        logger.debug("toBlocky");


        Root root;
        if (phrase instanceof org.openprovenance.prov.scala.nlgspec_transformer.defs.Paragraph) {
            Collection<specTypes.Phrase> ll= CollectionConverters.IterableHasAsJava(((org.openprovenance.prov.scala.nlgspec_transformer.defs.Paragraph)phrase).items()).asJavaCollection();
            List<Block>res=new LinkedList<>();
            for (specTypes.Phrase p: ll) {
                res.add(p.toBlockly());
            }
            root=new Root(res);

        } else {
            root=new Root(phrase.toBlockly());
        }
        StreamingOutput promise = (out) -> new BlocklySerializer(true).serialiseBlock(out, root, true);
        return ServiceUtils.composeResponseOK(promise).type(MediaType.TEXT_XML).build();

    }



    @POST
    @Path("/conversion")
    @Tag(name= NLG)
    @Consumes({ MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_APPLICATION_JSONLD,
                MEDIA_APPLICATION_JSON})
    @Produces({ MEDIA_TEXT_PROVENANCE_NOTATION, MEDIA_APPLICATION_JSON, MEDIA_APPLICATION_JSONLD,
                MEDIA_IMAGE_SVG_XML, MEDIA_IMAGE_PNG, MEDIA_IMAGE_JPEG, MEDIA_APPLICATION_PDF })

    public Response conversion(@Context HttpServletResponse ignoredResponse,
                               @Context Request request,
                               Document document) {

        logger.debug("conversion");
        org.openprovenance.prov.model.ProvFactory factory = InteropFramework.getDefaultFactory();
        InteropFramework interop=new InteropFramework(factory);

        List<Variant> vs = interop.getVariants();
        Variant v = request.selectVariant(vs);

        if (v == null) {
            return utils.composeResponseNotAcceptable(vs);
        } else {
            String mediaType = v.getMediaType().toString();
            StreamingOutput promise = (out) -> interop.writeDocument(out,document,mediaType,true);
            return ServiceUtils.composeResponseOK(promise).type(mediaType).build();
        }
    }


}






