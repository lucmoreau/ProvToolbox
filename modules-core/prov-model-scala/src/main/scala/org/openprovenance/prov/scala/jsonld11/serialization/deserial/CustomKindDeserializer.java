package org.openprovenance.prov.scala.jsonld11.serialization.deserial;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;
import org.openprovenance.prov.model.StatementOrBundle;


public class CustomKindDeserializer extends StdDeserializer<StatementOrBundle.Kind> {

    public CustomKindDeserializer() {
        super(StatementOrBundle.Kind.class);
    }

    @Override
    public StatementOrBundle.Kind deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        return StatementOrBundle.Kind.PROV_ACTIVITY;
    }

    protected CustomKindDeserializer(Class<StatementOrBundle.Kind> t) {
        super(t);
    }

}