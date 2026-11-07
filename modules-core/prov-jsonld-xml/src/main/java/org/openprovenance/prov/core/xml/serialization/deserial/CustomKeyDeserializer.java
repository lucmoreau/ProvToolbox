package org.openprovenance.prov.core.xml.serialization.deserial;

import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.KeyDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

public class CustomKeyDeserializer extends KeyDeserializer {

    public static final String PROV_ATTRIBUTE_CONTEXT_KEY = "prov:Attribute";

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) {
        QualifiedName qn = new CustomQualifiedNameDeserializer().deserialize(s, deserializationContext);
        qn=DeserializerUtil.unescapeQualifiedName(qn);
        deserializationContext.setAttribute(PROV_ATTRIBUTE_CONTEXT_KEY,qn);
        return qn;
    }
}
