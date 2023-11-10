package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasStartedByBuilder extends TimeBuilder<WasStartedByBuilder> implements  HasRole<WasStartedByBuilder> {
    protected QualifiedName entity;
    protected QualifiedName activity;
    protected QualifiedName starter;


    public WasStartedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasStartedByBuilder entity(QualifiedName entity) {
        this.entity=entity;
        return this;
    }
    public WasStartedByBuilder entity(String prefix, String local) {
        this.entity= qn(prefix,local);
        return this;
    }
    public WasStartedByBuilder entity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("entity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.entity=qn;
        return this;
    }


    public WasStartedByBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }
    public WasStartedByBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }
    public WasStartedByBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }

    public WasStartedByBuilder starter(QualifiedName starter) {
        this.starter=starter;
        return this;
    }
    public WasStartedByBuilder starter(String prefix, String local) {
        this.starter= qn(prefix,local);
        return this;
    }
    public WasStartedByBuilder starter(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("starter: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.starter=qn;
        return this;
    }
    public Builder build() {
        parent.statements.add(mc.newWasStartedBy(id, entity, activity, starter, time, attrs));
        return parent;
    }



}
