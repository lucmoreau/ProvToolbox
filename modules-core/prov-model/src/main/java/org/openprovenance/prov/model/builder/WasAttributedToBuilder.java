package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasAttributedToBuilder extends GenericBuilder<WasAttributedToBuilder>{
    protected QualifiedName entity;
    protected QualifiedName agent;
    protected XMLGregorianCalendar time;

    public WasAttributedToBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasAttributedToBuilder entity(QualifiedName entity) {
        this.entity=entity;
        return this;
    }

    public WasAttributedToBuilder entity(String prefix, String local) {
        this.entity= qn(prefix,local);
        return this;
    }

    public WasAttributedToBuilder entity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("entity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.entity=qn;
        return this;
    }

    public WasAttributedToBuilder agent(QualifiedName agent) {
        this.agent=agent;
        return this;
    }
    public WasAttributedToBuilder agent(String prefix, String local) {
        this.agent= qn(prefix,local);
        return this;
    }
    public WasAttributedToBuilder agent(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("agent cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.agent=qn;
        return this;
    }
    public WasAttributedToBuilder role(QualifiedName e) {
        attr(provRole,e,pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public WasAttributedToBuilder role(String prefix, String local) {
        attr(provRole, qn(prefix,local),pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public WasAttributedToBuilder role(String role) {
        attr(provRole,role,pf.getName().XSD_STRING);
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasAttributedTo(id, entity, agent, attrs));
        return parent;
    }



}
