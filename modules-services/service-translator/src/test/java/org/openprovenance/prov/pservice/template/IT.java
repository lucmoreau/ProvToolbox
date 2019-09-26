package org.openprovenance.prov.pservice.template;

import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_HTML;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_XML;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.Formats.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.pservice.DocumentMessageBodyReader;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IT extends TestCase {
    static Logger logger = Logger.getLogger(IT.class);

     
    public IT(String name) {
	super(name);
    }
    
    static String port="7070";
    
    String expansionURL="http://localhost:" + port + "/provapi/documents/";
    

    public static HashMap<String, String> table=new HashMap<String, String>();
     

    public void testAction1(){
    	testAction("src/test/resources/bindings1.json");
    }
   

   
    public void testAction(String file){
        
        logger.info("/////////////////////////////////// testAction");


        logger.info("*** action 1");
        String location=doPostStatements(expansionURL,"https://openprovenance.org/templates/org/openprovenance/generic/binaryop/1.provn",file);
        assertNotNull("location", location);
        table.put("location", location);    

        Response resp=getResource(location, null);
        logger.debug("++ response contents " + resp.getEntity());
        logger.debug("++ response headers " + resp.getHeaders());
        logger.debug("++ response links " + resp.getLinks());

        String location_html=(String)resp.getHeaders().getFirst("Location");
        logger.debug("location " + location_html);
        assertNull("location", location_html);
        logger.info("/////////////////////////////////// end testAction");

       
    }


    
    public String readFile(String path, Charset encoding) throws IOException 
          {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
          }

    public String doPostStatements(String url, String template, String file) {
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

ClassLoader classLoader = this.getClass().getClassLoader();
java.net.URL resource = classLoader.getResource("org/apache/http/message/BasicLineFormatter.class");
System.out.println(resource);
resource = classLoader.getResource("org/apache/http/conn/ssl/SSLConnectionSocketFactory.class");
System.out.println(resource);

        Client client = ClientBuilder.newBuilder().build();



        WebTarget target = client.target(url);



        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("url", template, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("statements", str, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type", "provn", MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("expand", "provn", MediaType.TEXT_PLAIN_TYPE);

        logger.debug("Form is " +output);

        Response response=target.request().post(Entity.entity(output, MediaType.MULTIPART_FORM_DATA_TYPE));

        String location=response.getHeaderString("Location");
        System.out.println("*** write " + location);
        client.close();
        return location;  

    }


    public Object readObject(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(DocumentMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get(); 
        Object o=response2.readEntity(InputStream.class);
        //System.out.println(" *** found entity ");
        client.close();
                return o;
    }
    

    public Response getResource(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
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
