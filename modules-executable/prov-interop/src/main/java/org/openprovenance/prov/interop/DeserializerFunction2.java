package org.openprovenance.prov.interop;

/* Functional interface, returning a deserializer function.  */

import org.openprovenance.prov.model.DateTimeOption;

import java.util.TimeZone;

public interface DeserializerFunction2 {
        org.openprovenance.prov.model.ProvDeserialiser apply(DateTimeOption dateTimeOption, TimeZone timeZone);
}
