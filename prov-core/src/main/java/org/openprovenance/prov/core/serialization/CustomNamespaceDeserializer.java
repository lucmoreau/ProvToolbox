package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.openprovenance.prov.model.Namespace;

import java.io.IOException;
import java.util.*;


public class CustomNamespaceDeserializer extends StdDeserializer<Namespace> {

    JavaType tr;

    public CustomNamespaceDeserializer(JavaType tr) {
        super(tr);
        this.tr=tr;

    }

    @Override
    public Namespace deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        String key=jp.getCurrentName();

        System.out.println("Key " + key);
        //JsonNode map=jp.getCodec().readTree(jp);
        //Hashtable<String,String> map=jp.getCodec().readValue(jp, Hashtable.class);
        //HashMap<String,String> map2=jp.getCodec().readValue(jp, HashMap.class);



        JsonNode map2=jp.readValueAsTree();


        System.out.println("Key " + map2);

        


        Namespace ns=new Namespace();

        System.out.println("NS " + ns);

        return ns;
    }
}
