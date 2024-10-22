package org.openprovenance.prov.service.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.*;
import org.apache.coheigea.santuario.xml.signature.TestSecurityEventListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xml.security.stax.impl.securityToken.X509SecurityToken;
import org.apache.xml.security.stax.securityEvent.AlgorithmSuiteSecurityEvent;
import org.apache.xml.security.stax.securityEvent.SignedElementSecurityEvent;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.exception.ParserException;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.rules.RulesFactory;
import org.openprovenance.prov.rules.SimpleRulesFactory;
import org.openprovenance.prov.scala.nf.DocumentProxyFromStatements;
import org.openprovenance.prov.scala.nf.Normalizer;
import org.openprovenance.prov.scala.nf.xml.XmlSignature;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.rest.ForwardedUriInfo;
import org.openprovenance.prov.service.signature.SignatureService;
import scala.Tuple4;
import scala.collection.mutable.Buffer;

import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.fasterxml.jackson.core.StreamReadConstraints.DEFAULT_MAX_NUM_LEN;

public class MetricsPostService extends PostService {

    static Logger logger = LogManager.getLogger(MetricsPostService.class);
    private final SignatureService signatureService;

    private  RulesFactory rulesFactory = new SimpleRulesFactory();

    public void setRulesFactory(RulesFactory rulesFactory) {
        this.rulesFactory = rulesFactory;
    }

    public MetricsPostService(ServiceUtilsConfig config) {
        super(config);
        this.signatureService = new SignatureService(this);
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

                logger.info("location is " + location);



                return utils.composeResponseSeeOther("../rating.html?m=" + location).build();


                //StreamingOutput promise= out -> new ObjectMapper().writeValue(out, o);


                //return ServiceUtils.composeResponseOK(promise).type(MEDIA_APPLICATION_JSON).build();

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

    private static DocumentProxyFromStatements getNormalFormDocumentProxy(org.openprovenance.prov.model.Document doc) throws IOException {
        org.openprovenance.prov.model.Document d2=org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(doc);
        org.openprovenance.prov.scala.immutable.Document d3=(org.openprovenance.prov.scala.immutable.Document)d2;
        DocumentProxyFromStatements nfDoc= Normalizer.fusion(d3);
        return nfDoc;
    }

;

    static public Object getSignature(SignatureService signatureService, org.openprovenance.prov.model.Document doc) {
        try {
            DocumentProxyFromStatements nfDoc=getNormalFormDocumentProxy(doc);
            SignatureService.KeyAndCert kac = signatureService.getKeyAndCertificate();
            ByteArrayOutputStream baos = signatureService.signatureForNormalFormDocumentProxy(nfDoc, kac);
            byte[] anArray = baos.toByteArray();
            scala.collection.immutable.List<QName> namesToSign = new scala.collection.immutable.$colon$colon<QName>(new QName("document"), scala.collection.immutable.List$.MODULE$.<QName>empty());
            TestSecurityEventListener listener = XmlSignature.getEventListenerUsingStAX(new ByteArrayInputStream(anArray), namesToSign, kac.cert);
            Tuple4<String, X509SecurityToken, Buffer<AlgorithmSuiteSecurityEvent>, Buffer<SignedElementSecurityEvent>> tuple = XmlSignature.extractSignature(listener, namesToSign);

            System.err.println(tuple._1());
            //System.err.println(tuple._2());
            //System.err.println(tuple._3());
            //System.err.println(tuple._4());

            Map<String, Object> result = new HashMap<>() {{
                put("signature", tuple._1());
                //put("cert", tuple._2());
                //put("algo", tuple._3());
                //put("signed", tuple._4());
            }};
            return result;

        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException |
                 UnrecoverableKeyException e) {
            HashMap<String,String> signError = new HashMap<>();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(outputStream));
            signError.put("error", outputStream.toString());
            return signError;
        }
    }


    public SignatureService getSignatureService() {
        return signatureService;
    }
}
