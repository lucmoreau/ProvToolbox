package org.openprovenance.prov.core.xml.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.Document;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;


public class CustomDocumentSerializer extends ValueSerializer<Document> {


    public static final String TO_DELETE = "to_delete";

    @Override
    public void serialize(Document doc, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
        Namespace ns = doc.getNamespace();



        jsonGenerator.writeStartObject();

        for (String prefix: ns.getPrefixes().keySet()) {
            if ("xsd".equals(prefix)) continue;
            StaxStreamWriterUtil.writeNamespace(xmlGenerator, prefix, ns.getPrefixes().get(prefix));
        }
        if (ns.getDefaultNamespace()!=null) {
           // StaxStreamWriterUtil.writeDefaultNamespace(xmlGenerator, ns.getDefaultNamespace());
        }

        /* It's difficult to work with jackson to generate xml compatible to provx.
        The easiest and quickest solution i found was to create this dummy, which will
        be removed in a post phase using the stax streaming package.
         */

        xmlGenerator.writePOJOProperty(TO_DELETE, new WrapperDocument(doc));




    }
}