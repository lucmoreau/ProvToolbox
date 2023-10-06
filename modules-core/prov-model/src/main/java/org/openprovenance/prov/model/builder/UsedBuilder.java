package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class UsedBuilder extends GenericBuilder<UsedBuilder>{
    protected QualifiedName e;
    protected QualifiedName a;
    protected XMLGregorianCalendar time;

    public UsedBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public UsedBuilder e(QualifiedName e) {
        this.e=e;
        return this;
    }
    public UsedBuilder e(String prefix, String local) {
        this.e=qn(prefix,local);
        return this;
    }
    public UsedBuilder e(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.e=qn;
        return this;
    }


    public UsedBuilder a(QualifiedName a) {
        this.a=a;
        return this;
    }
    public UsedBuilder a(String prefix, String local) {
        this.a=qn(prefix,local);
        return this;
    }
    public UsedBuilder a(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.a=qn;
        return this;
    }

    public UsedBuilder role(QualifiedName e) {
        attr(pf.getName().PROV_ROLE,e,pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public UsedBuilder role(String prefix, String local) {
        attr(pf.getName().PROV_ROLE,qn(prefix,local),pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public UsedBuilder role(String role) {
        attr(pf.getName().PROV_ROLE,role,pf.getName().XSD_STRING);
        return this;
    }


    public UsedBuilder time(XMLGregorianCalendar time) {
        this.time=time;
        return this;
    }
    public UsedBuilder now() {
        this.time=parent.pf.newTimeNow();
        return this;
    }
    public Builder build() {
        parent.statements.add(mc.newUsed(id,a,e,time,attrs));
        return parent;
    }



}
