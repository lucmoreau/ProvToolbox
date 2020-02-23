package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Value;

import java.util.*;
import java.util.stream.Collectors;
import static org.openprovenance.prov.vanilla.ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING;

public class QualifiedSpecializationOf implements org.openprovenance.prov.model.extension.QualifiedSpecializationOf, Equals, HashCode, ToString, HasAttributes {

    private Optional<QualifiedName> id=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName specific;
    protected QualifiedName generalEntity;


    final ProvUtilities u=new ProvUtilities();


    @Override
    public boolean isUnqualified() {
        return id.isEmpty() && other.isEmpty() && labels.isEmpty() && type.isEmpty();
    }

    private QualifiedSpecializationOf() {}


    public QualifiedSpecializationOf(QualifiedName id,
                                     QualifiedName specificEntity,
                                     QualifiedName generalEntity,
                                     Collection<Attribute> attributes) {
        this.setId(id);
        this.specific = specificEntity;
        this.generalEntity = generalEntity;
        u.populateAttributes(attributes, labels, new LinkedList<>(), type,new LinkedList<>(),other,null);
    }



    @Override
    public void setSpecificEntity(QualifiedName specific) {
        this.specific =specific;
    }

    @Override
    public void setGeneralEntity(QualifiedName informant) {
        this.generalEntity =informant;
    }

    @Override
    public QualifiedName getGeneralEntity() {
        return generalEntity;
    }

    @Override
    public QualifiedName getSpecificEntity() {
        return specific;
    }



    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @Override
    public Kind getKind() {
        return Kind.PROV_SPECIALIZATION;
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
        if (!(object instanceof QualifiedSpecializationOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final QualifiedSpecializationOf that = ((QualifiedSpecializationOf) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getSpecificEntity(), that.getSpecificEntity());
        equalsBuilder.append(this.getGeneralEntity(), that.getGeneralEntity());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public void equals2(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof SpecializationOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final SpecializationOf that = ((SpecializationOf) object);
        //equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getSpecificEntity(), that.getSpecificEntity());
        equalsBuilder.append(this.getGeneralEntity(), that.getGeneralEntity());
        //equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }


    public boolean equals(Object object) {
        if (!(object instanceof QualifiedSpecializationOf)) {
            if (object instanceof SpecializationOf) {
                if (this.isUnqualified()) {
                    SpecializationOf qspec=(SpecializationOf) object;
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
        hashCodeBuilder.append(this.getSpecificEntity());
        hashCodeBuilder.append(this.getGeneralEntity());
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
            QualifiedName theSpecificEntity;
            theSpecificEntity = this.getSpecificEntity();
            toStringBuilder.append("specificEntity", theSpecificEntity);
        }

        {
            QualifiedName theGeneralEntity;
            theGeneralEntity = this.getGeneralEntity();
            toStringBuilder.append("generalEntity", theGeneralEntity);
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
