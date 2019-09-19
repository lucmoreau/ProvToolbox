package org.openprovenance.prov.core.jsonld11;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.jsonld11.serialization.CustomAttributesSerializer;
import org.openprovenance.prov.core.jsonld11.serialization.CustomKeyDeserializer;
import org.openprovenance.prov.core.jsonld11.serialization.CustomMapSerializer2;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface HasAttributes extends org.openprovenance.prov.vanilla.HasAttributes {

    @JsonIgnore
    Collection<Attribute> getAttributes();

    @JsonAnySetter
    @JsonDeserialize(keyUsing= CustomKeyDeserializer.class)
    void setIndexedAttributes(Object qn, Set<Attribute> attributes);

    @JsonAnyGetter
    @JsonProperty("attributes")
    @JsonSerialize(keyUsing= CustomMapSerializer2.class, contentUsing = CustomAttributesSerializer.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<QualifiedName, Set<Attribute>> getIndexedAttributes();
}
