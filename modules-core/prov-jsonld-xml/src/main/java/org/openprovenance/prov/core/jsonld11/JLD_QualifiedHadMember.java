package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.util.List;

@JsonPropertyOrder({ "@id",  "collection", "entity"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_QualifiedHadMember extends JLD_Generic2, JLD_Qualified {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getCollection();

    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializer.class)
    public List<QualifiedName> getEntity();


}
