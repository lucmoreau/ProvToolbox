package org.openprovenance.prov.rdf;

import java.io.StringWriter;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HasExtensibility;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openprovenance.prov.xml.UncheckedTestException;
import org.openrdf.rio.RDFFormat;

public class RoundTripFromJavaTest extends
		org.openprovenance.prov.xml.RoundTripFromJavaTest {
    
    

	public RoundTripFromJavaTest(String name)
	{
		super(name);
	}

	public String extension()
	{
		return ".trig";
	}

	   //public void testDictionaryInsertion1() {};
	    //public void testDictionaryInsertion2() {};
	    //public void testDictionaryInsertion3() {};
	    //public void testDictionaryInsertion4() {};
	    //public void testDictionaryInsertion5() {};
	    //public void testDictionaryRemoval1() {};
	    //public void testDictionaryRemoval2() {};
	    //public void testDictionaryRemoval3() {};
	    //public void testDictionaryRemoval4() {};
	    //public void testDictionaryRemoval5() {};
	    //public void testDictionaryMembership1() {};
	    //public void testDictionaryMembership2() {};
	    //public void testDictionaryMembership3() {};
	    //public void testDictionaryMembership4() {};

	    
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
		if(name.endsWith("mention1"+extension()) || name.endsWith("mention2"+extension()))
		{
			return false;
		}
	
		return true;
	}

	final Utility u = new Utility();

	private String dumpXML(org.openprovenance.prov.model.ProvFactory pFactory, Document document)
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


	public void testScruffyGeneration2()
	{
		// TODO: Unsupported
	}

	public void testScruffyInvalidation2()
	{
		// TODO: Unsupported
	}

	public void testScruffyUsage2()
	{
		// TODO: Unsupported
	}

	public void testScruffyStart2()
	{
		// TODO: Unsupported
	}

	public void testScruffyStart3()
	{
		// TODO: Unsupported
	}

	public void testScruffyStart4()
	{
		// TODO: Unsupported
	}

	public void testScruffyEnd2()
	{
		// TODO: Unsupported
	}

	public void testScruffyEnd3()
	{
		// TODO: Unsupported
	}

	public void testScruffyEnd4()
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
