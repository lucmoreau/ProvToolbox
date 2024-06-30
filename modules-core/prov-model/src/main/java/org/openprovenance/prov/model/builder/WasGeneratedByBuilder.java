package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasGeneratedByBuilder extends TimeBuilder<WasGeneratedByBuilder> implements HasRole<WasGeneratedByBuilder> {
    protected QualifiedName entity;
    protected QualifiedName activity;

    public WasGeneratedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasGeneratedByBuilder entity(QualifiedName entity) {
        this.entity=entity;
        return this;
    }
    public WasGeneratedByBuilder entity(String prefix, String local) {
        this.entity= qn(prefix,local);
        return this;
    }
    public WasGeneratedByBuilder entity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("entity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.entity=qn;
        return this;
    }


    public WasGeneratedByBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }
    public WasGeneratedByBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }
    public WasGeneratedByBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }


    public Builder build() {
        parent.statements.add(mc.newWasGeneratedBy(id, entity, activity, time, attrs));
        return parent;
    }

    public WasGeneratedByBuilder aka() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }


}
