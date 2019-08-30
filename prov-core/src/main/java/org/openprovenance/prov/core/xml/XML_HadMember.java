package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.util.List;

@JsonPropertyOrder({ "collection", "entity"})

public interface XML_HadMember extends HasKind {


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getCollection();

    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializer.class)
    public List<QualifiedName> getEntity();
}
