package org.openprovenance.prov.core.jsonld11.serialization.deserial.attic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomAttributeDeserializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CustomAttributeSetDeserializer extends StdDeserializer<Set> {


    static final ProvFactory pf=new ProvFactory();

    static final CustomAttributeDeserializer customAttributeDeserializerWithRootName = new CustomAttributeDeserializer();

    public CustomAttributeSetDeserializer(JavaType vc) {
        super(vc);
    }

    //@Override
    public Set OLDdeserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        System.out.println("OLD CustomAttributeSetDeserializer");


        Iterator<JsonNode> elements=node.elements();
        Set<Attribute> set=new HashSet<>();
        while (elements.hasNext()) {
            JsonNode next=elements.next();

            Attribute attr;
            if (next.isObject() ) {
                attr = customAttributeDeserializerWithRootName.deserialize(next, deserializationContext);
            } else {
                attr = customAttributeDeserializerWithRootName.deserialize(next.textValue(), deserializationContext);
            }
            set.add(attr);


        }
        return set;
    }




    //CollectionDeserializer collectionDeserializer=new CollectionDeserializer();

    //LUC FIXME: Make this the new method.
    // Requires CustomAttributeDeserializerWithRootName to be declared as deserialiser for attribute.

    public Set deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        System.out.println("NEW CustomAttributeSetDeserializer " + deserializationContext);
       // ObjectCodec codec = jp.getCodec();

        TypeFactory tf=deserializationContext.getTypeFactory();
        JavaType customAttributeSet = tf.constructCollectionType(Set.class, Attribute.class);



        System.out.println("start array? " + jp.isExpectedStartArrayToken());
        System.out.println("start array? " + jp.getCurrentToken());

       JsonToken tok=jp.nextToken();
       System.out.println("start array-> " + tok);
        //QualifiedName context=(QualifiedName)deserializationContext.getAttribute(CustomKeyDeserializer.PROV_ATTRIBUTE_CONTEXT_KEY);

        Set<Attribute> set=new HashSet<>();


      //  Collection coll=collectionDeserializer.deserialize(jp,deserializationContext);


        //FIXME:  ISSUE the deserilaization context, with the namespace, is not passed by jp.readValues.
        for (Iterator<Attribute> it = jp.readValuesAs(Attribute.class); it.hasNext(); ) {
           Attribute attr = it.next();
            set.add(attr);
       }


       // set.addAll(coll);

        System.out.println(" CustomAttributeSetDeserializer " + jp.getCurrentToken());
        return set;
    }

}
