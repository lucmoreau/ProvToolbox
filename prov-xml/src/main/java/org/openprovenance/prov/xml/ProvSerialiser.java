package org.openprovenance.prov.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.NamespaceGatherer;
import org.w3c.dom.Node;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.File;

/** Serialiser of PROV Graphs. */

public class ProvSerialiser implements org.openprovenance.prov.model.ProvSerialiser {
    private ObjectFactory2 of=new ObjectFactory2();
	static DocumentBuilder docBuilder;



    /** Note DocumentBuilderFactory is documented to be non thread safe. 
        TODO: code analysis, of potential concurrency issues. */
	static void initBuilder() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setNamespaceAware(true);
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			throw new RuntimeException(ex);
		}
	}

    private static ThreadLocal<ProvSerialiser> threadSerialiser =
        new ThreadLocal<ProvSerialiser> () {
        protected synchronized ProvSerialiser initialValue () {
                try {
                    return new ProvSerialiser();
                } catch (JAXBException jxb) {
		    jxb.printStackTrace();
                    throw new RuntimeException("ProvSerialiser: serialiser init failure()");
                }
            }
    };

    public static ProvSerialiser getThreadProvSerialiser() {
        return threadSerialiser.get();
    }


    
    static {
        initBuilder();
    }
    protected JAXBContext jc;

    public ProvSerialiser ()  throws JAXBException {
        jc = JAXBContext.newInstance( ProvFactory.packageList );
    }

    public ProvSerialiser (String packageList) throws JAXBException {
        jc = JAXBContext.newInstance( packageList );
    }

    public void configurePrefixes(Marshaller m, Namespace namespaces) throws PropertyException {
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                      new NamespacePrefixMapper(namespaces));
    }

    /** By default we use a document provided by the DocumentBuilder
        factory. If this functionality is required,
        PStructureSerialiser needs to be subclassed and the
        defaultEmptyDocument method overriden. */

    public org.w3c.dom.Document defaultEmptyDocument () {
        return docBuilder.newDocument();
    }


  
   
    public org.w3c.dom.Document serialiseDocument (Document request) throws JAXBException {
        return (org.w3c.dom.Document) serialiseDocument (defaultEmptyDocument(), request);
    }
    
    public Node serialiseDocument (Node addTo, Document document)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        prepareNamespaces(m, document);
        m.marshal(of.createDocument(document),addTo);
        return addTo;
    }

    public void prepareNamespaces(Marshaller m, Document document) throws PropertyException {
	final Namespace superNamespace = NamespaceGatherer.accumulateAllNamespaces(document);
	configurePrefixes(m,superNamespace);
	Namespace.withThreadNamespace(superNamespace);
    }
 

    public String serialiseDocument (StringWriter sw, Document document, boolean format)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        m.setProperty("jaxb.formatted.output",format);
        prepareNamespaces(m, document);
        m.marshal(of.createDocument(document),sw);
        return sw.toString();
    }

    public void serialiseDocument (OutputStream out, Document document, boolean format)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        m.setProperty("jaxb.formatted.output",format);
        prepareNamespaces(m, document);
        m.marshal(of.createDocument(document),out);
    }
    public void serialiseDocument (File file, Document document, boolean format)
	        throws JAXBException {
	Marshaller m=jc.createMarshaller();
	m.setProperty("jaxb.formatted.output",format);
	//m.setProperty("jaxb.encoding","UTF-16");
	prepareNamespaces(m, document);
	m.marshal(of.createDocument(document),file);
    }


}

