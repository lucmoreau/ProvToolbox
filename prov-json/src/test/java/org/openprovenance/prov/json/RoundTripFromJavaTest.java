package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.Statement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

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

	@Override
	public void makeDocAndTest(Statement stment, String file, boolean check)
			throws JAXBException {
		Document doc = pFactory.newDocument();
		doc.getEntityOrActivityOrWasGeneratedBy().add(stment);
		updateNamespaces(doc);
		file = file + extension();

		try {
			writeJSONDocument(doc, file);
			Document doc2=readJSONDocument(file);
			compareDocuments(doc, doc2, check);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected Document readJSONDocument(String file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Document doc = gson.fromJson(new BufferedReader(new FileReader(file)), Document.class);
		return doc;
	}
	
	protected void writeJSONDocument(Document doc, String file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		gson.toJson(doc, writer);
		writer.close();
	}

}
