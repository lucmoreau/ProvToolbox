package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasInvalidatedByBuilder extends TimeBuilder<WasInvalidatedByBuilder> implements HasRole<WasInvalidatedByBuilder> {
    protected QualifiedName entity;
    protected QualifiedName activity;

    public WasInvalidatedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasInvalidatedByBuilder entity(QualifiedName entity) {
        this.entity=entity;
        return this;
    }
    public WasInvalidatedByBuilder entity(String prefix, String local) {
        this.entity= qn(prefix,local);
        return this;
    }
    public WasInvalidatedByBuilder entity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("entity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.entity=qn;
        return this;
    }


    public WasInvalidatedByBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }
    public WasInvalidatedByBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }
    public WasInvalidatedByBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }


    public Builder build() {
        parent.statements.add(mc.newWasInvalidatedBy(id, entity, activity, time, attrs));
        return parent;
    }



}
