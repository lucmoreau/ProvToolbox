package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.DocumentEquality;
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
	public void makeDocAndTest(Statement stment, String file, Statement[] opt, boolean check)
			throws JAXBException {
		Document d1 = pFactory.newDocument();
		d1.getEntityOrActivityOrWasGeneratedBy().add(stment);
		updateNamespaces(d1);
		file = file + extension();

		try {
			writeJSONDocument(d1, file);
			Document d2 = readJSONDocument(file);
			if (check) {
				assertTrue(DocumentEquality.check(d1, d2));
			}
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