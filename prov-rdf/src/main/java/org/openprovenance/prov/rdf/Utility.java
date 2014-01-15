package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.rdf.collector.QualifiedCollector;
import org.openprovenance.prov.rdf.collector.RdfCollector;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openrdf.repository.Repository;
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
	RDFParser rdfParser = Rio.createParser(Rio.getParserFormatForFileName(file.getName()));
	URL documentURL = file.toURI().toURL();
	InputStream inputStream = documentURL.openStream();
	RdfCollector rdfCollector = new QualifiedCollector(pFactory,onto);
	rdfParser.setRDFHandler(rdfCollector);
	rdfParser.parse(inputStream, documentURL.toString());
	Document doc=rdfCollector.getDocument();
	Namespace ns=doc.getNamespace();
	ns.unregister("xsd", "http://www.w3.org/2001/XMLSchema#");
	ns.register("xsd", "http://www.w3.org/2001/XMLSchema");
	return doc;
    }

    public void dumpRDF(Document document,
			RDFFormat format, String filename) throws Exception {
	
	
	Repository myRepository = new SailRepository(new MemoryStore());
	myRepository.initialize();
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
	bt.convert(document);
	

	rHelper.dumpToRDF(filename, rep, format, 
			  rdfc.getNamespace());
    }
    


}
