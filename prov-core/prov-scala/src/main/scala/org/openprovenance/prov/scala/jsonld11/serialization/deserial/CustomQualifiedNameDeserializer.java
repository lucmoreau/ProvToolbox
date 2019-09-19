package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.scala.immutable.ProvFactory;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

public class CustomQualifiedNameDeserializer extends StdDeserializer<QualifiedName> {


    static final ProvFactory pf=ProvFactory.pf();
    static final QualifiedName PROV_TYPE=pf.getName().PROV_TYPE;

    public CustomQualifiedNameDeserializer() {
        this(QualifiedName.class);
    }


    public CustomQualifiedNameDeserializer(Class<?> vc) {
        super(vc);


    }

    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        String text = jsonParser.getText();
        if (Constants.PROPERTY_AT_TYPE.equals(text)) return PROV_TYPE;
        return ns.stringToQualifiedName(text, pf, false);
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        if (Constants.PROPERTY_AT_TYPE.equals(s)) return PROV_TYPE;
        return ns.stringToQualifiedName(s, pf, false);
    }
}
