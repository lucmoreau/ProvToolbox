package org.openprovenance.prov.core.json.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.json.serialization.Constants;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;

public class CustomDeferredQualifiedNameDeserializer extends StdDeserializer<QualifiedName> {


    static final ProvFactory pf=new ProvFactory();
    static final QualifiedName PROV_TYPE=pf.getName().PROV_TYPE;
    public static final String OPENPROVENANCE_DEFERREDNS_DEFAULTNS = "https://openprovenance/deferredns/defaultns";
    public static final String OPENPROVENANCE_DEFERREDNS_DEFERREDNS = "https://openprovenance/deferredns/deferredns";

    public CustomDeferredQualifiedNameDeserializer() {
        this(QualifiedName.class);
    }


    public CustomDeferredQualifiedNameDeserializer(Class<?> vc) {
        super(vc);


    }

    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        if (Constants.PROPERTY_AT_TYPE.equals(text)) return PROV_TYPE;
        return getDeferredQualifiedName(text);
    }

    public QualifiedName deserialize(String text, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return getDeferredQualifiedName(text);
    }

    private QualifiedName getDeferredQualifiedName(String text) {
        final int index = text.indexOf(':');
        if (index == -1) {
            return pf.newQualifiedName(OPENPROVENANCE_DEFERREDNS_DEFAULTNS, text, null);
        } else {
            final String prefix = text.substring(0, index);
            final String local = text.substring(index + 1);
            return pf.newQualifiedName(OPENPROVENANCE_DEFERREDNS_DEFERREDNS, local, prefix);
        }
    }
}
