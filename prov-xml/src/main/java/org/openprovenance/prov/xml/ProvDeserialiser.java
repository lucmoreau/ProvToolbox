package org.openprovenance.prov.xml;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;


/** Deserialiser of OPM Graphs. */
public class ProvDeserialiser {


    // it is recommended by the Jaxb documentation that one JAXB
    // context is created for one application. This object is thread
    // safe (in the sun impelmenation, but not
    // marshallers/unmarshallers.

    static protected JAXBContext jc;

    public ProvDeserialiser () throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance( ProvFactory.packageList );
        // note, it is sometimes recommended to pass the current classloader
        
    }

    public ProvDeserialiser (String packageList) throws JAXBException {
        if (jc==null) 
            jc = JAXBContext.newInstance(packageList);
    }


    private static ThreadLocal<ProvDeserialiser> threadDeserialiser=
        new ThreadLocal<ProvDeserialiser> () {
        protected synchronized ProvDeserialiser initialValue () {
                try {
                    return new ProvDeserialiser();
                } catch (JAXBException jxb) {
                    throw new RuntimeException("ProvDeserialiser: deserialiser init failure()");
                }
            }
    };

    public static ProvDeserialiser getThreadProvDeserialiser() {
        return threadDeserialiser.get();
    }


    public Document deserialiseDocument (File serialised)
        throws JAXBException {
        Unmarshaller u=jc.createUnmarshaller();
        Object root= u.unmarshal(serialised);
        @SuppressWarnings("unchecked")
        Document res=(Document)((JAXBElement<Document>) root).getValue();
        return res;
    }

    public Document validateDocument (String[] schemaFiles, File serialised)throws JAXBException,SAXException, IOException {
	return validateDocument(schemaFiles,serialised,true);
    }

    public Document validateDocument (String[] schemaFiles, File serialised, boolean withCurie)
	        throws JAXBException,SAXException, IOException {
	        SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	        Source [] sources=new Source[2+schemaFiles.length];
	        int schemaCount;
	        if (withCurie) {
	            schemaCount=3;
	            sources=new Source[schemaCount+schemaFiles.length];
	            sources[0]=new StreamSource(this.getClass().getResourceAsStream("/"+"curie.xsd"));
	            sources[1]=new StreamSource(this.getClass().getResourceAsStream("/"+"xml.xsd"));
	            sources[2]=new StreamSource(this.getClass().getResourceAsStream("/"+"prov-20130307.xsd")); //TODO: here to use: prov-20130307-curie.xsd
	        } else {
	            schemaCount=3;
	            sources=new Source[schemaCount+schemaFiles.length];
	            sources[0]=new StreamSource(this.getClass().getResourceAsStream("/"+"curie.xsd"));
	            sources[1]=new StreamSource(this.getClass().getResourceAsStream("/"+"xml.xsd"));
	            sources[2]=new StreamSource(this.getClass().getResourceAsStream("/"+"prov-20130307.xsd"));
	        }


	        int i=0;
	        for (String schemaFile: schemaFiles) {
	            sources[schemaCount+i]=new StreamSource(new File(schemaFile));
	            i++;
	        }
	        Schema schema = sf.newSchema(sources);  
	        Unmarshaller u=jc.createUnmarshaller();
	        //u.setValidating(true); was jaxb1.0
	        u.setSchema(schema);
	        Object root= u.unmarshal(serialised);
	        @SuppressWarnings("unchecked")
	        Document res=(Document)((JAXBElement<Document>) root).getValue();
	        return res;
	    }
    
    
    class ResourceResolver implements LSResourceResolver {

	public LSInput resolveResource(String type, 
	                               String namespaceURI,
	                               String publicId, 
	                               String systemId, 
	                               String baseURI) {
	    
	    System.out.println("resolveResource " + type + "\n" + namespaceURI + "\n" + publicId + "\n" + systemId + "\n" + baseURI);

	     // note: in this sample, the XSD's are expected to be in the root of the classpath
	    InputStream resourceAsStream = this.getClass().getClassLoader()
	            .getResourceAsStream("/w3c/"+systemId);
	    return new Input(publicId, systemId, resourceAsStream);
	}

	 }
      class Input implements LSInput {

	 private String publicId;

	 private String systemId;

	 public String getPublicId() {
	     return publicId;
	 }

	 public void setPublicId(String publicId) {
	     this.publicId = publicId;
	 }

	 public String getBaseURI() {
	     return null;
	 }

	 public InputStream getByteStream() {
	     return null;
	 }

	 public boolean getCertifiedText() {
	     return false;
	 }

	 public Reader getCharacterStream() {
	     return null;
	 }

	 public String getEncoding() {
	     return null;
	 }

	 public String getStringData() {
	     synchronized (inputStream) {
	         try {
	             byte[] input = new byte[inputStream.available()];
	             inputStream.read(input);
	             String contents = new String(input);
	             return contents;
	         } catch (IOException e) {
	            // e.printStackTrace();
	             //System.out.println("Exception " + e);
	             return null;
	         }
	     }
	 }

	 public void setBaseURI(String baseURI) {
	 }

	 public void setByteStream(InputStream byteStream) {
	 }

	 public void setCertifiedText(boolean certifiedText) {
	 }

	 public void setCharacterStream(Reader characterStream) {
	 }

	 public void setEncoding(String encoding) {
	 }

	 public void setStringData(String stringData) {
	 }

	 public String getSystemId() {
	     return systemId;
	 }

	 public void setSystemId(String systemId) {
	     this.systemId = systemId;
	 }

	 public BufferedInputStream getInputStream() {
	     return inputStream;
	 }

	 public void setInputStream(BufferedInputStream inputStream) {
	     this.inputStream = inputStream;
	 }

	 private BufferedInputStream inputStream;

	 public Input(String publicId, String sysId, InputStream input) {
	     this.publicId = publicId;
	     this.systemId = sysId;
	     this.inputStream = new BufferedInputStream(input);
	 }
      }
    public Document validateDocumentNew(String[] schemaFiles, File serialised) throws JAXBException,
						       SAXException,
						       IOException {
	int schemaCount;
	
	schemaCount = 2;

	 //System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/XML/XMLSchema/v1.1",
           //      "org.apache.xerces.jaxp.validation.XMLSchema11Factory");
	 
		SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			  //  "http://www.w3.org/XML/XMLSchema/v1.1");

	 
	// associate the schema factory with the resource resolver, which is responsible for resolving the imported XSD's
	//sf.setResourceResolver(new ResourceResolver());

	
	Source[] sources = new Source[schemaCount + schemaFiles.length];
	
	sources = new Source[schemaCount + schemaFiles.length];
	sources[0] = new StreamSource(this.getClass().getResourceAsStream("/w3c/" + "xml.xsd"));
	sources[1] = new StreamSource(this.getClass().getResourceAsStream("/w3c/" + "prov-single.xsd"));
	
	
	int i = 0;
	for (String schemaFile : schemaFiles) {
	    sources[schemaCount + i] = new StreamSource(new File(schemaFile));
	    i++;
	}
	Schema schema = sf.newSchema(sources);
	Unmarshaller u = jc.createUnmarshaller();
	// u.setValidating(true); was jaxb1.0
	u.setSchema(schema);
	Object root = u.unmarshal(serialised);
	@SuppressWarnings("unchecked")
	Document res = (Document) ((JAXBElement<Document>) root).getValue();
	return res;
    }

    public static void main(String [] args) {
        ProvDeserialiser deserial=ProvDeserialiser.getThreadProvDeserialiser();
        if ((args==null) ||  (args.length==0)) {
            System.out.println("Usage: opmxml-validate <filename> {schemaFiles}*");
            return;
        }
        File f=new File(args[0]);
        String [] schemas=new String[args.length-1];
        for (int i=1; i< args.length; i++) {
            schemas[i-1]=args[i];
        }
        try {
            deserial.validateDocument(schemas,f);
            System.out.println(args[0] + " IS a valid OPM graph");
            return ;
        } catch (JAXBException je) {
            je.printStackTrace();
            System.out.println(args[0] + " IS NOT a valid OPM graph");
        } catch (SAXException je) {
            je.printStackTrace();
            System.out.println(args[0] + " IS NOT a valid OPM graph");
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(args[0] + " IS NOT a valid OPM graph");
        }
    }
}
