package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import javax.xml.datatype.XMLGregorianCalendar;

public class WasGeneratedByBuilder extends GenericBuilder<WasGeneratedByBuilder>{
    protected QualifiedName e;
    protected QualifiedName a;
    protected XMLGregorianCalendar time;

    public WasGeneratedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasGeneratedByBuilder e(QualifiedName e) {
        this.e=e;
        return this;
    }
    public WasGeneratedByBuilder e(String prefix, String local) {
        this.e=qn(prefix,local);
        return this;
    }
    public WasGeneratedByBuilder e(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.e=qn;
        return this;
    }


    public WasGeneratedByBuilder a(QualifiedName a) {
        this.a=a;
        return this;
    }
    public WasGeneratedByBuilder a(String prefix, String local) {
        this.a=qn(prefix,local);
        return this;
    }
    public WasGeneratedByBuilder a(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.a=qn;
        return this;
    }

    public WasGeneratedByBuilder time(XMLGregorianCalendar time) {
        this.time=time;
        return this;
    }
    public WasGeneratedByBuilder now() {
        this.time=parent.pf.newTimeNow();
        return this;
    }

    public WasGeneratedByBuilder role(QualifiedName e) {
        attr(pf.getName().PROV_ROLE,e,pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public WasGeneratedByBuilder role(String prefix, String local) {
        attr(pf.getName().PROV_ROLE,qn(prefix,local),pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public WasGeneratedByBuilder role(String role) {
        attr(pf.getName().PROV_ROLE,role,pf.getName().XSD_STRING);
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasGeneratedBy(id,e,a,time,attrs));
        return parent;
    }



}
