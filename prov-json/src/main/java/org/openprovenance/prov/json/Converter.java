package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.openprovenance.prov.xml.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Converter {
	private Gson gson = new GsonBuilder()
	                       .registerTypeAdapter(Document.class, new ProvDocumentDeserializer())
	                       .registerTypeAdapter(Document.class, new ProvDocumentSerializer())
	                       .setPrettyPrinting()
	                       .create();
	
	public Document readDocument(String file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
	    Document doc = gson.fromJson(new BufferedReader(new FileReader(file)), Document.class);
	    return doc;
	}

	public void writeDocument(Document doc, String file) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    gson.toJson(doc, writer);
	    writer.close();
	}

}
