package org.openprovenance.prov.core.xml.serialization;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

import static org.openprovenance.prov.core.xml.serialization.CustomAttributeDeserializerWithRootName.unescapeQualifiedName;

public class CustomKeyDeserializer extends KeyDeserializer {

    public static final String PROV_ATTRIBUTE_CONTEXT_KEY = "prov:Attribute";

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        QualifiedName qn = new CustomQualifiedNameDeserializer().deserialize(s, deserializationContext);
        qn=unescapeQualifiedName(qn);
        deserializationContext.setAttribute(PROV_ATTRIBUTE_CONTEXT_KEY,qn);
        return qn;
    }
}
