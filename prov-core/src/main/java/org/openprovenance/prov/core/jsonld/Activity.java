package org.openprovenance.prov.core.jsonld;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;

class OLD_Activity extends org.openprovenance.prov.core.Activity implements JLD_Activity {
    private OLD_Activity() {}
    public OLD_Activity(QualifiedName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, Collection<Attribute> attributes) {
        super(id, startTime, endTime, attributes);
    }
}
