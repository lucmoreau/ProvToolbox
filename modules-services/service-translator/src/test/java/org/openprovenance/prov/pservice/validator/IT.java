package org.openprovenance.prov.pservice.validator;

import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_HTML;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_XML;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.pservice.DocumentMessageBodyReader;
import org.openprovenance.prov.pservice.ValidationReportMessageBodyReader;
import org.openprovenance.prov.xml.validation.ValidationReport;
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
    
    String validationURL="http://localhost:" + port + "/provapi/documents/";
    

    public static HashMap<String, String> table=new HashMap<String, String>();
     

    public void testAction1(){
    	testAction("src/test/resources/doc-tovalidate.provn");
    }
   

    public void testAction2(){
    	testAction("src/test/resources/picaso-file.provn");
    }

    public void testAction3(){
    	testAction("src/test/resources/picaso-file2.provn");
    }
   
    public void testAction(String file){

    	
    	logger.debug("*** action 1");
        String location=doPostStatements(validationURL,file);
        assertNotNull("location", location);
        table.put("location", location);    
        
	Response resp=getResource(location, MEDIA_TEXT_HTML);
	logger.debug("response contents " + resp.getEntity());
	logger.debug("response headers " + resp.getHeaders());
	logger.debug("response links " + resp.getLinks());
	
	String location_html=(String)resp.getHeaders().getFirst("Location");
	logger.debug("location_html " + location_html);
	assertNotNull("location_html", location_html);

	Response resp_html=getResource(location_html, MEDIA_TEXT_HTML);
	assertEquals("Response HTML status is OK",Response.Status.OK.getStatusCode(),resp_html.getStatus());

	Response resp2=getResource(location, MEDIA_TEXT_XML);
	logger.debug("response contents " + resp2.getEntity());
	logger.debug("response headers " + resp2.getHeaders());
	logger.debug("response links " + resp2.getLinks());
	
	String location_xml=(String)resp2.getHeaders().getFirst("Location");
	logger.debug("location_xml " + location_xml);
	assertNotNull("location_xml", location_xml);
	
	

	Response resp_xml=getResource(location_xml, MEDIA_TEXT_XML);
	assertEquals("Response XML status is OK",Response.Status.OK.getStatusCode(),resp_xml.getStatus());
	logger.debug("response is " + resp_xml.getEntity());
	


	ValidationReport o=readValidationReport(location_xml, MEDIA_TEXT_XML);
	//logger.debug("response is " + o);
	assertNotNull("validation report", o);
	
        assertTrue("cycles ", o.getCycle().isEmpty());
        assertTrue("non strict cycles ", o.getNonStrictCycle().isEmpty());

        assertTrue("failed merges  ", o.getFailedMerge().isEmpty());
        assertNull("malformed statements  ", o.getMalformedStatements());
        assertNull("specialization report  ", o.getSpecializationReport());
        assertTrue("qualified name mismatch  ", o.getQualifiedNameMismatch().isEmpty());

        
    }
  
   
    
    
    
    public String doPostStatements(String url, String file) {
	InteropFramework intF=new InteropFramework();
	Document doc;
	doc = intF.readDocumentFromFile(file);
	assertNotNull(" document (" + file + ") is not null", doc);

	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	intF.writeDocument(baos, ProvFormat.PROVN, doc);
	String s=baos.toString();
	
        Client client = ClientBuilder.newBuilder().build();
        
        
        
        WebTarget target = client.target(url);
        
        
        
        MultipartFormDataOutput output=new MultipartFormDataOutput();
        output.addFormData("statements", s, MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("type", "provn", MediaType.TEXT_PLAIN_TYPE);
        output.addFormData("validate", "Validate", MediaType.TEXT_PLAIN_TYPE);
        
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
    
    public ValidationReport readValidationReport(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        client.register(ValidationReportMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get(); 
        System.out.println(" *** found response2 ");

        ValidationReport o=response2.readEntity(ValidationReport.class);
        System.out.println(" *** found o ");
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
    /*
    public SimplifiedDocument getOriginal(String location, String media) {
        Client client=ClientBuilder.newBuilder().build();
        WebTarget target=client.target(location);
        client.register(SimplifiedDocumentMessageBodyReader.class);

        Response response2;
        if (media==null) {
            response2=target.request().get(); 
        } else {
            response2=target.request(media).get(); 
        }
        SimplifiedDocument doc= response2.readEntity(SimplifiedDocument.class);
        client.close();
        return doc;
    }*/
     

}
