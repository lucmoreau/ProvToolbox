package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Converter {
    final private ProvFactory pFactory;

    final private Gson  gson;
    final Class class1;
    
    public Converter (ProvFactory pFactory) {
	this.pFactory=pFactory;
	
	class1=pFactory.newDocument().getClass();
	
	gson = new GsonBuilder()
        .registerTypeAdapter(class1, new ProvDocumentDeserializer(pFactory))
        .registerTypeAdapter(class1, new ProvDocumentSerializer(pFactory))
        .setPrettyPrinting()
        .create();

    }
    
	@SuppressWarnings("unchecked")
	public Document readDocument(String file) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
	    Document doc = (Document) gson.fromJson(new BufferedReader(new FileReader(file)), class1);
	    return doc;
	}

	public void writeDocument(Document doc, String file) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    gson.toJson(doc, writer);
	    writer.close();
	}
	
	public String getString(Document doc) {
	    return gson.toJson(doc);
	}
	
	@SuppressWarnings("unchecked")
	public Document fromString(String jsonStr) {
	    return (Document) gson.fromJson(jsonStr, class1);
	}

}
