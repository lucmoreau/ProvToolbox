package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ModelConstructorExtension;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class AlternateOfBuilder extends GenericBuilder<AlternateOfBuilder>{
    protected final ModelConstructorExtension mce;
    protected QualifiedName e2;
    protected QualifiedName e1;

    public AlternateOfBuilder(Builder builder, ModelConstructor mc, ModelConstructorExtension mce, ProvFactory pf) {
        super(builder,mc,pf);
        this.mce=mce;
    }
    public AlternateOfBuilder e2(QualifiedName specificEntity) {
        this.e2 =specificEntity;
        return this;
    }
    public AlternateOfBuilder e2(String prefix, String local) {
        this.e2 = qn(prefix,local);
        return this;
    }
    public AlternateOfBuilder e2(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e2 cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.e2 =qn;
        return this;
    }
    public AlternateOfBuilder e1(QualifiedName e1) {
        this.e1=e1;
        return this;
    }
    public AlternateOfBuilder e1(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("generalEntity cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.e1 =qn;
        return this;
    }
    public AlternateOfBuilder e1(String prefix, String local) {
        this.e1 = qn(prefix,local);
        return this;
    }

    public Builder build() {
        if ((id==null)&&attrs.isEmpty()) {
            parent.statements.add(mc.newAlternateOf(e2, e1));
        } else {
            parent.statements.add(mce.newQualifiedAlternateOf(id, e2, e1, attrs));
        }
        return parent;
    }

}
