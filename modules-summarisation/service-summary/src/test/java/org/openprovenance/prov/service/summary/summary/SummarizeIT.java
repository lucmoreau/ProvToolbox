package org.openprovenance.prov.service.summary.summary;

import junit.framework.TestCase;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.service.core.readers.DocumentMessageBodyReader;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;
import org.openprovenance.prov.service.core.readers.MapMessageBodyReader;
import org.openprovenance.prov.vanilla.ProvFactory;

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
public class SummarizeIT extends TestCase {
    static Logger logger = LogManager.getLogger(SummarizeIT.class);
    final static ClientConfig config=new ClientConfig(SummarizeIT.class);
    final private VanillaDocumentMessageBodyWriter bodyWriter;


    public SummarizeIT() {
        this.bodyWriter = new VanillaDocumentMessageBodyWriter(new InteropFramework(new ProvFactory()));
    }


    public Client getClient() {
        Client client= ClientBuilder.newBuilder().build();
        client.register(bodyWriter);
        client.register(DocumentMessageBodyReader.class);
        client.register(MapMessageBodyReader.class);
        return client;
    }



    public static HashMap<String, String> table= new HashMap<>();
     

    public void testTemplateAction1() throws IOException {
    	testAction("src/test/resources/test-explain/128350251.provn", "src/test/resources/test-summary/empty.json");
    }
   

   
    public void testAction(String file, String summaryConfigFile) throws IOException {
        
        logger.debug("/////////////////////////////////// testAction");


        logger.debug("*** action 1");
        String location= doPostStatements_upload(config.formURL,file);
        assertNotNull("location", location);
        table.put("location", location);

        logger.info("*** location = " + location);
        Document response=readDocument(location+ ".jsonld");

        //logger.info(response);
        assertNotNull("response", response);


        String location2= doPostStatements_summary(location+"/summary",summaryConfigFile);
        assertNotNull("location2", location2);
        table.put("location2", location2);
        logger.info("*** location2 = " + location2);

        Document summary=readDocument(location2+ ".provn");
        assertNotNull(summary);
        //logger.info(summary);

        Map config=readAsJson(location2+ "/config", MediaType.APPLICATION_JSON);
        assertNotNull(config);
        logger.info(config);

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


    public String doPostStatements_upload(String url, String file) {
        InteropFramework intF=new InteropFramework();
        Document doc;
        doc = intF.readDocumentFromFile(file);
        assertNotNull(" document (" + file + ") is not null", doc);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        intF.writeDocument(baos, doc, Formats.ProvFormat.PROVN);
        String s=baos.toString();

        Client client = ClientBuilder.newBuilder().build();



        WebTarget target = client.target(url);


        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("statements", s, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type",      "provn",    MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("upload",   "upload",   MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public String doPostStatements_summary(String url, String file) {


        String str;
        try {
            str=readFile(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str=null;
        }



        Client client = getClient();



        WebTarget target = client.target(url);

        String summaryConfig="{\"level\": \"2\", \"level0\": " + str + "}" ;
        System.out.println(summaryConfig);

        Response response=target.request().post(Entity.entity(summaryConfig, MediaType.APPLICATION_JSON));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public String doPostStatements_upload(String url, String template, String file) {
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
