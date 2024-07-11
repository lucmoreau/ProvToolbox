package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasAssociatedWithBuilder extends GenericBuilder<WasAssociatedWithBuilder> implements HasRole<WasAssociatedWithBuilder> {
    protected QualifiedName activity;
    protected QualifiedName agent;
    protected QualifiedName plan;
    protected XMLGregorianCalendar time;

    public WasAssociatedWithBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasAssociatedWithBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }

    public WasAssociatedWithBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }

    public WasAssociatedWithBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }

    public WasAssociatedWithBuilder plan(QualifiedName plan) {
        this.plan=plan;
        return this;
    }

    public WasAssociatedWithBuilder plan(String prefix, String local) {
        this.plan= qn(prefix,local);
        return this;
    }

    public WasAssociatedWithBuilder plan (String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("plan cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.plan=qn;
        return this;
    }

    public WasAssociatedWithBuilder agent(QualifiedName agent) {
        this.agent=agent;
        return this;
    }
    public WasAssociatedWithBuilder agent(String prefix, String local) {
        this.agent= qn(prefix,local);
        return this;
    }
    public WasAssociatedWithBuilder agent(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("agent cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.agent=qn;
        return this;
    }

    public WasAssociatedWithBuilder aka() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasAssociatedWith(id, activity, agent, plan, attrs));
        return parent;
    }



}
