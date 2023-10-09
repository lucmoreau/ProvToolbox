package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class WasInformedByBuilder extends GenericBuilder<WasInformedByBuilder>{
    protected QualifiedName informed;
    protected QualifiedName informant;

    public WasInformedByBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);
    }

    public WasInformedByBuilder informed(QualifiedName informed) {
        this.informed=informed;
        return this;
    }
    public WasInformedByBuilder informed(String prefix, String local) {
        this.informed=qn(prefix,local);
        return this;
    }
    public WasInformedByBuilder informed(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("informed cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.informed =qn;
        return this;
    }


    public WasInformedByBuilder informant(QualifiedName informant) {
        this.informant=informant;
        return this;
    }
    public WasInformedByBuilder informant(String prefix, String local) {
        this.informant=qn(prefix,local);
        return this;
    }
    public WasInformedByBuilder informant(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("informant: cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.informant =qn;
        return this;
    }

    public WasInformedByBuilder role(QualifiedName e) {
        attr(provRole,e,pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public WasInformedByBuilder role(String prefix, String local) {
        attr(provRole, qn(prefix,local),pf.getName().PROV_QUALIFIED_NAME);
        return this;
    }
    public WasInformedByBuilder role(String role) {
        attr(provRole,role,pf.getName().XSD_STRING);
        return this;
    }

    public Builder build() {
        parent.statements.add(mc.newWasInformedBy(id, informed, informant, attrs));
        return parent;
    }



}
