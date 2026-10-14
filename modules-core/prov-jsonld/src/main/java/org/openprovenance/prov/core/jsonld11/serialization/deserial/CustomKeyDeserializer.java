package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.ID_COERCED_ATTRIBUTE_TERMS;

public class CustomKeyDeserializer extends KeyDeserializer {

    public static final String PROV_ATTRIBUTE_CONTEXT_KEY = "prov:Attribute";
    /** Whether the key just read is coerced to {@code @id}, so whether a bare string under it is a qualified name. */
    public static final String ID_COERCED_CONTEXT_KEY = "prov:idCoerced";
    public static final CustomQualifiedNameDeserializer customQualifiedNameDeserializer = new CustomQualifiedNameDeserializer();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        final QualifiedName qn = customQualifiedNameDeserializer.deserialize(s, deserializationContext);
        deserializationContext.setAttribute(PROV_ATTRIBUTE_CONTEXT_KEY,qn);
        deserializationContext.setAttribute(ID_COERCED_CONTEXT_KEY, ID_COERCED_ATTRIBUTE_TERMS.contains(s));
        return qn;
    }
}
