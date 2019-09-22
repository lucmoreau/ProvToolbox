package org.openprovenance.prov.pservice.roundtrip;

import static org.openprovenance.prov.interop.InteropFramework.MEDIA_IMAGE_SVG_XML;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_PROVENANCE_NOTATION;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_APPLICATION_RDF_XML;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_APPLICATION_TRIG;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_TEXT_TURTLE;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_APPLICATION_PROVENANCE_XML;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_APPLICATION_JSON;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_APPLICATION_PDF;
import static org.openprovenance.prov.interop.InteropFramework.MEDIA_APPLICATION_FORM_URLENCODED;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.netlib.util.intW;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.pservice.DocumentMessageBodyReader;
import org.openprovenance.prov.service.core.DocumentMessageBodyWriter;
import org.openprovenance.prov.model.DocumentEquality;
import org.openprovenance.prov.xml.UncheckedTestException;
import org.openprovenance.prov.service.core.DocumentMessageBodyWriter;

abstract 
public class IT extends
		    org.openprovenance.prov.xml.RoundTripFromJavaTest {
    
    private DocumentEquality documentEquality;
    public IT(String name) {
	super(name);
	this.documentEquality = new DocumentEquality(mergeDuplicateProperties(),null);
    }

    public String extension() {
	return "";
    }

    
    public void testDictionaryInsertion1() {}
    public void testDictionaryInsertion2() {}
    public void testDictionaryInsertion3() {}
    public void testDictionaryInsertion4() {}
    public void testDictionaryInsertion5() {}
    public void testDictionaryInsertion6() {}
    public void testDictionaryInsertion7() {}
    public void testDictionaryRemoval1() {}
    public void testDictionaryRemoval2() {}
    public void testDictionaryRemoval3() {}
    public void testDictionaryRemoval4() {}
    public void testDictionaryRemoval5() {}
    public void testDictionaryMembership2() {}
    public void testDictionaryMembership3() {}
    public void testDictionaryMembership4() {}

    public void testMembership2() {}
    public void testMembership3() {}
    
    ProvFormat format=ProvFormat.TRIG;
    
    public boolean isRdf(ProvFormat format) {
    	switch (format) {
    	case TRIG:
    	case RDFXML:
    	case TURTLE: {
    		return true;
    	}
		default:
			return false;
    	}
    }
    
	


	class Pair {
		Document document;
		ProvFormat format;
		Pair(Document document, ProvFormat format) {
			this.document=document;
			this.format=format;
		}
	}

    @Override
   	public void compareDocAndFile(Document doc, String file, boolean check) {
        file=file+extension();
        writeDocument(doc, file);
        List<Pair> docs=readDocuments(file);
        for (Pair pair: docs) {
        	Document doc3=pair.document;
        	ProvFormat format=pair.format;
        	compareDocuments(doc, doc3, check && checkTest(file,format));
        }
    }
	public boolean checkTest(String name, ProvFormat format)
	{
		if (isRdf(format)) {
			if(name.endsWith("mention1"+extension()) || name.endsWith("mention2"+extension()))
			{
				return false;
			}
			if (name.contains("scruffy")) {
				return false;
			}
		}
	
		return true;
	}


    // FOR RDF only
	public boolean mergeDuplicateProperties()
	{
		return true;
	}


    
    public List<Pair> readDocuments(String file) {
    	String location=fileToUri.get(file);
        System.out.println("*** reading " + location);
        Document doc1 = readDocument(location,MEDIA_APPLICATION_PROVENANCE_XML);
        Document doc2 = readDocument(location,MEDIA_APPLICATION_JSON);
        Document doc3 = readDocument(location,MEDIA_TEXT_PROVENANCE_NOTATION);
        Document doc4 = readDocument(location,MEDIA_APPLICATION_TRIG);
        Object o1=readObject(location, MEDIA_IMAGE_SVG_XML);
        List<Pair> ll=new LinkedList<Pair>();
        ll.add(new Pair(doc1,ProvFormat.XML));
        ll.add(new Pair(doc2,ProvFormat.JSON));
        ll.add(new Pair(doc3,ProvFormat.PROVN));
        ll.add(new Pair(doc4,ProvFormat.TRIG));
        return ll;
    }

	public Document readDocument(String location, String media) {
		Client client=ClientBuilder.newBuilder().build();
        client.register(DocumentMessageBodyReader.class);
        WebTarget target=client.target(location);
        Response response2=target.request(media).get(); 
        Document doc=response2.readEntity(org.openprovenance.prov.model.Document.class);
        //Object doc=response2.readEntity(InputStream.class);
        //System.out.println(" *** found entity ");
        client.close();
		return doc;
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

    static Map<String,String> fileToUri=new HashMap<String,String>();
    
    @Override
    public void writeDocument(Document doc, String file) {
        System.out.println("*** doing " + file);

        Client client = ClientBuilder.newBuilder().build();
        WebTarget target = client.target("http://localhost:8080/ptm/bindings/");
        client.register(DocumentMessageBodyWriter.class);
	//Namespace.withThreadNamespace(doc.getNamespace());
        //Response response=target.request(MEDIA_APPLICATION_PROVENANCE_XML).post(Entity.entity(doc, MEDIA_APPLICATION_PROVENANCE_XML));
	Response response=target.request(MEDIA_TEXT_PROVENANCE_NOTATION).post(Entity.entity(doc, MEDIA_TEXT_PROVENANCE_NOTATION));
	//Response response=target.request(MEDIA_TEXT_TURTLE).post(Entity.entity(doc, MEDIA_TEXT_TURTLE));
	//Response response=target.request(MEDIA_APPLICATION_JSON).post(Entity.entity(doc, MEDIA_APPLICATION_JSON));
        String location=response.getHeaderString("Location");
        System.out.println("*** write " + location);
        client.close();
        fileToUri.put(file,location);
        
        
        
    }

    @Override
    public boolean checkSchema(String name) {
	return false;
    }


    public void compareDocuments(Document doc, Document doc2, boolean check) {
	assertTrue("self doc equality", doc.equals(doc));
	assertTrue("self doc2 equality", doc2.equals(doc2));
	if (check) {
	    boolean result=this.documentEquality.check(doc, doc2);
	    if (!result) {
	    System.out.println("Pre-write graph: "+doc);
		System.out.println("Read graph: "+doc2);
	    }
	    assertTrue("doc equals doc2", result);
	} else {
	    //assertFalse("doc distinct from doc2", doc.equals(doc2));
	}
    }
    

}
