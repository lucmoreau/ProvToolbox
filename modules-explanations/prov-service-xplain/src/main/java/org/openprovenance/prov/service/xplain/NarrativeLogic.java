package org.openprovenance.prov.service.xplain;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.scala.iface.Explainer;
import org.openprovenance.prov.scala.iface.XFactory;
import org.openprovenance.prov.scala.immutable.Document;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.scala.narrator.XConfig;
import org.openprovenance.prov.scala.xplain.Narrative;
import org.openprovenance.prov.scala.narrator.NarratorFunctionality;
import org.openprovenance.prov.scala.summary.TypePropagator;
import org.openprovenance.prov.scala.wrapper.IO;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.memory.LRUHashMap;
import org.openprovenance.prov.service.xplain.actions.ActionExplain;
import org.openprovenance.prov.service.xplain.actions.ActionLinear;
import org.openprovenance.prov.service.xplain.actions.ActionRandom;
import org.openprovenance.prov.service.summary.actions.ActionUpload;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import java.io.IOException;
import java.util.*;

public class NarrativeLogic {
    public static final String PLEAD_CS_TEMPLATE_LIBRARY_JSON = "/nlg/templates/plead.cs/plead-template-library.json";
    final NarrativeServiceUtils utils;
    final LRUHashMap<String, Document> scalaDocumentCache;
    int format_option = 1;
    public String the_template_library = PLEAD_CS_TEMPLATE_LIBRARY_JSON;
    static final Logger logger = LogManager.getLogger(NarrativeLogic.class);
    ProvFactory pFactoryS = ProvFactory.pf();
    final List<Variant> narrativeVariants;
    ObjectMapper mapper = TypePropagator.om();

    public NarrativeLogic(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        this.utils = new NarrativeServiceUtils(postService, postService.getServiceUtils().getConfig());
        postService.addToPerformers(
                PostService.addToList(new ActionRandom(utils),
                        PostService.addToList(new ActionLinear(utils),
                                PostService.addToList(new ActionExplain(utils),
                                        PostService.addToList(new ActionUpload(utils), performers)))));
        postService.addOtherPerformer(Optional.of(otherPerformer.orElse(new EmptyOtherActionPerformer())));
        scalaDocumentCache = new LRUHashMap<>(postService.getServiceUtils().getConfig().documentCacheSize);
        this.narrativeVariants = Arrays.asList(new Variant(MediaType.APPLICATION_JSON_TYPE, (Locale) null, null),
                new Variant(MediaType.TEXT_PLAIN_TYPE, (Locale) null, null));
    }

    public Response random_nar(HttpServletResponse response,
                               Request request,
                               String msg,
                               String ignoredAccept) {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {

            DocumentResource vr = index.get(msg);

            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            try {
                Variant v = request.selectVariant(narrativeVariants);
                final MediaType mediaType = v.getMediaType();
                Object textDocument = getRandomNarrative(vr, mediaType);

                //System.out.println("debug --> " + textDocument.toString());

                return ServiceUtils.composeResponseOK(textDocument).type(mediaType).build();
            } catch (Throwable e) {
                logger.throwing(e);;
                String result = "Failed to create a narrative";
                return utils.composeResponseBadRequest(result, e);

            }
        } finally {
            index.close();
        }

    }

    public Response explanations(HttpServletResponse ignoredResponse,
                                 Request ignoredRequest,
                                 String docId,
                                 String explanationId,
                                 String ignoredAccept) {

        return getExplanationForDocument(docId, explanationId, true);

    }


    public Response explanationDetails(HttpServletResponse ignoredResponse,
                                       Request ignoredRequest,
                                       String docId,
                                       String explanationId,
                                       String ignoredAccept) {


        return getExplanationForDocument(docId, explanationId, false);

    }

    final XFactory factory = new XFactory();
    final Explainer explainer=factory.makeExplainer();
    final org.openprovenance.prov.scala.iface.Narrator narrator=factory.makeNarrator();

    public Response getExplanationForDocument(String docId,
                                              String explanationId,
                                              boolean partial) {
        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(docId);

            if (dr == null) {
                return utils.composeResponseNotFoundResource(docId);
            }

            Map<String, Map<String, String>> details = utils.getDetails(index, docId, dr);

            Map<String, String> entry = details.get(explanationId);

            String templates = entry.get(NarrativeServiceUtils.KEY_TEMPLATES);
            String profiles  = entry.get(NarrativeServiceUtils.KEY_PROFILES);

            //logger.debug("found templates: " + templates);
            //logger.debug("found profiles: " + profiles);


            try {
                org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(dr.getStorageId());
                ServiceConfig config = new ServiceConfig(templates, profiles, the_template_library, format_option);
                Document doc1 = getScalaDocument(docId, doc);

                StreamingOutput promise;

                if (partial) {
                    scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = narrator.getTextOnly(explainer.explain(doc1, config));
                    promise = out -> IO.mapper().writeValue(out, result);
                } else {
                    scala.collection.immutable.Map<String, Narrative> result = explainer.explain(doc1, config);
                    promise = out -> IO.mapper().writeValue(out, result);
                }
                return ServiceUtils.composeResponseOK(promise).type(MediaType.APPLICATION_JSON).build();
            } catch (Throwable e) {
                logger.throwing(e);
                String result = "Failed to create an explanation";
                return utils.composeResponseBadRequest(result, e);
            }
        } finally {
            index.close();
        }
    }

    public Document getScalaDocument(String id, org.openprovenance.prov.model.Document doc) {
        Document cached;
        synchronized (this) {
            cached = scalaDocumentCache.get(id);
        }
        if (cached == null) {
            Document doc2 = (Document) new BeanTraversal(pFactoryS, pFactoryS).doAction(doc);
            synchronized (this) {
                scalaDocumentCache.put(id, doc2);
            }
            cached = doc2;
        }
        return cached;
    }


    public Response svg4anim(HttpServletResponse ignoredResponse,
                             Request ignoredRequest,
                             String msg,
                             String ignoredAccept) throws IOException {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource vr = index.get(msg);

            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(vr.getStorageId());

            Document sdoc = getScalaDocument(msg, doc);

            return ServiceUtils.composeResponseOK(sdoc).type(InteropMediaType.MEDIA_IMAGE_SVG_XML).build();
        } finally {
            index.close();
        }

    }


    public Response explanationDetailsConfig(HttpServletResponse response,
                                             Request request,
                                             String msg,
                                             String explanationId) {
        return explanationConfig(response, request, msg, explanationId);
    }


    public Response explanationConfig(HttpServletResponse response,
                                      Request ignoredRequest,
                                      String msg,
                                      String explanationId) {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);

            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            Map<String, Map<String, String>> details = utils.getDetails(index, msg, dr);

            Map<String, String> entry = details.get(explanationId);

            String templates = entry.get(NarrativeServiceUtils.KEY_TEMPLATES);
            String profiles = entry.get(NarrativeServiceUtils.KEY_PROFILES);

            //logger.debug("found templates: " + templates);
            //logger.debug("found profiles: " + profiles);

            TemplateAndProfile tap = new TemplateAndProfile();
            tap.profile = profiles;
            tap.template = templates;

            return ServiceUtils.composeResponseOK(tap).type(MediaType.APPLICATION_JSON).build();
        } finally {
            index.close();
        }
    }


    public Response explanationsForDoc(HttpServletResponse response,
                                       Request request,
                                       String msg,
                                       TemplateAndProfile tap,
                                       UriInfo uriInfo,
                                       String accept) {
        return explanationsForDoc1(response, request, msg, tap.template, tap.profile, true, uriInfo, accept);
    }


    public Response explanationsDetailsForDoc(HttpServletResponse response,
                                              Request request,
                                              String msg,
                                              TemplateAndProfile tap,
                                              UriInfo uriInfo,
                                              String accept) {
        return explanationsForDoc1(response, request, msg, tap.template, tap.profile, false, uriInfo, accept);
    }

    public Response explanationsForDoc1(HttpServletResponse ignoredResponse,
                                        Request ignoredRequest,
                                        String msg,
                                        String template,
                                        String profile,
                                        boolean partial,
                                        UriInfo uriInfo,
                                        String ignoredAccept) {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);


            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            Map<String, Map<String, String>> details = utils.getDetails(index, msg, dr);

            String explanationId = utils.createEntryForTemplateAndProfile(index, msg, dr, template, profile);

            Map<String, String> entry = details.get(explanationId);

            String templates = entry.get(NarrativeServiceUtils.KEY_TEMPLATES);
            String profiles = entry.get(NarrativeServiceUtils.KEY_PROFILES);

            index.put(msg, dr);

            logger.debug("found templates: " + templates);
            logger.debug("found profiles: " + profiles);
            logger.debug("explanation id: " + explanationId);


            try {
                org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(dr.getStorageId());

                ServiceConfig config = new ServiceConfig(templates, profiles, the_template_library, format_option);

                Document doc1 = getScalaDocument(msg, doc);

                String s;

                if (partial) {
                    scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = narrator.getTextOnly(explainer.explain(doc1, config));
                    s = IO.mapper().writeValueAsString(result);
                } else {
                    scala.collection.immutable.Map<String, Narrative> result = explainer.explain(doc1, config);
                    s = IO.mapper().writeValueAsString(result);
                }

                return utils.composeResponseCreated(s, explanationId, uriInfo).type(MediaType.APPLICATION_JSON).build();
            } catch (Throwable e) {
                logger.throwing(e);
                String result = "Failed to create an explanation";
                return utils.composeResponseBadRequest(result, e);

            }
        } finally {
            index.close();
        }
    }

    public Response linear_narrative(HttpServletResponse ignoredResponse,
                                     Request request,
                                     String msg,
                                     String ignoredAccept) {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource dr = index.get(msg);


            if (dr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            try {
                Variant v = request.selectVariant(narrativeVariants);
                final MediaType mediaType = v.getMediaType();
                Object textDocument = getLinearNarrative(dr, mediaType);

                System.out.println("debug --> " + textDocument.toString());

                return ServiceUtils.composeResponseOK(textDocument).type(mediaType).build();
            } catch (Throwable e) {
                logger.throwing(e);
                String result = "Failed to create a narrative";
                return utils.composeResponseBadRequest(result, e);
            }
        } finally {
            index.close();
        }

    }

    public Object getLinearNarrative(DocumentResource vr, MediaType variant) throws IOException {
        return getNarrative(vr, NarratorFunctionality.linearConfig(), variant);
    }

    public Object getRandomNarrative(DocumentResource vr, MediaType variant) throws IOException {
        return getNarrative(vr, NarratorFunctionality.randomConfig(), variant);
    }

    public Object getNarrative(DocumentResource vr, XConfig config, MediaType variant) throws IOException {
        org.openprovenance.prov.model.Document doc = utils.getDocumentFromCacheOrStore(vr.getStorageId());
        Document d2 = ProvFactory.pf().newDocument(doc);

        logger.info("variant: " + variant);
        if (MediaType.TEXT_PLAIN_TYPE.equals(variant)) {
            final scala.collection.immutable.Map<String, String> result = narrator.narrate2string(d2, config);
            return result.mkString("", "\n", "");
        } else {
            final scala.collection.immutable.Map<String, scala.collection.immutable.List<String>> result = narrator.narrate2(d2, config);
            return (StreamingOutput) (out) -> mapper.writeValue(out, result);
        }
    }
}