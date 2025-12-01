package org.openprovenance.prov.service.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.*;

import javax.xml.namespace.QName;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.http.HttpServletResponse;


import org.openprovenance.prov.model.interop.InteropMediaType;
import org.openprovenance.prov.model.exception.ParserException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import scala.Tuple4;
import scala.collection.mutable.Buffer;

import org.apache.coheigea.santuario.xml.signature.TestSecurityEventListener;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.xml.security.stax.impl.securityToken.X509SecurityToken;
import org.apache.xml.security.stax.securityEvent.AlgorithmSuiteSecurityEvent;
import org.apache.xml.security.stax.securityEvent.SignedElementSecurityEvent;

import org.openprovenance.prov.scala.nf.DocumentProxyFromStatements;
import org.openprovenance.prov.scala.nf.Normalizer;
import org.openprovenance.prov.scala.nf.xml.XmlNfBean;
import org.openprovenance.prov.scala.immutable.Document;
import org.openprovenance.prov.scala.nf.xml.XmlSignature;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.openprovenance.prov.model.interop.ApiUriFragments.FRAGMENT_DOCUMENTS;


@Path("")
public class SignatureService implements Constants, InteropMediaType {
    static Logger logger = LogManager.getLogger(SignatureService.class);

    final private ServiceUtils utils;
    private static final String KEYSTORE_PATH = "keystore_path";
    private static final String KEYSTORE_PASS = "keystore_pass";
    private static final String KEYSTORE_KEY = "keystore_key";
    private static final String KEYSTORE_KEYPASS = "keystore_keypass";

    public SignatureService(PostService postService) {
        this(postService, new LinkedList<ActionPerformer>(), Optional.<OtherActionPerformer>empty());
    }

    public SignatureService(PostService postService, List<ActionPerformer> performers, Optional<OtherActionPerformer> otherPerformer) {
        this.utils=postService.getServiceUtils();
        postService.addToPerformers(PostService.addToList(new ActionNormalForm(utils),
                PostService.addToList(new ActionSignature(utils),
                        PostService.addToList(new ActionCheck(utils),
                                PostService.addToList(new ActionSign(utils), performers)))));
        postService.addOtherPerformer(Optional.of((otherPerformer.orElse(new EmptyOtherActionPerformer()))));
    }



    
    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/nf")
    @Tag(name="sig")
    @Operation(summary = "Normal form Document", description = "",
	           responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    public Response nf(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "application/json,text/xml,application/xml",
                              required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) {


        System.out.println("Requesting normal form for document " + msg);

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource vr = index.get(msg);

            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }
            try {
                DocumentProxyFromStatements nfDoc = getNormalFormDocumentProxy(vr);

                return ServiceUtils.composeResponseOK(nfDoc).type("text/xml").build();
            } catch (ParserException e) {
                logger.throwing(e);
                String result = "Not parsable input";
                return utils.composeResponseBadRequest(result, e);
            } catch (Throwable e) {
                logger.throwing(e);
                String result = "Failed to create a normal form";
                return utils.composeResponseInternalServerError(result, e);
            }
        } finally {
            index.close();
        }
        


    }   
    
    
    static String  keystorePathProperty=System.getProperty(KEYSTORE_PATH);
    static String  keystorePassProperty=System.getProperty(KEYSTORE_PASS);
    static String  keystoreKeyProperty=System.getProperty(KEYSTORE_KEY);
    static String  keystoreKeyPassProperty=System.getProperty(KEYSTORE_KEYPASS);

    
    String store=(keystorePathProperty==null) ? "src/main/resources/clientstore.jks" : keystorePathProperty;
    String storepass=(keystorePassProperty==null) ? "cspass" : keystorePassProperty;
    String key=(keystoreKeyProperty==null) ? "myclientkey" : keystoreKeyProperty;
    String keypass=(keystoreKeyPassProperty==null)? "ckpass" : keystoreKeyPassProperty;
 
    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/signed")
    @Operation(summary = "Signed Document", description = "",
	       responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    @Tag(name="sig")
    public Response sign(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "application/json,text/xml,application/xml",
                              required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) throws IOException {


        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource vr = index.get(msg);


            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            DocumentProxyFromStatements nfDoc = getNormalFormDocumentProxy(vr);

            try {
                KeyAndCert kac = getKeyAndCertificate();
                ByteArrayOutputStream baos = signatureForNormalFormDocumentProxy(nfDoc, kac);
                return utils.composeResponseOK(baos).type("text/xml").build();
            } catch (ParserException e) {
                e.printStackTrace();
                String result = "Not parsable input";
                return utils.composeResponseBadRequest(result, e);
            } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | IOException e) {
                e.printStackTrace();
                String result = "problem with signature";
                return utils.composeResponseNotFOUND(result, e);
            }
        } finally {
            index.close();
        }
    }
    
    public static class KeyAndCert {
        public KeyAndCert(Key cryptoKey, X509Certificate cert) {
            this.cryptoKey=cryptoKey;
            this.cert=cert;
        }
        public Key cryptoKey;
        public X509Certificate cert;
    }

    public ByteArrayOutputStream signatureForNormalFormDocumentProxy(DocumentProxyFromStatements nfDoc, KeyAndCert kac)
            throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        PipedInputStream pipeIn=XmlNfBean.serializeToPipe(nfDoc,"123");
        XmlSignature.doSign( XmlSignature.toStreamReader(pipeIn), baos, kac.cryptoKey, kac.cert);
        return baos;
    }

    public KeyAndCert getKeyAndCertificate() throws KeyStoreException, IOException,
                                             NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore keyStore;

        keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(store), storepass.toCharArray());
        Key cryptoKey = keyStore.getKey(key, keypass.toCharArray());
        X509Certificate cert = (X509Certificate)keyStore.getCertificate(key);
        KeyAndCert kac=new KeyAndCert(cryptoKey,cert);
        return kac;
    }

    public DocumentProxyFromStatements getNormalFormDocumentProxy(DocumentResource vr) throws IOException {
        org.openprovenance.prov.model.Document doc=utils.getDocumentFromCacheOrStore(vr.getStorageId());
        org.openprovenance.prov.model.Document d2=org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(doc);
        Document d3=(Document)d2;
        DocumentProxyFromStatements nfDoc=Normalizer.fusion(d3);
        return nfDoc;
    }   

    
    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/signature")
    @Operation(summary = "Signature object", description = "",
           // , responseClass = "org.openprovenance.prov.xml.Document"
           responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    @Tag(name="sig")
    public Response signature(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "application/json,text/xml,application/xml",
                              required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) throws IOException {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();

        try {
            DocumentResource vr = index.get(msg);


            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }


            DocumentProxyFromStatements nfDoc = getNormalFormDocumentProxy(vr);


            try {
                KeyAndCert kac = getKeyAndCertificate();
                ByteArrayOutputStream baos = signatureForNormalFormDocumentProxy(nfDoc, kac);
                byte[] anArray = baos.toByteArray();
                scala.collection.immutable.List<QName> namesToSign = new scala.collection.immutable.$colon$colon<QName>(new QName("document"), scala.collection.immutable.List$.MODULE$.<QName>empty());
                TestSecurityEventListener listener = XmlSignature.getEventListenerUsingStAX(new ByteArrayInputStream(anArray), namesToSign, kac.cert);
                Tuple4<String, X509SecurityToken, Buffer<AlgorithmSuiteSecurityEvent>, Buffer<SignedElementSecurityEvent>> tuple = XmlSignature.extractSignature(listener, namesToSign);

                System.err.println(tuple._1());
                System.err.println(tuple._2());
                System.err.println(tuple._3());
                System.err.println(tuple._4());

                return utils.composeResponseOK(tuple._1()).type("text/plain").build();


            } catch (ParserException e) {
                e.printStackTrace();
                String result = "Not parsable input";
                return utils.composeResponseBadRequest(result, e);
            } catch (Throwable e) {
                e.printStackTrace();
                String result = "problem with signature";
                return utils.composeResponseNotFOUND(result, e);
            }
        } finally {
            index.close();
        }

    } 
    
    
    
    @GET
    @Path(FRAGMENT_DOCUMENTS + "{docId}/check")
    @Operation(summary = "Signed Document", description = "",
               responses = { @ApiResponse(responseCode = "404", description = DOCUMENT_NOT_FOUND) })
    @Tag(name="sig")
    public Response check(@Context HttpServletResponse response,
                              @Context Request request,
                              @PathParam("docId") String msg,
                              @Parameter(name = HEADER_PARAM_ACCEPT, description = "Accept header parameter", example = "application/json,text/xml,application/xml",
                              required = false) @HeaderParam(HEADER_PARAM_ACCEPT) String accept) throws IOException {

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();
        try {
            DocumentResource vr = index.get(msg);


            if (vr == null) {
                return utils.composeResponseNotFoundResource(msg);
            }

            if (!(vr instanceof SignedDocumentResource)) {
                return utils.composeResponseNotFoundType(msg);
            }

            SignedDocumentResource vr2 = (SignedDocumentResource) vr;

            DocumentProxyFromStatements nfDoc = getNormalFormDocumentProxy(vr);

            System.out.println("debug 1 --> " + nfDoc.toString());

            System.out.println("debug 2--> " + vr2.getSignedfilepath());

            SignatureCheck check = new SignatureCheck();
            check.samesig = "false";

            try {
                KeyAndCert kac = getKeyAndCertificate();
                ByteArrayOutputStream baos = signatureForNormalFormDocumentProxy(nfDoc, kac);
                byte[] anArray1 = baos.toByteArray();

                System.out.println("debug 3 --> " + new String(anArray1));

                scala.collection.immutable.List<QName> namesToSign = new scala.collection.immutable.$colon$colon<QName>(new QName("document"), scala.collection.immutable.List$.MODULE$.<QName>empty());
                TestSecurityEventListener listener1 = XmlSignature.getEventListenerUsingStAX(new ByteArrayInputStream(anArray1), namesToSign, kac.cert);
                Tuple4<String, X509SecurityToken, Buffer<AlgorithmSuiteSecurityEvent>, Buffer<SignedElementSecurityEvent>> tuple1 = XmlSignature.extractSignature(listener1, namesToSign);
                check.sig1 = tuple1._1();

                System.out.println("debug 4");

                TestSecurityEventListener listener2 = XmlSignature.getEventListenerUsingStAX(new FileInputStream(vr2.getSignedfilepath()), namesToSign, kac.cert);
                System.out.println("debug 5");
                Tuple4<String, X509SecurityToken, Buffer<AlgorithmSuiteSecurityEvent>, Buffer<SignedElementSecurityEvent>> tuple2 = XmlSignature.extractSignature(listener2, namesToSign);
                check.sig2 = tuple2._1();


                check.samesig = Boolean.toString(check.sig1.equals(check.sig2));

                System.out.println("debug 6 --> " + check);

                return utils.composeResponseOK(check).type("application/json").build();


            } catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | IOException e) {
                e.printStackTrace();
                check.status = "problem with signature";
                return utils.composeResponseOK(check).type("application/json").build(); // is it correct to return OK?
            }
        } finally {
            index.close();
        }

    }


    
}
