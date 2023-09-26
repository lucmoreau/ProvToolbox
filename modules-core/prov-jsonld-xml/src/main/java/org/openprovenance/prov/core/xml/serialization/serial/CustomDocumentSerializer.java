package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.Document;

import java.io.IOException;


public class CustomDocumentSerializer extends JsonSerializer<Document> {


    public static final String TO_DELETE = "to_delete";

    @Override
    public void serialize(Document doc, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
        Namespace ns = doc.getNamespace();



        jsonGenerator.writeStartObject();

        for (String prefix: ns.getPrefixes().keySet()) {
            if ("xsd".equals(prefix)) continue;
            StaxStreamWriterUtil.writeNamespace(xmlGenerator, prefix, ns.getPrefixes().get(prefix));
        }




        xmlGenerator.writeObjectField(TO_DELETE, new WrapperDocument(doc));

        //serializerProvider.defaultSerializeValue(new WrapperDocument(doc), jsonGenerator);

        //xmlGenerator.setNextIsUnwrapped(true);
        //xmlGenerator.writeObject(new WrapperDocument(doc));




    }
}