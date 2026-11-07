package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.Namespace;

import java.util.Iterator;


public class CustomNamespaceDeserializer extends StdDeserializer<Namespace> {

    public static final Object CONTEXT_KEY_NAMESPACE = "CONTEXT_KEY_NAMESPACE";
    private final JavaType tr;


    public CustomNamespaceDeserializer(JavaType tr) {
        super(tr);
        this.tr=tr;
    }

    @Override
    public Namespace deserialize(JsonParser jp, DeserializationContext deserializationContext) {
        

          Namespace ns=new Namespace();

          JsonNode x=deserializationContext.readTree(jp) /* jackson 3: the ongoing context, see catalogue #21 */;
          if (x.isArray()) {
              for (int i=0; i < x.size(); i++) {
                  JsonNode n=x.get(i);
                  if (n.isObject()) {
                      for (Iterator<String> it = n.propertyNames().iterator(); it.hasNext(); ) {
                          String field = it.next();
                          JsonNode value=n.get(field);
                          if (value.isObject()) {
                             // ignore, it's a JSONLD Mapping
                          } else {
                              String s=value.stringValue(null);
                              //System.out.println("[" + field + "]" + "[" + s + "]");
                              if ("@namespace".equals(field)) {
                                  ns.setDefaultNamespace(s);
                              } else if ("@version".equals(field)) {
                                  // ignore
                              }
                              else {
                                  ns.register(field, s);
                              }

                          }

                      }

                  }
              }
          }


        deserializationContext.setAttribute(CONTEXT_KEY_NAMESPACE,ns);


        return ns;
    }
}
