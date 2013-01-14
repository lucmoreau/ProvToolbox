package org.openprovenance.prov.rdf;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.DocumentEquality;
import org.openprovenance.prov.xml.ProvFactory;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;

@RunWith(value = Parameterized.class)
public class RoundTripFromRdfTest extends TestCase {

	private String file;
	private String folder;
	private static String[] folders = { "issues", "examples" };
	private static List<String> skip = Arrays
			.asList(new String[] { "prov-o-property-hadUsage-FAIL.ttl",
					"prov-o-property-hadGeneration-FAIL.ttl",
					"prov-o-property-hadMember-PASS.ttl",
					"prov-o-property-alternateOf-PASS.ttl",
					"prov-o-property-specializationOf-PASS.ttl",
					"ex_alternateOf.ttl" });

	public RoundTripFromRdfTest(String folder, String file)
	{
		this.file = file;
		this.folder = folder;
	}

	@Test
	public void testSaveAndReload() throws Exception
	{
		loadFromRdfSaveAndReload(this.folder + "/" + this.file, true);
	}

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<Object[]> data()
	{
		Collection<Object[]> data = new ArrayList<Object[]>();

		for (String folder : folders)
		{
			File current = new File("src/test/resources/" + folder);
			File[] allFiles = current.listFiles();
			for (File file : allFiles)
			{
				if (!skip.contains(file.getName()))
				{
					data.add(new Object[] { folder, file.getName() });
				}
			}
		}
		return data;
	}

	protected void loadFromRdfSaveAndReload(String file, Boolean compare)
			throws Exception
	{
		System.out.println("-------------- File: " + file);
		Utility u = new Utility();

		ProvFactory pFactory = new ProvFactory();

		DocumentEquality de = new DocumentEquality(true);
		Document doc1 = u.parseRDF("src/test/resources/" + file);
		file = file.replace('/', '_');
		RDFFormat format = Rio.getParserFormatForFileName(file,
				RDFFormat.TURTLE);
		u.dumpRDF(pFactory, doc1, format, "target/" + file);
		Document doc2 = u.parseRDF("target/" + file);

		boolean result = de.check(doc1, doc2); // TODO: we want
												// assertTrue(result);
		if (!result && compare)
		{
			System.out.println(doc1);
			System.out.println("------------------");
			System.out.println(doc2);
		}

		if (compare)
		{
			assertTrue(result);
		}
		System.out.println("result is " + result);
	}
}
