package org.openprovenance.prov.core.jsonld.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
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

    public static final Object CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";
    private final JavaType tr;


    public CustomNamespaceDeserializer(JavaType tr) {
        super(tr);
        this.tr=tr;
    }

    @Override
    public Namespace deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        

          Namespace ns=new Namespace();

          JsonNode x=jp.getCodec().readTree(jp);
          if (x.isArray()) {
              for (int i=0; i < x.size(); i++) {
                  JsonNode n=x.get(i);
                  if (n.isObject()) {
                      for (Iterator<String> it = n.fieldNames(); it.hasNext(); ) {
                          String field = it.next();
                          JsonNode value=n.get(field);
                          if (value.isObject()) {
                             // ignore, it's a JSONLD Mapping
                          } else {
                              String s=value.textValue();
                              //System.out.println("[" + field + "]" + "[" + s + "]");
                              if ("@namespace".equals(field)) {
                                  ns.setDefaultNamespace(s);
                              } else {
                                  ns.register(field, s);
                              }

                          }

                      }

                  }
              }
          }


      //  System.out.println("NS " + ns);
        deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE,ns);


        return ns;
    }
}
