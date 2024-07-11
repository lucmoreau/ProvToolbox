package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class UsedBuilder extends TimeBuilder<UsedBuilder> implements HasRole<UsedBuilder> {
    protected QualifiedName entity;
    protected QualifiedName activity;
    
    public UsedBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public UsedBuilder entity(QualifiedName entity) {
        this.entity=entity;
        return this;
    }
    public UsedBuilder entity(String prefix, String local) {
        this.entity= qn(prefix,local);
        return this;
    }
    public UsedBuilder entity(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.entity=qn;
        return this;
    }

    public UsedBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }
    public UsedBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }

    public UsedBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.activity=qn;
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newUsed(id, activity, entity, time, attrs));
        return parent;
    }

    public UsedBuilder aka() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }


}
