package org.openprovenance.prov.core.jsonld11.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.Constants;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;

import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.CONTEXT_KEY_NAMESPACE;
import static org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomThreadConfig.getAttributes;

public class CustomQualifiedNameDeserializer extends StdDeserializer<QualifiedName> {


    static final ProvFactory pf=new ProvFactory();
    static final QualifiedName PROV_TYPE=pf.getName().PROV_TYPE;

    public CustomQualifiedNameDeserializer() {
        this(QualifiedName.class);
    }


    public CustomQualifiedNameDeserializer(Class<?> vc) {
        super(vc);


    }

    @Override
    public QualifiedName deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        //Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Namespace ns= getAttributes().get().get(CONTEXT_KEY_NAMESPACE);

        String text = jsonParser.getText();
        //System.out.println("--> CustomQualifiedNameDeserializer deserialize: " + text + " : thread " + Thread.currentThread().getId());

        if (Constants.PROPERTY_AT_TYPE.equals(text)) return PROV_TYPE;
        return ns.stringToQualifiedName(text, pf, true);  // FIXME: is this correct, is it escaped?
    }


    public QualifiedName deserialize(String s, DeserializationContext deserializationContext) throws IOException {

        //Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);
        Namespace ns= getAttributes().get().get(CONTEXT_KEY_NAMESPACE);


        if (Constants.PROPERTY_AT_TYPE.equals(s)) return PROV_TYPE;
        return ns.stringToQualifiedName(s, pf, true);
    }
}
