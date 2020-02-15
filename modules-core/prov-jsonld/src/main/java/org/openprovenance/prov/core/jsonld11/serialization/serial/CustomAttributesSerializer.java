package org.openprovenance.prov.core.jsonld11.serialization.serial;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.vanilla.TypedValue;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.openprovenance.prov.core.jsonld11.serialization.serial.CustomAttributeValueSerializer.prnt;


public class CustomAttributesSerializer extends StdSerializer<Set> implements Constants {
    static final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;


    protected CustomAttributesSerializer() {
        super(Set.class);

    }

    protected CustomAttributesSerializer(Class<Set> t) {
        super(t);
    }

    @Override
    public void serialize(Set o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String newKey=(String)serializerProvider.getAttribute(CustomMapSerializer.CONTEXT_KEY_FOR_MAP);
        Set<TypedValue> set=(Set<TypedValue>) o;
        if (!(set.isEmpty())) {
            List<Object> set2=new LinkedList<>();
            for (TypedValue t: set) {  //FIXME: can I do this without creating a new set?
                if (t.getValue() instanceof QualifiedName) {
                    set2.add(prnt((QualifiedName)t.getValue()));
                } else {
                    set2.add(t);
                }
            }
            jsonGenerator.writeFieldName(newKey);
            System.out.println("hello hehllo " + newKey);
            jsonGenerator.writeObject(set2);
        }
    }
}