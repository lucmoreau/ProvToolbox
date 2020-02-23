package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Value;

import java.util.*;
import java.util.stream.Collectors;
import static org.openprovenance.prov.vanilla.ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING;

public class QualifiedAlternateOf implements org.openprovenance.prov.model.extension.QualifiedAlternateOf, Equals, HashCode, ToString, HasAttributes {

    private Optional<QualifiedName> id=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName alternate1;
    protected QualifiedName alternate2;


    final ProvUtilities u=new ProvUtilities();

    @Override
    public boolean isUnqualified() {
        return id.isEmpty() && other.isEmpty() && labels.isEmpty() && type.isEmpty();
    }


    private QualifiedAlternateOf() {}

    public QualifiedAlternateOf(QualifiedName id,
                                Collection<Attribute> attributes) {
        this.setId(id);
        u.populateAttributes(attributes, labels, new LinkedList<>(), type,new LinkedList<>(),other,null);
    }

    public QualifiedAlternateOf(QualifiedName id,
                                QualifiedName alternate1,
                                QualifiedName alternate2,
                                Collection<Attribute> attributes) {
        this.setId(id);
        this.alternate1 =alternate1;
        this.alternate2 =alternate2;
        u.populateAttributes(attributes, labels, new LinkedList<>(), type,new LinkedList<>(),other,null);
    }



    @Override
    public void setAlternate1(QualifiedName informed) {
        this.alternate1 =informed;
    }

    @Override
    public void setAlternate2(QualifiedName alternate2) {
        this.alternate2 =alternate2;
    }

    @Override
    public QualifiedName getAlternate2() {
        return alternate2;
    }

    @Override
    public QualifiedName getAlternate1() {
        return alternate1;
    }



    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @Override
    public Kind getKind() {
        return Kind.PROV_ALTERNATE;
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
        if (!(object instanceof QualifiedAlternateOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final QualifiedAlternateOf that = ((QualifiedAlternateOf) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getAlternate1(), that.getAlternate1());
        equalsBuilder.append(this.getAlternate2(), that.getAlternate2());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public void equals2(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof QualifiedAlternateOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final QualifiedAlternateOf that = ((QualifiedAlternateOf) object);
      //  equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getAlternate1(), that.getAlternate1());
        equalsBuilder.append(this.getAlternate2(), that.getAlternate2());
      //  equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof QualifiedAlternateOf)) {
            if (object instanceof AlternateOf) {
                if (this.isUnqualified()) {
                    AlternateOf alt=(AlternateOf) object;
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
        hashCodeBuilder.append(this.getAlternate1());
        hashCodeBuilder.append(this.getAlternate2());
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
            QualifiedName theInformed;
            theInformed = this.getAlternate1();
            toStringBuilder.append("alternate1", theInformed);
        }

        {
            QualifiedName theInformant;
            theInformant = this.getAlternate2();
            toStringBuilder.append("alternate2", theInformant);
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
