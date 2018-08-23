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

import org.openprovenance.prov.model.Namespace;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;


/** Deserialiser of OPM Graphs. */
public class ProvDeserialiser {
    
    static ProvUtilities utils=new ProvUtilities();


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
	        utils.updateNamespaces(res);
		return res;
    }

    /**
     * After reading a document, this method should be called to ensure that Namespaces are properly chained.
     * @param document a {@link Document} to update
     */
  /*  public void updateNamespacesxx(Document document) {
	Namespace rootNamespace = Namespace.gatherNamespaces(document);
	document.setNamespace(rootNamespace);
	for (org.openprovenance.prov.model.Bundle bu: utils.getBundle(document)) {
	    Namespace ns=bu.getNamespace();
	    if (ns!=null) {
		ns.setParent(rootNamespace);
	    } else {
		ns=new Namespace();
		ns.setParent(rootNamespace);
		bu.setNamespace(ns);
	    }
	}
    }
  */
    public Document deserialiseDocument (InputStream is)
	        throws JAXBException {
	        Unmarshaller u=jc.createUnmarshaller();
	        Object root= u.unmarshal(is);
	        @SuppressWarnings("unchecked")
	        Document res=(Document)((JAXBElement<Document>) root).getValue();
	        utils.updateNamespaces(res);
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
    public Document validateDocument(String[] schemaFiles, File serialised) throws JAXBException,
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
	u.setSchema(schema);

	Object root = u.unmarshal(serialised);
	@SuppressWarnings("unchecked")
	Document res = (Document) ((JAXBElement<Document>) root).getValue();
	return res;
    }


}
