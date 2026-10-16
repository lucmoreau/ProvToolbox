package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.ScopedKeyDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.serial.ScopedKeySerializer;
import org.openprovenance.prov.model.Attribute;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.jsonld11.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

@JsonPropertyOrder({ "@id", "informed", "informant"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasInformedBy extends JLD_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getInformed();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getInformant();

    /** The attribute keys, with the terms the openprov context scopes to this relation. */
    @JsonAnySetter
    @JsonDeserialize(keyUsing = ScopedKeyDeserializer.Communication.class)
    void setIndexedAttributes(Object qn, Set<Attribute> attributes);

    @JsonAnyGetter
    @JsonSerialize(keyUsing = ScopedKeySerializer.Communication.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<QualifiedName, Set<Attribute>> getIndexedAttributes();
}
