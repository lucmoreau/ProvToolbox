package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.rdf.collector.QualifiedCollector;
import org.openprovenance.prov.rdf.collector.RdfCollector;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.ValueConverter;
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


    public Document parseRDF(String filename) throws RDFParseException,
					     RDFHandlerException, IOException,
					     JAXBException {
    	System.out.println("**** Parse "+filename);
	ProvFactory pFactory = new ProvFactory();
	File file = new File(filename);
	RDFParser rdfParser = Rio.createParser(Rio.getParserFormatForFileName(file.getName()));
	URL documentURL = file.toURI().toURL();
	InputStream inputStream = documentURL.openStream();
	RdfCollector rdfCollector = new QualifiedCollector(pFactory);
	rdfParser.setRDFHandler(rdfCollector);
	rdfParser.parse(inputStream, documentURL.toString());
	return rdfCollector.getDocument();
    }

    public void dumpRDF(org.openprovenance.prov.model.ProvFactory pFactory, Document document,
			RDFFormat format, String filename) throws Exception {
	
	
	Repository myRepository = new SailRepository(new MemoryStore());
	myRepository.initialize();
	ContextAwareRepository rep=new ContextAwareRepository(myRepository); // was it necessary to create that?

	RepositoryHelper rHelper = new RepositoryHelper();

	
	RdfConstructor rdfc = new RdfConstructor(new SesameGraphBuilder(rep));
	rdfc.getNamespaceTable().putAll(document.getNss());
	rdfc.getNamespaceTable().put("xsd", "http://www.w3.org/2001/XMLSchema#");
	rdfc.getNamespaceTable().put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	

	BeanTraversal bt = new BeanTraversal(rdfc,pFactory, new ValueConverter(pFactory));
	bt.convert(document);
	

	rHelper.dumpToRDF(filename, rep, format, 
			  rdfc.getNamespaceTable());
    }
    


}
