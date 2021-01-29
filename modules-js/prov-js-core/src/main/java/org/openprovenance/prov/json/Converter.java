package org.openprovenance.prov.json;

import com.google.gson.*;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.*;

public class Converter {
    final private ProvFactory pFactory;

    final private Gson gson;
    final Class class1;

    public Converter(ProvFactory pFactory) {
        this.pFactory = pFactory;

        class1 = pFactory.newDocument().getClass();

        gson = new GsonBuilder().registerTypeAdapter(class1,
                new ProvDocumentDeserializer(pFactory))
                .registerTypeAdapter(class1,
                        new ProvDocumentSerializer(pFactory))
                .setPrettyPrinting().create();

    }



    public JsonElement toJsonElement(Document doc) {
        return gson.serialize(doc);
    }


    @SuppressWarnings("unchecked")
    public Document readDocument(String file) throws JsonSyntaxException,
            JsonIOException,
            IOException {
        final BufferedReader buf = new BufferedReader(new FileReader(file));
        Document doc = (Document) gson.fromJson(buf,class1);
        buf.close();
        return doc;
    }

    @SuppressWarnings("unchecked")
    public Document readDocument(InputStream is) throws JsonSyntaxException,
            JsonIOException,
            IOException {
        final BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        Document doc = (Document) gson.fromJson(buf,class1);
        buf.close();
        return doc;
    }

    public void writeDocument(Document doc, String file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        gson.toJson(doc, writer);
        writer.close();
    }

    public void writeDocument(Document doc, Writer out) throws IOException {
        BufferedWriter writer = new BufferedWriter(out);
        gson.toJson(doc, writer);
        writer.close();
    }

    public void writeDocument(Document doc, OutputStream out)  {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        gson.toJson(doc, writer);
        try {
            writer.close();
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public String getString(Document doc) {
        return gson.toJson(doc);
    }

    @SuppressWarnings("unchecked")
    public Document fromString(String jsonStr) {
        return (Document) gson.fromJson(jsonStr, class1);
    }

}
