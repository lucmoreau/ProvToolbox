package org.openprovenance.prov.core.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Value;

import java.util.*;
import java.util.stream.Collectors;

public class QualifiedHadMember implements org.openprovenance.prov.model.extension.QualifiedHadMember, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName collection;
    protected List<QualifiedName> entity=new LinkedList<>();


    final ProvUtilities u=new ProvUtilities();



    private QualifiedHadMember() {}

    @Override
    public boolean isUnqualified() {
        return id.isEmpty() && other.isEmpty() && labels.isEmpty() && type.isEmpty();
    }


    public QualifiedHadMember(QualifiedName id,
                              QualifiedName collection,
                              Collection<QualifiedName> entity,
                              Collection<Attribute> attributes) {
        this.setId(id);
        this.collection =collection;
        this.entity.addAll(entity);
        u.populateAttributes(attributes, labels, new LinkedList<>(), type,new LinkedList<>(),other,null);
    }



    @Override
    public void setCollection(QualifiedName collection) {
        this.collection =collection;
    }


    @Override
    public List<QualifiedName> getEntity() {
        return entity;
    }

    @Override
    public QualifiedName getCollection() {
        return collection;
    }



    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @Override
    public Kind getKind() {
        return Kind.PROV_MEMBERSHIP;
    }



    @Override
    public void setId(QualifiedName value) {
        id = Optional.ofNullable(value);
    }



    @Override
    public List<org.openprovenance.prov.model.LangString> getLabel() {
        return labels;
    }


    @Override
    public List<org.openprovenance.prov.model.Type> getType() {
        return type;
    }


    @Override
    public List<org.openprovenance.prov.model.Other> getOther() {
        return other;
    }



    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof QualifiedHadMember)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final QualifiedHadMember that = ((QualifiedHadMember) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getCollection(), that.getCollection());
        equalsBuilder.append(this.getEntity(), that.getEntity());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }


    public void equals2(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof HadMember)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final HadMember that = ((HadMember) object);
        //equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getCollection(), that.getCollection());
        equalsBuilder.append(this.getEntity(), that.getEntity());
       // equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }


    public boolean equals(Object object) {
        if (!(object instanceof QualifiedHadMember)) {
            if (object instanceof HadMember) {
                if (this.isUnqualified()) {
                    System.out.println("QualifiedHadMember.equals with HadMember");
                    final EqualsBuilder equalsBuilder2 = new EqualsBuilder();
                    equals2(object, equalsBuilder2);
                    return equalsBuilder2.isEquals();
                }
                return false;
            }
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new EqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        final boolean unqualified=isUnqualified();
        if (!unqualified) {
            hashCodeBuilder.append(this.getId());
        }
        hashCodeBuilder.append(this.getCollection());
        hashCodeBuilder.append(this.getEntity());
        if (!unqualified) {
            hashCodeBuilder.append(this.getIndexedAttributes());
        }
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {


        {
            QualifiedName theId;
            theId = this.getId();
            toStringBuilder.append("id", theId);
        }

        {
            QualifiedName theCollection;
            theCollection = this.getCollection();
            toStringBuilder.append("collection", theCollection);
        }

        {
            List<QualifiedName> theEntity;
            theEntity = this.getEntity();
            toStringBuilder.append("entity", theEntity);
        }

       {
            Map<QualifiedName, Set<Attribute>> theAttributes;
            theAttributes = this.getIndexedAttributes();
            toStringBuilder.append("attributes", theAttributes);
        }



    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    @Override
    public Collection<Attribute> getAttributes() {
        LinkedList<Attribute> result=new LinkedList<>();
        result.addAll(getLabel().stream().map(s -> new Label(QUALIFIED_NAME_XSD_STRING,s)).collect(Collectors.toList()));
        result.addAll(getType());
        result.addAll(getOther().stream().map(o -> (Attribute)o).collect(Collectors.toList())); //TODO: collect directly into result
        return result;
    }

    public void setIndexedAttributes (Object qn, Set<Attribute> attributes) {
        List<Value> values=new LinkedList<>();
        List<org.openprovenance.prov.model.Location> locations=new LinkedList<>();
        u.distribute((QualifiedName)qn,attributes,getLabel(),values, locations,getType(),Collections.EMPTY_LIST, getOther());
    }


    @Override
    public Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        return u.split(getAttributes());
    }

}
