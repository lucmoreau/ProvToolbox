package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.*;
import org.openprovenance.prov.core.json.serialization.Constants;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.vanilla.*;

import java.util.List;

@JsonPropertyOrder({ "context", "statements"})
public interface JSON_Document {
    @JsonFilter("nsFilter")
    @JsonProperty("context")
    Namespace getNamespace();

 
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
            @JsonSubTypes.Type(value = ActedOnBehalfOf.class,    name = Constants.PROPERTY_PROV_DELEGATION),
            @JsonSubTypes.Type(value = Bundle.class,             name = Constants.PROPERTY_PROV_BUNDLE),
            @JsonSubTypes.Type(value = QualifiedSpecializationOf.class,        name = Constants.PROPERTY_PROV_QUALIFIED_SPECIALIZATION),
            @JsonSubTypes.Type(value = QualifiedAlternateOf.class,             name = Constants.PROPERTY_PROV_QUALIFIED_ALTERNATE),
            @JsonSubTypes.Type(value = QualifiedHadMember.class,               name = Constants.PROPERTY_PROV_QUALIFIED_MEMBERSHIP)
    })
    @JsonProperty("statements")
    List<StatementOrBundle> getStatementOrBundle();
}
