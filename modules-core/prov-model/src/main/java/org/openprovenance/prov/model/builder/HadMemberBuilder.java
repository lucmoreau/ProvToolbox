package org.openprovenance.prov.model.builder;

import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ModelConstructorExtension;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;

import java.util.LinkedList;
import java.util.List;

public class HadMemberBuilder extends GenericBuilder<HadMemberBuilder>{
    protected final ModelConstructorExtension mce;
    protected QualifiedName collection;
    protected List<QualifiedName> entity=new LinkedList<>();

    public HadMemberBuilder(Builder builder, ModelConstructor mc, ModelConstructorExtension mce, ProvFactory pf) {
        super(builder,mc,pf);
        this.mce=mce;
    }
    public HadMemberBuilder collection(QualifiedName collection) {
        this.collection=collection;
        return this;
    }
    public HadMemberBuilder collection(String prefix, String local) {
        this.collection = qn(prefix,local);
        return this;
    }
    public HadMemberBuilder collection(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("collection cannot find knownAs " + knownAs + " in " + parent.knownAs.keySet());
        this.collection =qn;
        return this;
    }
    public HadMemberBuilder entity(QualifiedName entity) {
        this.entity.add(entity);
        return this;
    }
    public HadMemberBuilder entity(String knownAs) {
        QualifiedName qn=parent.knownAs.get(knownAs);
        if (qn==null) throw new NullPointerException("generalEntity cannot find knownAs " + knownAs + " in " + parent.knownAs);
        this.entity.add(qn);
        return this;
    }
    public HadMemberBuilder entity(String prefix, String local) {
        this.entity.add(qn(prefix,local));
        return this;
    }

    public Builder build() {
        if ((id==null)&&attrs.isEmpty()) {
            parent.statements.add(mc.newHadMember(collection, entity));
        } else {
            parent.statements.add(mce.newQualifiedHadMember(id, collection, entity, attrs));
        }
        return parent;
    }
    public Builder build(boolean oneAtTheTime) {
        if ((id==null)&&attrs.isEmpty()) {
            if (oneAtTheTime) {
                for (QualifiedName e: entity) {
                    parent.statements.add(mc.newHadMember(collection, List.of(e)));
                }
            } else {
                parent.statements.add(mc.newHadMember(collection, entity));
            }
        } else {
            if (oneAtTheTime) {
                for (QualifiedName e: entity) {
                    parent.statements.add(mce.newQualifiedHadMember(id, collection, List.of(e), attrs));
                }
            } else {
                parent.statements.add(mce.newQualifiedHadMember(id, collection, entity, attrs));
            }
        }
        return parent;
    }

}
