package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.vanilla.QualifiedName;

import java.io.IOException;


public class CustomMapSerializer2 extends StdSerializer<QualifiedName> {

    public static final String CONTEXT_KEY_FOR_MAP = "CONTEXT_KEY_FOR_MAP";

    protected CustomMapSerializer2() {
        super(QualifiedName.class);
    }

    protected CustomMapSerializer2(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        /*
        String s=q.getPrefix() + ":" + q.getLocalPart();
        if (s.equals("prov:type")) {
            s= Constants.PROPERTY_AT_TYPE;
        }
        //serializerProvider.setAttribute(CONTEXT_KEY_FOR_MAP,s);

         */
        serializerProvider.setAttribute(CONTEXT_KEY_FOR_MAP,q);

    }
}