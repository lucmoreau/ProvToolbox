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

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "activity", "trigger", "ender", "time" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_WasEndedBy extends JLD_Generic, HasRole {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getTrigger();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getActivity();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    QualifiedName getEnder();

    XMLGregorianCalendar getTime();

    /** The attribute keys, with the terms the openprov context scopes to this relation. */
    @JsonAnySetter
    @JsonDeserialize(keyUsing = ScopedKeyDeserializer.End.class)
    void setIndexedAttributes(Object qn, Set<Attribute> attributes);

    @JsonAnyGetter
    @JsonSerialize(keyUsing = ScopedKeySerializer.End.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<QualifiedName, Set<Attribute>> getIndexedAttributes();
}
