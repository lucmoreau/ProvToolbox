package org.openprovenance.prov.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.json.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import java.util.List;

@JsonPropertyOrder({ "@id",  "collection", "entity"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JSON_QualifiedHadMember extends JSON_Generic2, JSON_Qualified {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getCollection();

    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializer.class)
    public List<QualifiedName> getEntity();


}
