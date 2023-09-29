package org.openprovenance.prov.core.xml.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.openprovenance.prov.core.xml.serialization.stax.StaxStreamWriterUtil;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.vanilla.Bundle;

import java.io.IOException;

import static org.openprovenance.prov.core.xml.serialization.serial.CustomDocumentSerializer.TO_DELETE;


abstract public class CustomBundleSerializer extends JsonSerializer<Bundle> {



    @Override
    public void serialize(Bundle bun, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
        Namespace ns = bun.getNamespace();


        System.out.println("CustomBundleSerializer.serialize " + bun.getId());


       // jsonGenerator.writeStartObject();

        for (String prefix: ns.getPrefixes().keySet()) {
            if ("xsd".equals(prefix)) continue;
            //StaxStreamWriterUtil.writeNamespace(xmlGenerator, prefix, ns.getPrefixes().get(prefix));
        }

        /* It's difficult to work with jackson to generate xml compatible to provx.
        The easiest and quickest solution i found was to create this dummy, which will
        be removed in a post phase using the stax streaming package.
         */

        System.out.println("CustomBundleSerializer.serialize " + bun.getId());

        xmlGenerator.writeObjectField("FOO1", new WrapperBundle(bun));





    }

    @Override
    public void serializeWithType(Bundle bun, JsonGenerator jsonGenerator, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        System.out.println("CustomBundleSerializer.serializeWithType " + bun.getId());
        ToXmlGenerator xmlGenerator = (ToXmlGenerator) jsonGenerator;
        Namespace ns = bun.getNamespace();


        System.out.println("CustomBundleSerializer.serializeWithType " + bun.getId());


        xmlGenerator.writeString("statements");
       // xmlGenerator.writeStartObject();

        for (String prefix: ns.getPrefixes().keySet()) {
            if ("xsd".equals(prefix)) continue;
            //StaxStreamWriterUtil.setPrefix(xmlGenerator, prefix, ns.getPrefixes().get(prefix));
        }

        /* It's difficult to work with jackson to generate xml compatible to provx.
        The easiest and quickest solution i found was to create this dummy, which will
        be removed in a post phase using the stax streaming package.
         */

        System.out.println("CustomBundleSerializer.serializeWithType " + bun.getId());

       // xmlGenerator.writeObjectField("FOO2", new WrapperBundle(bun));
        xmlGenerator.writeObject( new WrapperBundle(bun));



    }
}