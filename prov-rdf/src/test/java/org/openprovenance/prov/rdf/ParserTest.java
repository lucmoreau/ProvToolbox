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
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 

	}

	public void testUsage7() throws JAXBException
	{
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 
	}

	public void testInvalidation7() throws JAXBException
	{
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 
	}

	public void testStart10() throws JAXBException
	{
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 
    	}

	public void testEnd10() throws JAXBException
	{
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 
	}

	public void testDerivation9() throws JAXBException
	{
	        // note, the original assertion was wasDerivedFrom(-,e1) which we could not translate into rdf at all. So, I added types to it.
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 
	}

	public void testDerivation10() throws JAXBException
	{
		// TODO: fails on comparison on a statement with id=<null> (Originally asserted) and a statement with id =blank node (retrieved from triple store). 
		// TODO: Get a wasDerivedFrom as well as a Derivation. <- I don't think it's the case
	}

	public void testInfluence1() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)   Limitation of Elmo, I think this can be fixed by asserting triples without Elmo
	}

	public void testInfluence2() throws JAXBException
	{
		// Class cast errors (ActivityOrAgentOrEntity)    Limitation of Elmo, I think this can be fixed by asserting triples without Elmo
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

	
	public void testMembership1() throws JAXBException
	{
	    
	    //Problem with RDFConstructor: I don't seem to be able to generate a resource of type prov:Collection
		// TODO: Unsupported
	}

	public void testMembership2() throws JAXBException
	{
		// TODO: Unsupported
	}

	public void testMembership3() throws JAXBException
	{
		// TODO: Unsupported
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
