package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "influnencee", "influencer"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_WasInfluencedBy extends XML_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getInfluencee();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getInfluencer();


}
