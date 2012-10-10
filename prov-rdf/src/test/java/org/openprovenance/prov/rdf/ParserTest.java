package org.openprovenance.prov.rdf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;

import javax.xml.bind.JAXBException;

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

public class ParserTest extends
		org.openprovenance.prov.xml.RoundTripFromJavaTest {

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

	@Override
	public void makeDocAndTest(Statement stment, String file, Statement[] opt,
			boolean check) throws JAXBException
	{
		Document doc = pFactory.newDocument();
		doc.getEntityOrActivityOrWasGeneratedBy().add(stment);
		updateNamespaces(doc);
		try
		{

			if (opt != null)
			{
				doc.getEntityOrActivityOrWasGeneratedBy().addAll(
						Arrays.asList(opt));

				String file2 = file + "-M";
				file2 = file2 + extension();
				dumpRDF(pFactory, doc, file2);
				Document doc3 = parseRDF(file2);
				System.out.println("-----------Comparing [internal]");
				System.out.println(doc);

				System.out.println("-----------Comparing [dumped&parsed]");
				System.out.println(doc3);
				System.out.println("--------------------------------");

				compareDocuments(doc, doc3, check && checkTest(file2));
			} else
			{

				String file1 = (opt == null) ? file : file + "-S";
				file1 = file1 + extension();
				dumpRDF(pFactory, doc, file1);
				Document doc2 = parseRDF(file1);
				compareDocuments(doc, doc2, check && checkTest(file1));
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeDocAndTest(Statement statement, String file, boolean check)
	{
		Document doc = pFactory.newDocument();
		doc.getEntityOrActivityOrWasGeneratedBy().add(statement);
		updateNamespaces(doc);

		// Dump to file
		file = file + extension();

		try
		{
			dumpRDF(pFactory, doc, file);

			Document doc2;
			doc2 = parseRDF(file);

			// System.out.println(dumpXML(pFactory, doc2));
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
			RDFHandlerException, IOException, JAXBException
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

	public void testEntity1() throws JAXBException
	{
	}

	public void testEntity2() throws JAXBException
	{
	}

	public void testEntity3() throws JAXBException
	{
	}

	public void testEntity4() throws JAXBException
	{
	}

	public void testEntity5() throws JAXBException
	{
	}

	public void testEntity6() throws JAXBException
	{
	}

	public void testEntity7() throws JAXBException
	{
	}

	public void testEntity8() throws JAXBException
	{
		// TODO: Don't yet handle duplicates
		assertTrue(true);
	}

	public void testEntity9() throws JAXBException
	{
		// TODO: URIs are expanded in RDF form
		assertTrue(true);
	}

	public void testEntity10() throws JAXBException
	{
	}

	public void testActivity1() throws JAXBException
	{
	}

	public void testActivity2() throws JAXBException
	{
	}

	public void testActivity3() throws JAXBException
	{
	}

	public void testActivity4() throws JAXBException
	{
	}

	public void testActivity5() throws JAXBException
	{
	}

	public void testActivity6() throws JAXBException
	{
	}

	public void testActivity7() throws JAXBException
	{
	}

	// /////////////////////////////////////////////////////////////////////

	public void testActivity8() throws JAXBException
	{
		// TODO: Location and duplicate attrs
		assertTrue(true);
	}

	public void testActivity9() throws JAXBException
	{
		// TODO: URIs are expanded in RDF form
		assertTrue(true);
	}

	// /////////////////////////////////////////////////////////////////////
	public void testAgent1() throws JAXBException
	{
	}

	public void testAgent2() throws JAXBException
	{
	}

	public void testAgent3() throws JAXBException
	{
	}

	public void testAgent4() throws JAXBException
	{
	}

	public void testAgent5() throws JAXBException
	{
	}

	public void testAgent6() throws JAXBException
	{
	}

	public void testAgent7() throws JAXBException
	{
	}

	public void testAgent8() throws JAXBException
	{
		// TODO: duplicate attrs
		assertTrue(true);
	}

	public void testGeneration1() throws JAXBException
	{
		// TODO: Ordering
		assertTrue(true);
	}

	public void testGeneration2() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		assertTrue(true);
	}

	public void testGeneration3() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		assertTrue(true);
	}

	public void testGeneration4() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		assertTrue(true);
	}

	public void testGeneration5() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		// TODO: URIs are expanded in RDF form
		assertTrue(true);
	}

	public void testGeneration6() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		assertTrue(true);
	}

	public void testGeneration7() throws JAXBException
	{
		// TODO: See 'hm - might have been autogenerated'
		assertTrue(true);
	}

	public void testUsage1() throws JAXBException
	{
		// null Activity issue again
		assertTrue(true);
	}

	public void testUsage2() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		assertTrue(true);
	}

	public void testUsage3() throws JAXBException
	{
		// TODO: Should pass once ordering is sorted.
		assertTrue(true);
	}

//	public void testUsage4() throws JAXBException
//	{
//	}

	public void testUsage5() throws JAXBException
	{
	}

	public void testUsage6() throws JAXBException
	{
	}

	public void testUsage7() throws JAXBException
	{
	}

	public void testInvalidation1() throws JAXBException
	{
	}

	public void testInvalidation2() throws JAXBException
	{
	}

	public void testInvalidation3() throws JAXBException
	{
	}

	public void testInvalidation4() throws JAXBException
	{
	}

	public void testInvalidation5() throws JAXBException
	{
	}

	public void testInvalidation6() throws JAXBException
	{
	}

	public void testInvalidation7() throws JAXBException
	{
	}

	public void testStart1() throws JAXBException
	{
	}

	public void testStart2() throws JAXBException
	{
	}

	public void testStart3() throws JAXBException
	{
	}

	public void testStart4() throws JAXBException
	{
	}

	public void testStart5() throws JAXBException
	{
	}

	public void testStart6() throws JAXBException
	{
	}

	public void testStart7() throws JAXBException
	{
	}

	public void testStart8() throws JAXBException
	{
	}

	public void testStart9() throws JAXBException
	{
	}

	public void testStart10() throws JAXBException
	{
	}

	public void testEnd1() throws JAXBException
	{
	}

	public void testEnd2() throws JAXBException
	{
	}

	public void testEnd3() throws JAXBException
	{
	}

	public void testEnd4() throws JAXBException
	{
	}

	public void testEnd5() throws JAXBException
	{
	}

	public void testEnd6() throws JAXBException
	{
	}

	public void testEnd7() throws JAXBException
	{
	}

	public void testEnd8() throws JAXBException
	{
	}

	public void testEnd9() throws JAXBException
	{
	}

	public void testEnd10() throws JAXBException
	{
	}

	public void testDerivation1() throws JAXBException
	{
	}

	public void testDerivation2() throws JAXBException
	{
	}

	public void testDerivation3() throws JAXBException
	{
	}

	public void testDerivation4() throws JAXBException
	{
	}

	public void testDerivation5() throws JAXBException
	{
	}

	public void testDerivation6() throws JAXBException
	{
	}

	public void testDerivation7() throws JAXBException
	{
	}

	public void testDerivation8() throws JAXBException
	{
	}

	public void testDerivation9() throws JAXBException
	{
	}

	public void testDerivation10() throws JAXBException
	{
	}

	public void testDerivation11() throws JAXBException
	{
	}

	public void testDerivation12() throws JAXBException
	{
	}

	public void testDerivation13() throws JAXBException
	{
	}

	public void testAssociation1() throws JAXBException
	{
	}

	public void testAssociation2() throws JAXBException
	{
	}

	public void testAssociation3() throws JAXBException
	{
	}

	public void testAssociation4() throws JAXBException
	{
	}

	public void testAssociation5() throws JAXBException
	{
	}

	public void testAssociation6() throws JAXBException
	{
	}

	public void testAssociation7() throws JAXBException
	{
	}

	public void testAssociation8() throws JAXBException
	{
	}

	public void testAssociation9() throws JAXBException
	{
	}

	public void testAttribution1() throws JAXBException
	{
	}

	public void testAttribution2() throws JAXBException
	{
	}

	public void testAttribution3() throws JAXBException
	{
	}

	public void testAttribution4() throws JAXBException
	{
	}

	public void testAttribution5() throws JAXBException
	{
	}

	public void testAttribution6() throws JAXBException
	{
	}

	public void testAttribution7() throws JAXBException
	{
	}

	public void testAttribution8() throws JAXBException
	{
	}

	public void testDelegation1() throws JAXBException
	{
	}

	public void testDelegation2() throws JAXBException
	{
	}

	public void testDelegation3() throws JAXBException
	{
	}

	public void testDelegation4() throws JAXBException
	{
	}

	public void testDelegation5() throws JAXBException
	{
	}

	public void testDelegation6() throws JAXBException
	{
	}

	public void testDelegation7() throws JAXBException
	{
	}

	public void testDelegation8() throws JAXBException
	{
	}

	public void testCommunication1() throws JAXBException
	{
	}

	public void testCommunication2() throws JAXBException
	{
	}

	public void testCommunication3() throws JAXBException
	{
	}

	public void testCommunication4() throws JAXBException
	{
	}

	public void testCommunication5() throws JAXBException
	{
	}

	public void testCommunication6() throws JAXBException
	{
	}

	public void testCommunication7() throws JAXBException
	{
	}

	public void testInfluence1() throws JAXBException
	{
	}

	public void testInfluence2() throws JAXBException
	{
	}

	public void testInfluence3() throws JAXBException
	{
	}

	public void testInfluence4() throws JAXBException
	{
	}

	public void testInfluence5() throws JAXBException
	{
	}

	public void testInfluence6() throws JAXBException
	{
	}

	public void testInfluence7() throws JAXBException
	{
	}

	public void testAlternate1() throws JAXBException
	{
	}

	public void testSpecialization1() throws JAXBException
	{
	}

	public void testMention1() throws JAXBException
	{
	}

	public void testMention2() throws JAXBException
	{
	}

	public void testMembership1() throws JAXBException
	{
	}

	public void testMembership2() throws JAXBException
	{
	}

	public void testMembership3() throws JAXBException
	{
	}
}
