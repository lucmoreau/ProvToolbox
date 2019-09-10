package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.openprovenance.prov.core.jsonld.HasKind;
import org.openprovenance.prov.core.jsonld.Identifiable;
import org.openprovenance.prov.core.jsonld.serialization.Constants;
import org.openprovenance.prov.core.vanilla.*;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Statement;

import java.util.List;

@JsonPropertyOrder({ "@context", "@id", "@graph" })

public interface JSON_Bundle extends Identifiable, HasKind {

    @JsonProperty("@context")
    Namespace getNamespace();

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = Constants.PROPERTY_BLOCK_TYPE)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = WasEndedBy.class,         name = Constants.PROPERTY_PROV_END),
            @JsonSubTypes.Type(value = WasStartedBy.class,       name = Constants.PROPERTY_PROV_START),
            @JsonSubTypes.Type(value = WasInvalidatedBy.class,   name = Constants.PROPERTY_PROV_INVALIDATION),
            @JsonSubTypes.Type(value = HadMember.class,          name = Constants.PROPERTY_PROV_MEMBERSHIP),
            @JsonSubTypes.Type(value = WasInfluencedBy.class,    name = Constants.PROPERTY_PROV_INFLUENCE),
            @JsonSubTypes.Type(value = WasInformedBy.class,      name = Constants.PROPERTY_PROV_COMMUNICATION),
            @JsonSubTypes.Type(value = WasDerivedFrom.class,     name = Constants.PROPERTY_PROV_DERIVATION),
            @JsonSubTypes.Type(value = AlternateOf.class,        name = Constants.PROPERTY_PROV_ALTERNATE),
            @JsonSubTypes.Type(value = SpecializationOf.class,   name = Constants.PROPERTY_PROV_SPECIALIZATION),
            @JsonSubTypes.Type(value = WasAttributedTo.class,    name = Constants.PROPERTY_PROV_ATTRIBUTION),
            @JsonSubTypes.Type(value = WasAssociatedWith.class,  name = Constants.PROPERTY_PROV_ASSOCIATION),
            @JsonSubTypes.Type(value = WasGeneratedBy.class,     name = Constants.PROPERTY_PROV_GENERATION),
            @JsonSubTypes.Type(value = Used.class,               name = Constants.PROPERTY_PROV_USED),
            @JsonSubTypes.Type(value = Activity.class,           name = Constants.PROPERTY_PROV_ACTIVITY),
            @JsonSubTypes.Type(value = Agent.class,              name = Constants.PROPERTY_PROV_AGENT),
            @JsonSubTypes.Type(value = Entity.class,             name = Constants.PROPERTY_PROV_ENTITY),
            @JsonSubTypes.Type(value = ActedOnBehalfOf.class,    name = Constants.PROPERTY_PROV_DELEGATION)
    })
    @JsonProperty("@graph")
    List<Statement> getStatement();

}
