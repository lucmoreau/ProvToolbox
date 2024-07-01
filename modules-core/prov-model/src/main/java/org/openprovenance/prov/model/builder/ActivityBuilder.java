package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;

import javax.xml.datatype.XMLGregorianCalendar;

public class ActivityBuilder extends GenericBuilder<ActivityBuilder>{

    private XMLGregorianCalendar start;
    private XMLGregorianCalendar end;

    public ActivityBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);

    }
    public ActivityBuilder start(XMLGregorianCalendar start) {
        this.start=start;
        return this;
    }
    public ActivityBuilder start(String start) {
        this.start=pf.newISOTime(start);
        return this;
    }
    public ActivityBuilder end(XMLGregorianCalendar end) {
        this.end=end;
        return this;
    }
    public ActivityBuilder end(String end) {
        this.end=pf.newISOTime(end);
        return this;
    }
    public Builder build() {
        parent.statements.add(mc.newActivity(id,start,end,attrs));
        return parent;
    }


    public ActivityBuilder aka() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }
}