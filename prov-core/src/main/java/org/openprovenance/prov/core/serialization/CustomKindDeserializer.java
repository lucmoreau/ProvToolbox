package org.openprovenance.prov.core.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.model.StatementOrBundle;

import java.io.IOException;


public class CustomKindDeserializer extends StdDeserializer<StatementOrBundle.Kind> {

    protected CustomKindDeserializer() {
        super(StatementOrBundle.Kind.class);
    }

    @Override
    public StatementOrBundle.Kind deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return StatementOrBundle.Kind.PROV_ACTIVITY;
    }

    protected CustomKindDeserializer(Class<StatementOrBundle.Kind> t) {
        super(t);
    }

    public void serialize(StatementOrBundle.Kind kind, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String s=null;
        switch (kind) {
            case PROV_ENTITY:
                s="prov:entity";
                break;
            case PROV_ACTIVITY:
                s="prov:activity";
                break;
            case PROV_AGENT:
                s="prov:agent";
                break;
            case PROV_USAGE:
                s="prov:used";
                break;
            case PROV_GENERATION:
                s="prov:wasGeneratedBy";
                break;
            case PROV_INVALIDATION:
                s="prov:";
                break;
            case PROV_START:
                s="prov:";
                break;
            case PROV_END:
                s="prov:";
                break;
            case PROV_COMMUNICATION:
                s="prov:";
                break;
            case PROV_DERIVATION:
                s="prov:";
                break;
            case PROV_ASSOCIATION:
                s="prov:";
                break;
            case PROV_ATTRIBUTION:
                s="prov:";
                break;
            case PROV_DELEGATION:
                s="prov:";
                break;
            case PROV_INFLUENCE:
                s="prov:";
                break;
            case PROV_ALTERNATE:
                s="prov:";
                break;
            case PROV_SPECIALIZATION:
                s="prov:";
                break;
            case PROV_MENTION:
                s="prov:";
                break;
            case PROV_MEMBERSHIP:
                s="prov:";
                break;
            case PROV_BUNDLE:
                s="prov:";
                break;
            case PROV_DICTIONARY_INSERTION:
                s="prov:";
                break;
            case PROV_DICTIONARY_REMOVAL:
                s="prov:";
                break;
            case PROV_DICTIONARY_MEMBERSHIP:
                s="prov:";
                break;
        }
        jsonGenerator.writeString(s);
    }
}