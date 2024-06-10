package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.util.List;

@JsonPropertyOrder({ "collection", "entity"})

public interface JLD_HadMember extends HasKind {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getCollection();

    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializer.class)
    List<QualifiedName> getEntity();
}
