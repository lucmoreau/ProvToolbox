package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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

import java.util.List;

@JsonPropertyOrder({ "@id",  "collection", "entity"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_QualifiedHadMember extends JLD_Generic2, JLD_Qualified {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getCollection();

    @JsonDeserialize(contentUsing = CustomQualifiedNameDeserializer.class)
    List<QualifiedName> getEntity();

    /** The attribute keys, with the terms the openprov context scopes to this relation. */
    @JsonAnySetter
    @JsonDeserialize(keyUsing = ScopedKeyDeserializer.Membership.class)
    void setIndexedAttributes(Object qn, Set<Attribute> attributes);

    @JsonAnyGetter
    @JsonSerialize(keyUsing = ScopedKeySerializer.Membership.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<QualifiedName, Set<Attribute>> getIndexedAttributes();
}
