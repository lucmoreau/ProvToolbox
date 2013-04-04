package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.UncheckedTestException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Round trip testing JavaBean -> PROV-JSON -> JavaBean
 */
public class RoundTripFromJavaTest extends org.openprovenance.prov.xml.RoundTripFromJavaTest
{

    
	private Gson gson = new GsonBuilder()
							.registerTypeAdapter(Document.class, new ProvDocumentDeserializer())
							.registerTypeAdapter(Document.class, new ProvDocumentSerializer())
							.setPrettyPrinting()
							.create();
	
	public RoundTripFromJavaTest(String testName) {
		super(testName);
	}

	@Override
	public String extension() {
		return ".json";
	}
	
	   public void testDictionaryInsertion1() {};
	    public void testDictionaryInsertion2() {};
	    public void testDictionaryInsertion3() {};
	    public void testDictionaryInsertion4() {};
	    public void testDictionaryInsertion5() {};
	    public void testDictionaryInsertion6() {};
	    public void testDictionaryRemoval1() {};
	    public void testDictionaryRemoval2() {};
	    public void testDictionaryRemoval3() {};
	    public void testDictionaryRemoval4() {};
	    public void testDictionaryRemoval5() {};
	    public void testDictionaryMembership1() {};
	    public void testDictionaryMembership2() {};
	    public void testDictionaryMembership3() {};
	    public void testDictionaryMembership4() {};

	@Override
	public Document readDocument(String file) {
		try {
			Document doc = gson.fromJson(new BufferedReader(new FileReader(file)), Document.class);
			return doc;
		} catch (Exception e) {
			throw new UncheckedTestException(e);
		}
	}

	@Override
	public void writeDocument(Document doc, String file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			gson.toJson(doc, writer);
			writer.close();
		} catch (Exception e) {
			throw new UncheckedTestException(e);
		}
	}
	
}