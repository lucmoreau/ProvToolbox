package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.util.List;

@JsonPropertyOrder({ "collection", "entity"})

public interface JSON_HadMember extends HasKind {


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getCollection();

    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializer.class)
    public List<QualifiedName> getEntity();
}
