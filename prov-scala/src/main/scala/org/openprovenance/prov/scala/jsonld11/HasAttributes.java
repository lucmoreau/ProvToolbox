package org.openprovenance.prov.scala.jsonld11;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.scala.jsonld11.serialization.deserial.CustomKeyDeserializer;
import org.openprovenance.prov.scala.jsonld11.serialization.serial.CustomAttributesSerializer;
import org.openprovenance.prov.scala.jsonld11.serialization.serial.CustomMapSerializer2;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface HasAttributes extends org.openprovenance.prov.core.vanilla.HasAttributes {

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
