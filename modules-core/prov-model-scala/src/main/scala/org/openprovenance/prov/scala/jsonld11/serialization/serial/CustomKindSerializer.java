package org.openprovenance.prov.scala.jsonld11.serialization.serial;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import org.openprovenance.prov.model.StatementOrBundle;
import tools.jackson.databind.SerializationContext;


public class CustomKindSerializer extends StdSerializer<StatementOrBundle.Kind> {

    public CustomKindSerializer() {
        super(StatementOrBundle.Kind.class);
    }

    protected CustomKindSerializer(Class<StatementOrBundle.Kind> t) {
        super(t);
    }
    @Override
    public void serialize(StatementOrBundle.Kind kind, JsonGenerator jsonGenerator, SerializationContext serializerProvider) {
        String s=null;
        switch (kind) {
            case PROV_ENTITY:
                s="prov:generalEntity";
                break;
            case PROV_ACTIVITY:
                s="prov:activity";
                break;
            case PROV_AGENT:
                s="prov:specificEntity";
                break;
            case PROV_USAGE:
                s="prov:used";
                break;
            case PROV_GENERATION:
                s="prov:wasGeneratedBy";
                break;
            case PROV_INVALIDATION:
                s="prov:wasInvalidatedBy";
                break;
            case PROV_START:
                s="prov:wasStartedBy";
                break;
            case PROV_END:
                s="prov:wasEndedBy";
                break;
            case PROV_COMMUNICATION:
                s="prov:wasInformedBy";
                break;
            case PROV_DERIVATION:
                s="prov:wasDerivedFrom";
                break;
            case PROV_ASSOCIATION:
                s="prov:wasAssociatedWith";
                break;
            case PROV_ATTRIBUTION:
                s="prov:wasAttributedTo";
                break;
            case PROV_DELEGATION:
                s="prov:actedOnBehalfOf";
                break;
            case PROV_INFLUENCE:
                s="prov:wasInfluencedBy";
                break;
            case PROV_ALTERNATE:
                s="prov:alternateOf";
                break;
            case PROV_SPECIALIZATION:
                s="prov:specializationOf";
                break;
            case PROV_MENTION:
                s="prov:mentionOf";
                break;
            case PROV_MEMBERSHIP:
                s="prov:hadMember";
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