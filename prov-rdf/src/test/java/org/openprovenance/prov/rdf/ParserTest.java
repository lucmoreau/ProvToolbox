package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.junit.Test;
import org.openprovenance.prov.notation.BeanTreeConstructor;
import org.openprovenance.prov.xml.BeanTraversal;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.Statement;
import org.openrdf.elmo.ElmoManager;
import org.openrdf.elmo.ElmoManagerFactory;
import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

public class ParserTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest {

	public ParserTest(String name)
	{
		super(name);
	}

	public String extension()
	{
		return ".trig";
	}

	public void makeDocAndTest(Statement statement, String file)
	{
		makeDocAndTest(statement, file, true);
	}

	public void makeDocAndTest(Statement statement, String file, boolean check)
	{
		System.out.println("----------------- Make doc: "+file);
		Document doc = pFactory.newDocument();
		doc.getEntityOrActivityOrWasGeneratedBy().add(statement);
		updateNamespaces(doc);
		System.out.println("---------------- Input Doc:");
		System.out.println(doc);

		// Dump to file
		file = file + extension();

		try
		{
			dumpRDF(pFactory, doc, file);
			
			Document doc2;
			doc2 = parseRDF(file);
			System.out.println("---------------------- Parsed Doc:");
			System.out.println(doc2);
			
			//System.out.println(dumpXML(pFactory, doc2));
			compareDocuments(doc, doc2, check);
			return;
		} catch (IOException e)
		{
			System.out.println("IO Exception");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e)
		{
			System.out.println("Throwable");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(false);

	}

	private Document parseRDF(String filename) throws RDFParseException,
			RDFHandlerException, IOException
	{
		ProvFactory pFactory = new ProvFactory();
		File file = new File(filename);
		RDFParser rdfParser = Rio.createParser(Rio
				.getParserFormatForFileName(file.getName()));
		URL documentURL = file.toURI().toURL();
		InputStream inputStream = documentURL.openStream();
		RdfCollector rdfCollector = new QualifiedCollector(pFactory);
		rdfParser.setRDFHandler(rdfCollector);
		rdfParser.parse(inputStream, documentURL.toString());
		return rdfCollector.getDocument();
	}

	private void dumpRDF(ProvFactory pFactory, Document document,
			String filename) throws Exception
	{
		RepositoryHelper rHelper = new RepositoryHelper();
		ElmoModule module = new ElmoModule();
		rHelper.registerConcepts(module);
		ElmoManagerFactory factory = new SesameManagerFactory(module);
		ElmoManager manager = factory.createElmoManager();

		RdfConstructor rdfc = new RdfConstructor(pFactory, manager);
		rdfc.getNamespaceTable().putAll(document.getNss());
		System.out.println("--------------------- XML:");
		//System.out.println(dumpXML(pFactory, document));
		BeanTraversal bt = new BeanTraversal(new BeanTreeConstructor(rdfc));
		bt.convert(document);

		rHelper.dumpToRDF(new File(filename), (SesameManager) manager,
				RDFFormat.TRIG, rdfc.getNamespaceTable());
	}

	private String dumpXML(ProvFactory pFactory, Document document)
			throws JAXBException
	{

		ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
		StringWriter sw = new StringWriter();
		serial.serialiseDocument(sw, document, true);
		return sw.toString();
	}

    public void testEntity8() throws JAXBException  {
        // TODO: Don't yet handle duplicates
    	assertTrue(false);
    }

    public void testEntity9() throws JAXBException  {
        // TODO: URIs are expanded in RDF form
    	assertTrue(false);
    }


    ///////////////////////////////////////////////////////////////////////

    public void testActivity6() throws JAXBException  {
        // TODO: Location not currently produced by activity
    	assertTrue(false);
    }

    public void testActivity7() throws JAXBException  {
        // TODO: Location not currently produced by activity
    	assertTrue(false);
    }

    public void testActivity8() throws JAXBException  {
        // TODO: Location and duplicate attrs
    	assertTrue(false);
    }
    
    public void testActivity9() throws JAXBException  {
        // TODO: URIs are expanded in RDF form
    	assertTrue(false);
    }


    ///////////////////////////////////////////////////////////////////////

    public void testAgent7() throws JAXBException  {
        // TODO: Location not currently produced by agent
    	assertTrue(false);
    }

    public void testAgent8() throws JAXBException  {
        // TODO: Location and duplicate attrs
    	assertTrue(false);
    }

    public void testGeneration1() throws JAXBException  {
    	// TODO: Broken Entity find
    	assertTrue(false);
    }

    public void testGeneration2() throws JAXBException  {
    	// TODO: Broken Entity find
    	assertTrue(false);
    }


    public void testGeneration3() throws JAXBException  {
    	// TODO: Broken Entity find
    	assertTrue(false);
    }


    public void testGeneration4() throws JAXBException  {
    	// TODO: Broken Entity find
    	assertTrue(false);
    }

}
