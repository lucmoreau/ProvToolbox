package org.openprovenance.prov.core.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.xml.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "alternate1", "alternate2"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface XML_AlternateOf extends HasKind {


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAlternate2();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAlternate1();

}
