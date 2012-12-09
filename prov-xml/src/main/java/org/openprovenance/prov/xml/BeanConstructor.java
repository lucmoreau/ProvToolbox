package org.openprovenance.prov.xml;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/** An interface for constructing Java Beans for the PROV data model */

public interface BeanConstructor {
    
    public Entity newEntity(QName id, List<Attribute> attributes);
    public Activity newActivity(QName id, XMLGregorianCalendar startTime, XMLGregorianCalendar endTime, List<Attribute> attributes);
    public Agent newAgent(QName id, List<Attribute> attributes);

}
