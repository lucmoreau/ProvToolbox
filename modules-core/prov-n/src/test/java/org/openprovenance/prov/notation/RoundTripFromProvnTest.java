package org.openprovenance.prov.notation;

import java.io.File;
import java.nio.file.Files;

import junit.framework.TestCase;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.DocumentEquality;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.model.ProvDeserialiser;


public class RoundTripFromProvnTest extends TestCase {
	final Utility u = new Utility();
	static org.openprovenance.prov.model.ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

	public RoundTripFromProvnTest(String name) {
		super(name);
	}
	
	public void loadFromProvnSaveAndReload(String file, Boolean compare) throws Throwable {
		System.out.println("-------------- File: " + file);
		org.openprovenance.prov.notation.Utility u2 = new org.openprovenance.prov.notation.Utility();
		DocumentEquality de = new DocumentEquality(true,null);

		Document doc1 = u.readDocument("src/test/resources/" + file,pFactory);
		file = file.replace('/', '_');
		u.writeDocument(doc1, "target/" + file,pFactory);
		Document doc2 = u.readDocument("target/" + file,pFactory);

		boolean result = de.check(doc1, doc2);

		if (!result && compare) {
			System.out.println(doc1);
			System.out.println("------------------");
			System.out.println(doc2);
		}
		if (compare) {
			assertTrue(result);
		}
		System.out.println("result is " + result);

		new org.openprovenance.prov.notation.ProvSerialiser(pFactory)
				.serialiseDocument(Files.newOutputStream(new File("target/" + file + ".jsonld").toPath()),
						doc2, true);
		u2.writeDocument(doc2, "target/" + file + ".provn",pFactory);

	}

	public void loadFromProvnSaveToJsonldAndReload(String file, Boolean compare) throws Throwable {
		System.out.println("-------------- File: " + file);
		org.openprovenance.prov.notation.Utility u2 = new org.openprovenance.prov.notation.Utility();
		DocumentEquality de = new DocumentEquality(true,null);

		Document doc1 = u.readDocument("src/test/resources/" + file,pFactory);
		file = file.replace('/', '_');
		ProvSerialiser serial=new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser();

		System.out.println("1. xxx");

		serial.serialiseDocument(Files.newOutputStream(new File("target/ld_" + file + ".jsonld").toPath()), doc1, true);
		ProvDeserialiser deserial=new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser();

		Document doc2 = deserial.deserialiseDocument(Files.newInputStream(new File("target/ld_" + file + ".jsonld").toPath()));

		System.out.println("2. xxx");

		boolean result = de.check(doc1, doc2);

		if (!result && compare) {
			System.out.println(doc1);
			System.out.println("------------------");
			System.out.println(doc2);
		}
		if (compare) {
			assertTrue(result);
		}
		System.out.println("result is " + result);
		System.out.println("3. xxx");


		u2.writeDocument(doc2, "target/" + file + ".provn",pFactory);

	}

	private void testIssue(String issueName) throws Throwable {
		loadFromProvnSaveAndReload("issues/" + issueName + ".provn", true);
	}

	private void testCrossIssue(String issueName) throws Throwable {
		loadFromProvnSaveToJsonldAndReload("issues/" + issueName + ".provn", true);
	}

	public void testMembership() throws Throwable {
		testIssue("unification-membership-f1-FAIL-DM");
		testIssue("unification-membership-f2-FAIL-DM");
	}

	public void testBundles() throws Throwable {
		//testCrossIssue("unification-membership-f1-FAIL-DM");

		testCrossIssue("picaso-file");
	}

	public void testQualifiedName() throws Throwable {
		testIssue("issue-qualified-name");
	}
	public void testEscape() throws Throwable {
		testIssue("issue-string");
	}

}
