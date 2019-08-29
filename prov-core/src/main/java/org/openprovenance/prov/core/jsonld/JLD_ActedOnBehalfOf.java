package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.openprovenance.prov.core.serialization.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

@JsonPropertyOrder({ "@id", "delegate", "responsible", "activity" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface JLD_ActedOnBehalfOf extends JLD_Generic2 {

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getDelegate();

    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getResponsible();


    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    public QualifiedName getActivity();



}
