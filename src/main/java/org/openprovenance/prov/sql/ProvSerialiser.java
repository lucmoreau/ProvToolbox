package org.openprovenance.prov.sql;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.openprovenance.prov.xml.NamespacePrefixMapper;
import org.openprovenance.prov.xml.validation.ValidationReport;
import org.w3c.dom.Node;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.File;
import java.util.Hashtable;

/** Serialiser of PROV Graphs. */

public class ProvSerialiser {
    private ObjectFactory of=new ObjectFactory();
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

    public ProvSerialiser () throws JAXBException {
        jc = JAXBContext.newInstance( ProvFactory.packageList );
        //System.out.println("JAXBContext => " + jc.getClass());
    }

    public ProvSerialiser (String packageList) throws JAXBException {
        jc = JAXBContext.newInstance( packageList );
        //System.out.println("JAXBContext => " + jc.getClass());
    }

    public void configurePrefixes(Marshaller m) throws PropertyException {
        configurePrefixes(m,new Hashtable<String,String>());
    }

    public void configurePrefixes(Marshaller m, Hashtable<String,String> namespaces) throws PropertyException {
        m.setProperty("com.sun.xml.bind.namespacePrefixMapper",
                             new NamespacePrefixMapper(namespaces));
	//System.out.println("--------------> ");
	//m.setAdapter(new org.w3._2001.xmlschema.Adapter1());
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
    
    public Node serialiseDocument (Node addTo, Document graph)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        m.marshal(of.createDocument(graph),addTo);
        return addTo;
    }
    public String serialiseDocument (StringWriter sw, Document graph)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        m.marshal(of.createDocument(graph),sw);
        return sw.toString();
    }

    public String serialiseDocument (StringWriter sw, Document graph, boolean format)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        m.setProperty("jaxb.formatted.output",format);
        configurePrefixes(m,graph.getNss());
        m.marshal(of.createDocument(graph),sw);
        return sw.toString();
    }
    

    public void serialiseDocument (OutputStream out, Document graph, boolean format)
        throws JAXBException {
        Marshaller m=jc.createMarshaller();
        m.setProperty("jaxb.formatted.output",format);
        configurePrefixes(m,graph.getNss());
        m.marshal(of.createDocument(graph),out);
    }
    public void serialiseDocument (File file, Document graph, boolean format)
	        throws JAXBException {
	Marshaller m=jc.createMarshaller();
	m.setProperty("jaxb.formatted.output",format);
	configurePrefixes(m,graph.getNss());
	m.marshal(of.createDocument(graph),file);
    }


}

