package org.openprovenance.prov.rdf;

import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.UncheckedTestException;
import org.openrdf.rio.RDFFormat;

public class ParserTest extends
		org.openprovenance.prov.xml.RoundTripFromJavaTest
{

	public ParserTest(String name)
	{
		super(name);
	}

	public String extension()
	{
		return ".trig";
	}

	@Override
	public Document readDocument(String file)
	{
		try
		{
			Document doc2 = u.parseRDF(file);
			return doc2;
		} catch (Exception e)
		{
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file)
	{
		try
		{
			u.dumpRDF(pFactory, doc, RDFFormat.TRIG, file);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public boolean checkTest(String name)
	{
		if (name.endsWith("-S" + extension()))
		{
			return false;
		}
		return true;
	}

	final Utility u = new Utility();

	private String dumpXML(ProvFactory pFactory, Document document)
			throws JAXBException
	{

		ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
		StringWriter sw = new StringWriter();
		serial.serialiseDocument(sw, document, true);
		return sw.toString();
	}

	public boolean mergeDuplicateProperties()
	{
		return true;
	}

	public void testGeneration7() throws JAXBException
	{
		// TODO: missing labels, types, etc
	}

	public void testUsage7() throws JAXBException
	{
		// TODO: missing labels, types, etc
	}

	public void testInvalidation7() throws JAXBException
	{
		// TODO: missing labels, types, etc
	}

	public void testStart10() throws JAXBException
	{
		// TODO: missing labels, types, etc
	}

//	public void testEnd10() throws JAXBException
//	{
//		// TODO: null exception
//	}

	public void testDerivation9() throws JAXBException
	{
		// TODO: Null
	}

	public void testDerivation10() throws JAXBException
	{
		// TODO: Get a wasDerivedFrom as well as a Derivation.
	}

	public void testInfluence1() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testInfluence2() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testInfluence3() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testInfluence4() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testInfluence5() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testInfluence6() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testInfluence7() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)
	}

	public void testMention1() throws JAXBException
	{
		// TODO: Null
	}

	public void testMembership1() throws JAXBException
	{
		// TODO: Not tested yet!
	}

	public void testMembership2() throws JAXBException
	{
		// TODO: Not tested yet!
	}

	public void testMembership3() throws JAXBException
	{
		// TODO: Not tested yet!
	}

	public void IGNOREtestExtraFilesOutsideRepository()
	{
		Document doc = readDocument("/home/lavm/Downloads/index-cloudsat_airs.aqua-v3.1-2006.09.01.001429.rdf");
		try
		{
			System.out.println(" xml " + dumpXML(pFactory, doc));
		} catch (JAXBException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
