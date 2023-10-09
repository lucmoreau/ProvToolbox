package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasEndedByBuilder extends TimeBuilder<WasEndedByBuilder> implements HasRole<WasEndedByBuilder> {
    protected QualifiedName entity;
    protected QualifiedName activity;
    protected QualifiedName ender;

    public WasEndedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasEndedByBuilder entity(QualifiedName entity) {
        this.entity=entity;
        return this;
    }
    public WasEndedByBuilder entity(String prefix, String local) {
        this.entity= qn(prefix,local);
        return this;
    }
    public WasEndedByBuilder entity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("entity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.entity=qn;
        return this;
    }


    public WasEndedByBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }
    public WasEndedByBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }
    public WasEndedByBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }

    public WasEndedByBuilder ender(QualifiedName ender) {
        this.ender=ender;
        return this;
    }
    public WasEndedByBuilder ender(String prefix, String local) {
        this.ender= qn(prefix,local);
        return this;
    }
    public WasEndedByBuilder ender(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("ender: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.ender=qn;
        return this;
    }



    public Builder build() {
        parent.statements.add(mc.newWasEndedBy(id, entity, activity, ender, time, attrs));
        return parent;
    }



}
