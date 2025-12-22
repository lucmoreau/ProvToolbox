package org.openprovenance.prov.service.template;

import junit.framework.TestCase;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openprovenance.prov.model.interop.ApiUriFragments;
import org.openprovenance.prov.model.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.readers.VanillaDocumentMessageBodyReader;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.service.translator.TranslateIT;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


import static org.openprovenance.prov.model.interop.InteropMediaType.MEDIA_APPLICATION_JSONLD;
import static org.openprovenance.prov.model.interop.InteropMediaType.MEDIA_TEXT_PROVENANCE_NOTATION;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstantiaterIT extends TestCase implements ApiUriFragments {
    static Logger logger = LogManager.getLogger(InstantiaterIT.class);
    final static ClientConfig config=new ClientConfig(TranslateIT.class);
    final org.openprovenance.prov.model.ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
    final InteropFramework intF=new InteropFramework(pf);
    final private VanillaDocumentMessageBodyWriter bodyWriter;


    public InstantiaterIT() {
        this.bodyWriter = new VanillaDocumentMessageBodyWriter(intF);
    }


    public Client getClient() {
        Client client= ClientBuilder.newBuilder().build();
        client.register(bodyWriter);
        client.register(new VanillaDocumentMessageBodyReader(pf));
        return client;
    }



    public static Map<String, String> table= new HashMap<>();
     

    public void testTemplateAction1() throws IOException {
    	doTestAction("src/test/resources/test-templates/bindings1.json", "src/test/resources/test-templates/expansion-bindings1.provn");
    }
   

   
    public void doTestAction(String file, String expectedFile) throws IOException {
        
        logger.debug("/////////////////////////////////// testAction");

        String theTemplate= config.resourcesURLprefix + "templates/org/openprovenance/generic/binaryop/1.provn";


        logger.debug("*** action 1");

        Response responseTemplate=getResource(theTemplate, MEDIA_TEXT_PROVENANCE_NOTATION);
        assertEquals("Response template status is NOT OK", Response.Status.OK.getStatusCode(), responseTemplate.getStatus());

        String location=doPostStatements(config.formURL, theTemplate,file);
        assertNotNull("location", location);
        table.put("location", location);

        Response response=getResource(location, MEDIA_APPLICATION_JSONLD);
        logger.debug("++ response contents " + response.getEntity());
        logger.debug("++ response headers " +  response.getHeaders());
        logger.debug("++ response links " +    response.getLinks());

        String location_html=(String)response.getHeaders().getFirst("Location");
        logger.debug("location " + location_html);
        assertNull("location", location_html);


        Document doc=readDocument(location);

        Document doc2=new InteropFramework().readDocument(new FileInputStream(expectedFile), Formats.ProvFormat.PROVN);

        logger.debug("+++ ready to test");
        final Bundle bundle1 = (Bundle)doc.getStatementOrBundle().get(0);
        final Bundle bundle2 = (Bundle)doc2.getStatementOrBundle().get(0);

        bundle2.setId(bundle1.getId());

        assertEquals(bundle1, bundle2);


        final String templateLocation = location.replace(".provn", "/template.provn");
        logger.debug(templateLocation);
        Document doc3=readDocument(templateLocation);
        assertNotNull(doc3);

        final String bindingsLocation = location.replace(".provn", "/bindings");
        logger.debug(bindingsLocation);
        Response response4=getResource(bindingsLocation, null);
        assertEquals("Response status for Bindings request is OK",Response.Status.OK.getStatusCode(),response4.getStatus());

       
    }


    
    public String readFile(String path, Charset encoding) throws IOException 
          {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
          }

    public String doPostStatements(String url, String template, String file) {

        String str;
        try {
            str=readFile(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.throwing(Level.WARN,e); //e.printStackTrace();
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
        return response2.readEntity(org.openprovenance.prov.vanilla.Document.class);
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
