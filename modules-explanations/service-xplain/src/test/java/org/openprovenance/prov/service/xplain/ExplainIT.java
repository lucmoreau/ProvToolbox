package org.openprovenance.prov.service.xplain;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.interop.Formats;
import org.openprovenance.prov.service.client.ClientConfig;
import org.openprovenance.prov.service.client.MapMessageBodyReader;
import org.openprovenance.prov.service.core.readers.VanillaDocumentMessageBodyReader;
import org.openprovenance.prov.service.core.writers.VanillaDocumentMessageBodyWriter;

import static org.openprovenance.prov.interop.InteropFramework.*;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExplainIT extends TestCase {
    public static final String TEMPLATES = "batch:[data.sources2],[automatic.decision3,human.decision1],[responsibility6],[inclusion3],[performance4],[relevancy2],[exclusion1]";
    public static final String PROFILE_LN_BORROWER_NOUN = "ln:borrower-noun";
    static Logger logger = LogManager.getLogger(ExplainIT.class);
    final org.openprovenance.prov.model.ProvFactory pf=new org.openprovenance.prov.vanilla.ProvFactory();
    final InteropFramework intF=new InteropFramework(pf);
    final private VanillaDocumentMessageBodyWriter bodyWriter;

    final static ClientConfig config=new ClientConfig(ExplainIT.class);

    public ExplainIT() {
        this.bodyWriter = new VanillaDocumentMessageBodyWriter(intF);
    }


    public Client getClient() {
        Client client= ClientBuilder.newBuilder().build();
        client.register(bodyWriter);
        client.register(new VanillaDocumentMessageBodyReader(pf));
        client.register(MapMessageBodyReader.class);
        return client;
    }


    public static HashMap<String, String> table= new HashMap<>();
     

    public void testTemplateAction1() throws IOException {
    	doTestAction("src/test/resources/test-explain/128350251.provn", "src/test/resources/test-templates/expansion-bindings1.provn");
    }
   

   
    public void doTestAction(String file, String expectedFile) throws IOException {
        
        logger.debug("/////////////////////////////////// testAction");


        logger.debug("*** action 1");
        String location= doPostStatements_linear_explain(config.formURL, file);
        assertNotNull("location", location);
        table.put("location", location);

        logger.debug("*** location = " + location);
        String response=readAsString(location, MEDIA_TEXT_PLAIN);

        logger.debug(response);
        assertNotNull("response", response);

        String response2=readAsString(location, MEDIA_APPLICATION_JSON);

        logger.debug(response2);
        assertNotNull("response2", response2);



        String location2= doPostStatements_explanation(config.formURL, file, TEMPLATES, PROFILE_LN_BORROWER_NOUN);
        assertNotNull("location2", location2);
        table.put("location2", location2);

        logger.debug("*** location2 = " + location2);
        Map response3=readAsJson(location2, MEDIA_APPLICATION_JSON);

        logger.debug(response3);
        assertNotNull("response3", response3);
        Set<String> keys3=response3.keySet();
        Set<String> expected3=new HashSet();
        expected3.addAll(Arrays.asList("data.sources2", "automatic.decision3,human.decision1", "responsibility6", "inclusion3", "performance4", "relevancy2", "exclusion1"));
        assertEquals("keys", expected3, keys3);
        assertEquals(response3.get("performance4"),new LinkedList<>());
        assertEquals(response3.get("responsibility6"),new LinkedList<>());

        // http://localhost:7076/provapi/documents/r13624/explanation/0
        String trimmed=location2.replace("/explanation/0","");
        final int index = trimmed.lastIndexOf("/");
        String docId=trimmed.substring(index +1);
        String prefix=trimmed.substring(0,index +1);
        logger.info("docId: " + docId);

        String explanationDetailsUrl=prefix+docId+"/explanationdetails/";
        logger.info("url: " + explanationDetailsUrl);


        Map map=doPost_explanationDetails(explanationDetailsUrl,TEMPLATES,PROFILE_LN_BORROWER_NOUN);
        assertNotNull("amap", map);
        logger.debug(map);


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


    public String doPostStatements_linear_explain(String url, String file) {
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
        output.addFormData("narrate",   "Linear",   MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }


    public String doPostStatements_explanation(String url, String file, String template, String profile) {
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
        output.addFormData("explain",   "Explanation",   MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("xplain-template", template, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("xplain-profile", profile, MediaType.TEXT_PLAIN_TYPE);
        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        client.close();
        return location;

    }

    public Map doPost_explanationDetails(String url, String template, String profile) {


        Client client = getClient();



        WebTarget target = client.target(url);

        TemplateAndProfile tap=new TemplateAndProfile();
        tap.setProfile(profile);
        tap.setTemplate(template);


        StreamingOutput promise=out -> new ObjectMapper().writeValue(out,tap);

        Response response=target.request().post(Entity.entity(promise, MediaType.APPLICATION_JSON_TYPE));

        Map o=response.readEntity(Map.class);


        client.close();
        return o;

    }
    public String doPostStatements_linear_explain(String url, String template, String file) {
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
