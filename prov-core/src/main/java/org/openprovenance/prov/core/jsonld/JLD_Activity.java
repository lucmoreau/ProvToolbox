package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.core.serialization.CustomAttributesSerializer;
import org.openprovenance.prov.core.serialization.CustomKeyDeserializer;
import org.openprovenance.prov.core.serialization.CustomMapSerializer;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonPropertyOrder({ "@id", "startTime", "endTime" })

public interface JLD_Activity extends Identifiable, HasKind, HasLabel, HasLocation, HasType, HasOther, HasAttributes {


    XMLGregorianCalendar getStartTime();

    void setStartTime(XMLGregorianCalendar value);

    XMLGregorianCalendar getEndTime();

    void setEndTime(XMLGregorianCalendar value);



}
