package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

public class CustomKeyDeserializer extends KeyDeserializer {

    public static final String PROV_ATTRIBUTE_CONTEXT_KEY = "prov:Attribute";
    public static final CustomQualifiedNameDeserializer customQualifiedNameDeserializer = new CustomQualifiedNameDeserializer();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {

        final QualifiedName qn = customQualifiedNameDeserializer.deserialize(s, deserializationContext);
        deserializationContext.setAttribute(PROV_ATTRIBUTE_CONTEXT_KEY,qn);
        return qn;
    }
}
