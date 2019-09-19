package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.openprovenance.prov.scala.immutable.QualifiedName;
import scala.collection.immutable.Set;

@JsonPropertyOrder({ "@id" })
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface JLD_Generic extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {

    @JsonIgnore
    org.openprovenance.prov.scala.immutable.QualifiedName getCause();

    @JsonIgnore
    org.openprovenance.prov.scala.immutable.QualifiedName getEffect();

    @JsonIgnore
    Set<? extends QualifiedName> getOtherCause();



}
