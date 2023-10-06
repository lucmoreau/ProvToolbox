package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class WasDerivedFromBuilder extends GenericBuilder<WasDerivedFromBuilder>{
    protected QualifiedName e2;
    protected QualifiedName e1;
    protected QualifiedName activity=null;
    protected QualifiedName generation=null;
    protected QualifiedName usage=null;

    public WasDerivedFromBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }
    public WasDerivedFromBuilder e2(QualifiedName e2) {
        this.e2 =e2;
        return this;
    }
    public WasDerivedFromBuilder e2(String prefix, String local) {
        this.e2=qn(prefix,local);
        return this;
    }
    public WasDerivedFromBuilder e2(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e2 cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.e2= qn;
        return this;
    }
    public WasDerivedFromBuilder e1(QualifiedName e1) {
        this.e1 =e1;
        return this;
    }
    public WasDerivedFromBuilder e1(String knownAs) {
        QualifiedName qn = parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e1 cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.e1=qn;
        return this;
    }
    public WasDerivedFromBuilder e1(String prefix, String local) {
        this.e1=qn(prefix,local);
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasDerivedFrom(id,e2,e1,activity,generation,usage,attrs));
        return parent;
    }

}
