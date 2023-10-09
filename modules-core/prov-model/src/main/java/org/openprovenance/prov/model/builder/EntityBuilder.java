package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

public class EntityBuilder extends GenericBuilder<EntityBuilder>{

    public EntityBuilder(Builder builder, ModelConstructor mc, ProvFactory pf) {
        super(builder,mc,pf);

    }
    public Builder build() {
        parent.statements.add(mc.newEntity(id,attrs));
        return parent;
    }

    public EntityBuilder value(QualifiedName qn) {
        return attr(pf.newAttribute(provValue,qn,pf.getName().PROV_QUALIFIED_NAME));
    }

    public EntityBuilder value(String str) {
        return attr(pf.newAttribute(provValue,str,pf.getName().XSD_STRING));
    }

    public EntityBuilder aka() {
        parent.knownAs.put(id.getLocalPart(),id);
        return this;
    }

}