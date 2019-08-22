package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.ProvFactory;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

public class CustomQualifiedNameDeserializer extends StdDeserializer<QualifiedName> {


    ProvFactory pf=new ProvFactory();

    public CustomQualifiedNameDeserializer() {
        this(QualifiedName.class);
    }


    public CustomQualifiedNameDeserializer(Class<?> vc) {
        super(vc);


    }

    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        return ns.stringToQualifiedName(jsonParser.getText(), pf, false);
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        return ns.stringToQualifiedName(s, pf, false);
    }
}
