package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class WasDerivedFromBuilder extends GenericBuilder<WasDerivedFromBuilder>{
    protected QualifiedName generatedEntity;
    protected QualifiedName usedEntity;
    protected QualifiedName activity=null;
    protected QualifiedName generation=null;
    protected QualifiedName usage=null;

    public WasDerivedFromBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }
    public WasDerivedFromBuilder generatedEntity(QualifiedName generatedEntity) {
        this.generatedEntity=generatedEntity;
        return this;
    }
    public WasDerivedFromBuilder generatedEntity(String prefix, String local) {
        this.generatedEntity= qn(prefix,local);
        return this;
    }
    public WasDerivedFromBuilder generatedEntity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e2 cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.generatedEntity=qn;
        return this;
    }
    public WasDerivedFromBuilder usedEntity(QualifiedName usedEntity) {
        this.usedEntity=usedEntity;
        return this;
    }
    public WasDerivedFromBuilder usedEntity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("e1 cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.usedEntity=qn;
        return this;
    }
    public WasDerivedFromBuilder usedEntity(String prefix, String local) {
        this.usage= qn(prefix,local);
        return this;
    }

    public WasDerivedFromBuilder usage(QualifiedName usage) {
        this.usage=usage;
        return this;
    }
    public WasDerivedFromBuilder usage(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("usage cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.usage=qn;
        return this;
    }

    public WasDerivedFromBuilder usage(String prefix, String local) {
        this.usedEntity= qn(prefix,local);
        return this;
    }
    public WasDerivedFromBuilder generation(QualifiedName generation) {
        this.generation=generation;
        return this;
    }
    public WasDerivedFromBuilder generation(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("generation cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.generation=qn;
        return this;
    }

    public WasDerivedFromBuilder generation(String prefix, String local) {
        this.generation= qn(prefix,local);
        return this;
    }

    public WasDerivedFromBuilder activity(QualifiedName activity) {
        this.activity=activity;
        return this;
    }
    public WasDerivedFromBuilder activity(String prefix, String local) {
        this.activity= qn(prefix,local);
        return this;
    }
    public WasDerivedFromBuilder activity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("activity cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.activity=qn;
        return this;
    }

    public WasDerivedFromBuilder aka() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasDerivedFrom(id, generatedEntity, usedEntity, activity, generation, usage, attrs));
        return parent;
    }

}
