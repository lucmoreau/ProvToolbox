package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ModelConstructorExtension;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class SpecializationOfBuilder extends GenericBuilder<SpecializationOfBuilder>{
    protected final ModelConstructorExtension mce;
    protected QualifiedName specificEntity;
    protected QualifiedName generalEntity;

    public SpecializationOfBuilder(Builder builder, ModelConstructor mc, ModelConstructorExtension mce, ProvFactory pf) {
        super(builder,mc,pf);
        this.mce=mce;
    }
    public SpecializationOfBuilder specificEntity(QualifiedName specificEntity) {
        this.specificEntity=specificEntity;
        return this;
    }
    public SpecializationOfBuilder specificEntity(String prefix, String local) {
        this.specificEntity = qn(prefix,local);
        return this;
    }
    public SpecializationOfBuilder specificEntity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("specificEntity cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.specificEntity =qn;
        return this;
    }
    public SpecializationOfBuilder generalEntity(QualifiedName generalEntity) {
        this.generalEntity=generalEntity;
        return this;
    }
    public SpecializationOfBuilder generalEntity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("generalEntity cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.generalEntity=qn;
        return this;
    }
    public SpecializationOfBuilder generalEntity(String prefix, String local) {
        this.generalEntity = qn(prefix,local);
        return this;
    }

    public Builder build() {
        if ((id==null)&&attrs.isEmpty()) {
            parent.statements.add(mc.newSpecializationOf(specificEntity, generalEntity));
        } else {
            parent.statements.add(mce.newQualifiedSpecializationOf(id, specificEntity, generalEntity, attrs));
        }
        return parent;
    }

}
