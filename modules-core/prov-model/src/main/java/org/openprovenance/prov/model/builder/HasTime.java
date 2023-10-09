package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;

public interface HasTime<T extends GenericBuilder<T>> {

    ProvFactory pf();
    void setTime(XMLGregorianCalendar time);
    default T time(XMLGregorianCalendar time) {
        setTime(time);
        return (T) this;
    }
    default T now() {
        setTime(pf().newTimeNow());
        return(T) this;
    }
}
