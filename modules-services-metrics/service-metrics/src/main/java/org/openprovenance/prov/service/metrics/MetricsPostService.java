package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.exception.ParserException;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.RulesFactory;
import org.openprovenance.prov.rules.SimpleRulesFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.rest.ForwardedUriInfo;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MetricsPostService extends PostService {

    static Logger logger = LogManager.getLogger(MetricsPostService.class);

    private  RulesFactory rulesFactory = new SimpleRulesFactory();

    public void setRulesFactory(RulesFactory rulesFactory) {
        this.rulesFactory = rulesFactory;
    }

    public MetricsPostService(ServiceUtilsConfig config) {
        super(config);
    }


    public Response submit(@Parameter(  name = "form",
                                        description = "form to be posted",
                                        required = true,
                                        schema = @Schema(implementation = UploadForm.class))
                           MultipartFormDataInput input,
                           @Context HttpHeaders headers,
                           @Context HttpServletRequest requestContext,
                           @Context UriInfo uriInfo) {

        MediaType mediaType = headers.getMediaType();

        Principal principal = requestContext.getUserPrincipal();
        logger.info("principal is " + principal);


        System.out.println(uriInfo.getAbsolutePath());
        System.out.println(uriInfo.getBaseUri());

        if (mediaType.toString().startsWith("multipart/form-data")) {

            try {


                Map<String, List<InputPart>> formData = input.getFormDataMap();
                logger.debug("submitted " + formData);

                Document doc;
                doc = docProcessFileInForm(formData);
                if (doc == null) doc = docProcessUrlInForm(formData);
                if (doc == null) doc = docProcessStatementsInForm(formData);


                if (doc == null) {
                    String result = "No provenance was found, and therefore failed to create metrics";
                    return utils.composeResponseNotFOUND(result);
                }


                Rules rules= rulesFactory.newRules();
                MetricsRecord o=(MetricsRecord)rules.computeMetrics(doc, utils.getProvFactory());
                String id= o.id;


                String location = new ForwardedUriInfo(uriInfo, new AtomicReference<>(headers)).getBaseUri() + "metrics/" + id + "." + "json";



                if (true) return utils.composeResponseSeeOther("../rating.html?m=" + location).build();


                StreamingOutput promise= out -> new ObjectMapper().writeValue(out, o);


                return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();

            } catch (UncheckedException e) {
                return utils.composeResponseBadRequest("URI problem (" + e.getCause() + ")", e);
            } catch (ParserException e) {
                return utils.composeResponseBadRequest("Parser problem", e);
            } catch (Throwable t) {
                logger.throwing(t);
                String result = "exception occurred";
                return utils.composeResponseInternalServerError(result, t);
            }
        } else {
            String result = "Media type " + mediaType + " Not Supported yet";
            return utils.composeResponseNotFOUND(result);
        }

    }



    private Document docProcessFileInForm(Map<String, List<InputPart>> formData) {
        Document doc = null;
        List<InputPart> inputParts = formData.get("file");
        if ((inputParts != null)) {
            doc = utils.docProcessFileForm(inputParts);
        }
        return doc;
    }

    private Document docProcessUrlInForm(Map<String, List<InputPart>> formData) {
        Document doc = null;
        List<InputPart> inputParts = formData.get("url");
        if (inputParts != null) {
            doc = utils.docProcessURLForm(inputParts);
        }
        return doc;
    }

    private Document docProcessStatementsInForm(Map<String, List<InputPart>> formData) {
        Document doc = null;
        List<InputPart> inputParts = formData.get("statements");
        List<InputPart> type = formData.get("type");
        if (inputParts != null) {
            doc = utils.docProcessStatementsForm(inputParts, type);
        }
        return doc;
    }



}
