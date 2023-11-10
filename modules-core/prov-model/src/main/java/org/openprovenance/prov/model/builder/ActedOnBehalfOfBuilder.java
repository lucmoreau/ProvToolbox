package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class ActedOnBehalfOfBuilder extends GenericBuilder<ActedOnBehalfOfBuilder>{
    protected QualifiedName delegate;
    protected QualifiedName responsible;
    protected QualifiedName activity;
    protected XMLGregorianCalendar time;

    public ActedOnBehalfOfBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public ActedOnBehalfOfBuilder delegate(QualifiedName delegate) {
        this.delegate=delegate;
        return this;
    }

    public ActedOnBehalfOfBuilder delegate(String prefix, String local) {
        this.delegate=qn(prefix,local);
        return this;
    }

    public ActedOnBehalfOfBuilder delegate(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("delegate cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.delegate =qn;
        return this;
    }

    public ActedOnBehalfOfBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }

    public ActedOnBehalfOfBuilder activity(String prefix, String local) {
        this.activity=qn(prefix,local);
        return this;
    }

    public ActedOnBehalfOfBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }

    public ActedOnBehalfOfBuilder responsible(QualifiedName responsible) {
        this.responsible=responsible;
        return this;
    }
    public ActedOnBehalfOfBuilder responsible(String prefix, String local) {
        this.responsible = qn(prefix,local);
        return this;
    }
    public ActedOnBehalfOfBuilder responsible(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("responsible cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.responsible =qn;
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newActedOnBehalfOf(id, delegate, responsible, activity, attrs));
        return parent;
    }



}
