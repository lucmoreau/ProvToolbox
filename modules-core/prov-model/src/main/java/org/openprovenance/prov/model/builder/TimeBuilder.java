package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;

abstract public class TimeBuilder<T extends TimeBuilder<T>> extends GenericBuilder<T> implements HasTime<T> {

    public TimeBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder, mc, pf);
    }

    protected XMLGregorianCalendar time;

    public void setTime(XMLGregorianCalendar time) {
        this.time=time;
    }
}
