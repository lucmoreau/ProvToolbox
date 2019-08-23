package org.openprovenance.prov.core.serialization.attic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.core.ProvFactory;
import org.openprovenance.prov.core.Type;
import org.openprovenance.prov.core.serialization.CustomNamespaceDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class CustomTypeDeserializer extends StdDeserializer<Type> {


    ProvFactory pf=new ProvFactory();

    public CustomTypeDeserializer() {
        this(Attribute.class);
    }


    public CustomTypeDeserializer(Class<?> vc) {
        super(vc);
        //Namespace ns=new Namespace();

    }

    @Override
    public Type deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Namespace ns= (Namespace) deserializationContext.getAttribute(CustomNamespaceDeserializer.CONTEXT_KEY_NAMESPACE);

        JsonNode node = jp.getCodec().readTree(jp);
        //System.out.println(node);
        Map.Entry<String, JsonNode> pair=node.fields().next();

        QualifiedName elementName=ns.stringToQualifiedName(pair.getKey(),pf);
        JsonNode vObj=pair.getValue();

        Iterator<Map.Entry<String, JsonNode>> pairs=vObj.fields();
        String type=pairs.next().getValue().textValue();
        String value=pairs.next().getValue().textValue();

        //System.out.println("Found value " + value);
        //System.out.println("Found type " + type);

        QualifiedName typeQN=ns.stringToQualifiedName(type,pf);
        if (pf.getName().PROV_QUALIFIED_NAME.equals(typeQN)) {
            return new Type(ns.stringToQualifiedName(type,pf),ns.stringToQualifiedName(value,pf));
        } else { //TODO: need to handle LangString
            return new Type(ns.stringToQualifiedName(type,pf),value);

        }


    }
}
