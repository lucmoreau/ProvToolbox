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

static public Namespace theNS;

    private final Namespace ns;
    ProvFactory pf=new ProvFactory();

    public CustomQualifiedNameDeserializer() {
        this(QualifiedName.class);
    }


    public CustomQualifiedNameDeserializer(Class<?> vc) {
        super(vc);
        Namespace ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register("ex", "http://example.org/");
        ns.register("ex2", "http://example2.org/");
        this.ns=ns;
        theNS=ns;

    }

    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return ns.stringToQualifiedName(jsonParser.getText(), pf, false);
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return ns.stringToQualifiedName(s, pf, false);
    }
}
