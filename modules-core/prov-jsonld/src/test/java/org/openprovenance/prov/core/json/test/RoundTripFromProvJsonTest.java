package org.openprovenance.prov.core.json.test;

import junit.framework.TestCase;
import org.openprovenance.prov.core.json.serialization.ProvSerialiser;
import org.openprovenance.prov.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class RoundTripFromProvJsonTest extends TestCase {
	static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();


	public Document loadFromProvJson(String file) throws IOException {
		System.out.println("-------------- File: " + file);

		org.openprovenance.prov.core.json.serialization.ProvDeserialiser deserial=new org.openprovenance.prov.core.json.serialization.ProvDeserialiser();
		return deserial.deserialiseDocument(new File(file));


	}

	public void writeToProvJson(Document doc, String file) throws IOException {

		org.openprovenance.prov.core.json.serialization.ProvSerialiser serial = new ProvSerialiser();
		Namespace.withThreadNamespace(doc.getNamespace());

		System.out.println("writing to " + file);

		serial.serialiseDocument(new FileOutputStream(file), doc, true);

	}


	public void writeToProvJsonLD(Document doc, String file) throws IOException {

		org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser serial = new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser();
		Namespace.withThreadNamespace(doc.getNamespace());

		System.out.println("writing to " + file);

		serial.serialiseDocument(new FileOutputStream(file), doc, true);

	}

	private void testIssue(String issueName) throws IOException {
		Document doc=loadFromProvJson("src/test/resources/issues/" + issueName + ".json");
		System.out.println("issue " + doc);
		writeToProvJson(doc, "target/" + issueName + ".json");
		writeToProvJsonLD(doc, "target/" + issueName + ".jsonld");
	}

	public void testIssue1() throws IOException {
		//testIssue("issue-231");
		testIssue("issue-231-simple");
	}


}
