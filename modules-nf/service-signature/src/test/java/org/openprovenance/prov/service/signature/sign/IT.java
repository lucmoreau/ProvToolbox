package org.openprovenance.prov.service.signature.sign;

import junit.framework.TestCase;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.ProvSerialiser;
import org.openprovenance.prov.service.core.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.signature.DocumentMessageBodyReader;
import org.openprovenance.prov.service.signature.MapMessageBodyReader;
import org.openprovenance.prov.vanilla.ProvFactory;

import static org.openprovenance.prov.interop.InteropFramework.*;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IT extends TestCase implements TerminalColors {
    public static final String TEMPLATES = "batch:[data.sources2],[automatic.decision3,human.decision1],[responsibility6],[inclusion3],[performance4],[relevancy2],[exclusion1]";
    public static final String PROFILE_LN_BORROWER_NOUN = "ln:borrower-noun";
    static Logger logger = LogManager.getLogger(IT.class);
    final private VanillaDocumentMessageBodyWriter bodyWriter;


    public IT(String name) {
        super(name);
        this.bodyWriter = new VanillaDocumentMessageBodyWriter(new ProvSerialiser(new ProvFactory()));

    }


    public Client getClient() {
        Client client= ClientBuilder.newBuilder().build();
        client.register(bodyWriter);
        client.register(DocumentMessageBodyReader.class);
        client.register(MapMessageBodyReader.class);
        return client;
    }


    static String port= Configuration.getPropertiesFromClasspath(IT.class,"config.properties").getProperty("service.port");
    
    String postURL ="http://localhost:" + port + "/provapi/documents/";
    

    public static HashMap<String, String> table=new HashMap<String, String>();
     

    public void testSignature() throws IOException {
        testSignature("src/test/resources/test-misc/primer.provn", "gKjWKISumUaGeBOx+D1vHUpzRmWGzofaamyMnu4Yuc95KGVu06knnu9FlK6Rn0lRVe8A47ajSsBkkDtIF6YBd5ZHpqkvIxn71LTdaoZ9r1O8rxnCRDFgf05qDNGEc5qPJocP8vH6GQB0KshSHuCrYGQGknGqf+1P/zP/fh19jTjYTu2Gf6txW3kSTkqTx8IKApvRGsw3a/85nl7/+0DQp80c+Q9QFnb//HbXrBfZ0uAm0QumkB8d3rc6IW7gXn8qg1LZbgH25MpQW3/W7vDDbjjjIXEUn278BeLRBI2KPAc10TqA6bTGwDy9bdTeja+em/SSrxElpU4pIj99SFbqgg==");
        testSignature("src/test/resources/test-explain/128350251.provn", "Pi/yrG8QuU9DpHwuZcBXBC2bMNA3xjqUu+czrtskrGG6S13iGLF/9561ThFZUUBNGfp5uoXExF2aDwep0vV9t+YVryP/xbB3Wg/bMNQ4ewbSwt9wjO54sNze19poqcHgrSGBHkhjONka5sNdVjKFcYnLea81p87vbVMA1AAtikadiL6/T+xw1pVSpisbUqBWatwNkW2UHlFkNCGJdg+hzw5Q2aXj8vLPhg/BKLqAdGrjFpMTCX8keI6F03F4mgyRw39Wyt/cpVsytP37TY5I+eGfSND4NTk34GeTwu6F57TKT5p5dBcVV1pyCPXiWtvHMSJZTqWCO7CF6ynyWhunhA==");
        testNF("src/test/resources/test-misc/primer.provn", "target/nf-primer.xml");
        testNF("src/test/resources/test-explain/128350251.provn", "target/nf-128350251.xml");
        testSigned("src/test/resources/test-misc/primer.provn", "target/signed-primer.xml");
        testSigned("src/test/resources/test-explain/128350251.provn", "target/signed-128350251.xml");

    }
   

   
    public void testSignature(String file, String sig) throws IOException {
        
        logger.debug("/////////////////////////////////// testAction");


        logger.debug("*** action 1");
        String location= doPostStatements_signature(postURL,file);
        assertNotNull("location", location);
        table.put("location", location);

        logger.info("*** location = " + location);
        String response=readAsString(location, MEDIA_TEXT_PLAIN);

        logger.info(response);
        assertNotNull("response", response);

        try {
            assertEquals(sig, response);
            logger.info(ANSI_GREEN + "!!!!!!!!!!!!!! SIGNATURE OK !!!!!!!!!!!!!!!!!!!!" + ANSI_RESET);
        } catch (Throwable t) {
            System.out.println(ANSI_RED + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!       non matching signature    ");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + ANSI_RESET);
        }



    }



    public void testNF(String file, String outFile) throws IOException {

        logger.debug("/////////////////////////////////// testAction");


        logger.debug("*** action 1");
        String location= doPostStatements_nf(postURL,file);
        assertNotNull("location", location);
        table.put("location", location);

        logger.info("*** location = " + location);
        String response=readAsString(location, MEDIA_APPLICATION_JSON);

        assertNotNull("response", response);

        Files.write(Paths.get(outFile),response.getBytes());


    }


    public void testSigned(String file, String outFile) throws IOException {

        logger.debug("/////////////////////////////////// testAction");


        logger.debug("*** action 1");
        String location= doPostStatements_signed(postURL,file);
        assertNotNull("location", location);
        table.put("location", location);

        logger.info("*** location = " + location);
        String response=readAsString(location, MEDIA_APPLICATION_JSON);

        assertNotNull("response", response);

        Files.write(Paths.get(outFile),response.getBytes());


    }

    public String readAsString(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();

        String o=response2.readEntity(String.class);
        client.close();
        return o;
    }


    public Map readAsJson(String location, String media) {
        Client client=getClient();
        WebTarget target=client.target(location);
        Response response2=target.request(media).get();

        Map o=response2.readEntity(Map.class);
        client.close();
        return o;
    }


    public String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    public String doPostStatements_signature(String url, String file) {
        InteropFramework intF=new InteropFramework();
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is not null", doc);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        intF.writeDocument(baos, Formats.ProvFormat.PROVN, doc);
        String s=baos.toString();

        Client client = ClientBuilder.newBuilder().build();



        WebTarget target = client.target(url);


        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("statements", s, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type",      "provn",    MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("sign",      "Signature",   MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public String doPostStatements_nf(String url, String file) {
        InteropFramework intF=new InteropFramework();
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is not null", doc);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        intF.writeDocument(baos, Formats.ProvFormat.PROVN, doc);
        String s=baos.toString();

        Client client = ClientBuilder.newBuilder().build();



        WebTarget target = client.target(url);


        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("statements", s,                 MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type",      "provn",     MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("sign",      "NF",       MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public String doPostStatements_signed(String url, String file) {
        InteropFramework intF=new InteropFramework();
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is not null", doc);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        intF.writeDocument(baos, Formats.ProvFormat.PROVN, doc);
        String s=baos.toString();

        Client client = ClientBuilder.newBuilder().build();



        WebTarget target = client.target(url);


        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("statements", s,                 MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type",      "provn",     MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("sign",      "Sign",      MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public String doPostStatements_signature(String url, String template, String file) {
        InteropFramework intF=new InteropFramework();
        
        String str;
        try {
            str=readFile(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str=null;
        } 
        assertNotNull(" string (" + file + ") is not null", str);

        Client client = getClient();

        WebTarget target = client.target(url);

        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("url",           template,        MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("statements",    str,             MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type",      "provn",      MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("expand",    "provn",      MediaType.TEXT_PLAIN_TYPE);

        Response response=target.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        System.out.println("*** write " + location);
        client.close();


        return location;  

    }



    public Document readDocument(String location) {

        Client client=getClient();
        WebTarget target=client.target(location);
        Response response2=target.request().get();
        Document doc=response2.readEntity(org.openprovenance.prov.vanilla.Document.class);
        return doc;
    }


    public Response getResource(String location, String media) {
        Client client=getClient();
        WebTarget target=client.target(location);
        Response response2;
        if (media==null) {
            response2=target.request().get(); 
        } else {
            response2=target.request(media).get(); 
        }
        client.close();
        return response2;
    }


}
