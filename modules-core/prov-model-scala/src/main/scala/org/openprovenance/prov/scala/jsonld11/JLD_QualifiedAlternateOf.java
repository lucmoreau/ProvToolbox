package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id",  "alternate1", "alternate2" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_QualifiedAlternateOf extends JLD_Generic2, JLD_Qualified {


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAlternate2();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getAlternate1();


}
