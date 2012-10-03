package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.trig.TriGParser;

public class ParserTest extends TestCase {
	
	ProvFactory pFactory=ProvFactory.getFactory();
	
	public void testRDF() throws IOException, RDFParseException, RDFHandlerException, JAXBException {
		URL documentURL = new File("src/test/resources/ex10.ttl").toURI().toURL();
		InputStream inputStream = documentURL.openStream();
		RDFParser rdfParser = new TriGParser();
		RdfCollector rdfCollector = new RdfCollector(pFactory);
		rdfParser.setRDFHandler(rdfCollector);
		rdfParser.parse(inputStream, documentURL.toString());
		
		Document document = rdfCollector.getDocument();
		
		ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        serial.serialiseDocument(System.out,document,true);
	}
}
