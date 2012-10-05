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
import org.openrdf.rio.Rio;
import org.openrdf.rio.trig.TriGParser;

public class ParserTest extends TestCase {
	
	
	private Document parseRDF(String filename) throws RDFParseException, RDFHandlerException, IOException {

		ProvFactory pFactory = new ProvFactory();
		
		RDFParser rdfParser = Rio.createParser(Rio.getParserFormatForFileName(filename));
		URL documentURL = new File(filename).toURI().toURL();
		InputStream inputStream = documentURL.openStream();
		RdfCollector rdfCollector = new QualifiedCollector(pFactory);
		rdfParser.setRDFHandler(rdfCollector);
		rdfParser.parse(inputStream, documentURL.toString());
		return rdfCollector.getDocument();
	}
	
	public void testRDF() throws IOException, RDFParseException, RDFHandlerException, JAXBException {

		String[] filenames = new String[]{"activityInfluence", "agentInfluence", "association", "attribution", "communication", "delegation", "derivation", "end", "entityInfluence", "generation", "influence", "instantaneousEvent", "invalidation", "start", "usage", "qualifiedInfluence"};
		
		String filename = filenames[15];
		//for(String filename: filenames) {
			System.out.println("----------------");
			System.out.println("Parse "+filename);
		Document document = parseRDF("src/test/resources/influence/ex_"+filename+".ttl");
		
		ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        serial.serialiseDocument(System.out,document,true);
		//}
	}
}
