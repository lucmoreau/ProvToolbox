package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.rdf.collector.QualifiedCollector;
import org.openprovenance.prov.rdf.collector.RdfCollector;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.contextaware.ContextAwareRepository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.sail.memory.MemoryStore;

public class Utility {
    private final ProvFactory pFactory;
    private final Ontology onto;

    public Utility (ProvFactory pFactory, Ontology onto) {
	this.pFactory=pFactory;
	this.onto=onto;

    }


    public Document parseRDF(String filename) throws RDFParseException,
					     RDFHandlerException, IOException,
					     JAXBException {
    	//System.out.println("**** Parse "+filename);
	File file = new File(filename);
	URL documentURL = file.toURI().toURL();
	InputStream inputStream = documentURL.openStream();
	RDFParser rdfParser = Rio.createParser(Rio.getParserFormatForFileName(file.getName()));
	String streamName=documentURL.toString();
	
	return parseRDF(inputStream, rdfParser, streamName);
    }

    public Document parseRDF(InputStream inputStream, RDFFormat format) throws RDFParseException,
    							RDFHandlerException, IOException,
    							JAXBException {
	RDFParser rdfParser=Rio.createParser(format);
	String streamName="inputstream";
	return parseRDF(inputStream, rdfParser, streamName);
    }	

    public Document parseRDF(InputStream inputStream, RDFParser rdfParser,
			     String streamName) throws IOException,
					       RDFParseException,
					       RDFHandlerException {
	RdfCollector rdfCollector = new QualifiedCollector(pFactory,onto);
	rdfParser.setRDFHandler(rdfCollector);
	rdfParser.parse(inputStream, streamName);
	Document doc=rdfCollector.getDocument();
	Namespace ns=doc.getNamespace();
	ns.unregister("xsd", "http://www.w3.org/2001/XMLSchema#");
	ns.register("xsd", "http://www.w3.org/2001/XMLSchema");
	return doc;
    }

    public void dumpRDF(Document document,
			RDFFormat format, String filename) {
	dumpRDFInternal(document, format, filename);
    }
    
    public void dumpRDF(Document document,
			RDFFormat format, OutputStream os) {
	dumpRDFInternal(document, format, os);
	
    }
	
    private void dumpRDFInternal(Document document,
                         RDFFormat format, Object out) {
	Repository myRepository = new SailRepository(new MemoryStore());
	try {
	    myRepository.initialize();
	} catch (RepositoryException e) {
	    throw new RdfConverterException("failed to initialize repository", e);
	}
	ContextAwareRepository rep=new ContextAwareRepository(myRepository); // was it necessary to create that?

	RepositoryHelper rHelper = new RepositoryHelper();
	
	RdfConstructor rdfc = new RdfConstructor(new SesameGraphBuilder(rep, pFactory), pFactory);
			
	Namespace ns=new Namespace(document.getNamespace());	
	ns.unregister("xsd", "http://www.w3.org/2001/XMLSchema");
	ns.register("xsd", "http://www.w3.org/2001/XMLSchema#");
	ns.register("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	rdfc.setNamespace(ns);

	Namespace.withThreadNamespace(document.getNamespace());
	BeanTraversal bt = new BeanTraversal(rdfc,pFactory);
	bt.doAction(document);
	
	if (out instanceof String) {
	    rHelper.dumpToRDF((String)out, rep, format, 
	                      rdfc.getNamespace());
	} else {
	    rHelper.dumpToRDF(new OutputStreamWriter((OutputStream)out), rep, format, 
	                      rdfc.getNamespace());
	}
    }
    


}
