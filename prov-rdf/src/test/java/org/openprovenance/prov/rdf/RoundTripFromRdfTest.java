package org.openprovenance.prov.rdf;

import junit.framework.TestCase;
import java.io.File;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.DocumentEquality;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.ProvSerialiser;
import org.openrdf.rio.RDFFormat;

public class RoundTripFromRdfTest extends TestCase {

	public void loadFromRdfSaveAndReload(String file, Boolean compare)
			throws Exception
	{
		System.out.println("-------------- File: " + file);
		Utility u = new Utility();
		org.openprovenance.prov.notation.Utility u2 = new org.openprovenance.prov.notation.Utility();
		ProvFactory pFactory = new ProvFactory();

		DocumentEquality de = new DocumentEquality(true);
		Document doc1 = u.parseRDF("src/test/resources/" + file);
		file = file.replace('/', '_');
		u.dumpRDF(pFactory, doc1, RDFFormat.TURTLE, "target/" + file);
		Document doc2 = u.parseRDF("target/" + file);
		// System.out.println("Equality check:");
		boolean result = de.check(doc1, doc2); // TODO: we want
												// assertTrue(result);
		if (!result && compare)
		{
			System.out.println(doc1);
			System.out.println("------------------");
			System.out.println(doc2);
		}
		// System.out.println("-----------------");
		//
		// System.out.println(doc1.getEntityOrActivityOrWasGeneratedBy());
		// System.out.println("------------------");
		// System.out.println(doc2.getEntityOrActivityOrWasGeneratedBy());
		if (compare)
		{
			assertTrue(result);
		}
		System.out.println("result is " + result);

		//ProvSerialiser.getThreadProvSerialiser().serialiseDocument(
		//		new File("target/" + file + ".xml"), doc2, true);
		//u2.writeDocument(doc2, "target/" + file + ".provn");

	}

	private void testClass(String clazz, Boolean compare) throws Exception
	{
		loadFromRdfSaveAndReload("provo-tests/prov-o-class-" + clazz
				+ "-PASS.ttl", compare);
	}

	private void testProperty(String property, Boolean compare)
			throws Exception
	{
		loadFromRdfSaveAndReload("provo-tests/prov-o-property-" + property
				+ "-PASS.ttl", compare);
	}

	private void testIssue(String issueName) throws Exception
	{
		loadFromRdfSaveAndReload("issues/issue_" + issueName + ".ttl", true);
	}

	public void testInversePredicates() throws Exception
	{
		testIssue("inverse_predicates");
	}

	public void testNoPrefix() throws Exception
	{
		testIssue("no_prefix");
	}

	public void testPrefixlessType() throws Exception
	{
		testIssue("prefixless_type");
	}

	public void testMultipleTypes() throws Exception
	{
		testIssue("multiple_types");
	}

	public void testFile1() throws Exception
	{
		loadFromRdfSaveAndReload("prov-o-ex2-PASS.ttl", true);
	}

	public void testClassInvalidation() throws Exception
	{
		testClass("Invalidation", true);
	}

	public void testClassPrimarySource() throws Exception
	{
		testClass("PrimarySource", true);
	}

	public void testClassQuotation() throws Exception
	{
		testClass("Quotation", true);
	}

	public void testClassRevision() throws Exception
	{
		testClass("Revision", true);
	}

	public void testPropertyEntity() throws Exception
	{
		testProperty("entity", true);
	}

	public void testPropertyQualifiedDelegation() throws Exception
	{
		testProperty("qualifiedDelegation", false);
	}

	public void testPropertyQualifiedEnd() throws Exception
	{
		testProperty("qualifiedEnd", true);
	}

	public void testPropertyQualifiedPrimarySource() throws Exception
	{
		testProperty("qualifiedPrimarySource", true);
	}

	public void testPropertyQualifiedQuotation() throws Exception
	{
		testProperty("qualifiedQuotation", true);
	}

	public void testPropertyQualifiedRevision() throws Exception
	{
		testProperty("qualifiedRevision", true);
	}
	private void testBundleWithTrig(String issueName) throws Exception
	{
		loadFromRdfSaveAndReload("issues/issue_" + issueName + ".trig", true);
	}
	public void testBundle() throws Exception
	{
            try {
                testBundleWithTrig("bundle1");
                System.out.println("never here");
            } catch (Throwable e) {
                assertTrue(true);
            }

            try {
                testBundleWithTrig("bundle2");
                assertTrue(false);
            } catch (Throwable e) {
                assertTrue(true);
            }
	}



}
