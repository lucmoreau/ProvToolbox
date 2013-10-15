package org.openprovenance.prov.model;

import javax.xml.datatype.XMLGregorianCalendar;

public interface HasTime {
    public XMLGregorianCalendar getTime();
    public void setTime(XMLGregorianCalendar time);
} 