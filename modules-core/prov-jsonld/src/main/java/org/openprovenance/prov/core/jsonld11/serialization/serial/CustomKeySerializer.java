package org.openprovenance.prov.core.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.vanilla.QualifiedName;
import tools.jackson.databind.SerializationContext;

import static org.openprovenance.prov.core.jsonld11.serialization.Constants.ID_COERCED_ATTRIBUTE_TERMS;
import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_NS;


public class CustomKeySerializer extends StdSerializer<QualifiedName> {

    /** Whether the key just written is coerced to {@code @id}, so whether a qualified name under it is written as a bare string. */
    public static final String ID_COERCED_KEY = "prov:idCoerced";


    protected CustomKeySerializer() {
        super(QualifiedName.class);
    }

    protected CustomKeySerializer(Class<QualifiedName> t) {
        super(t);
    }

    @Override
    public void serialize(QualifiedName q, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        String s;
        if (q.getPrefix()==null || PROV_NS.equals(q.getNamespaceURI())) {
            s = q.getLocalPart();
        } else {
            s = q.getPrefix() + ":" + q.getLocalPart();
        }
        serializerProvider.setAttribute(ID_COERCED_KEY, PROV_NS.equals(q.getNamespaceURI()) && ID_COERCED_ATTRIBUTE_TERMS.contains(s));
        jsonGenerator.writeName(s);
    }
}