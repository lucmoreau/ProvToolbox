package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "influnencee", "influencer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasInfluencedBy extends JLD_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getInfluencee();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getInfluencer();


}
