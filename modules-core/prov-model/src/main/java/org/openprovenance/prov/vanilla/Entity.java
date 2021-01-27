package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.Value;

import java.util.*;
import java.util.stream.Collectors;


public class Entity implements org.openprovenance.prov.model.Entity, Equals, HashCode, ToString, HasAttributes {

    static private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id;

    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Location> location = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    private Optional<org.openprovenance.prov.model.Value> value=Optional.empty();


    static final ProvUtilities u=new ProvUtilities();



    private Entity() {}

    public Entity(QualifiedName id,
                  Collection<Attribute> attributes) {
        this.setId(id);
        location=new LinkedList<>();
        type=new LinkedList<>();
        other=new LinkedList<>();
        Value [] valueHolder=new Value[1];
        valueHolder[0]=null;
        u.populateAttributes(attributes, labels, location, type, new LinkedList<>(), other, valueHolder);
        value=Optional.ofNullable(valueHolder[0]);
    }

    public void setValue(org.openprovenance.prov.model.Value o) {
        this.value=Optional.ofNullable(o);
    }

    public org.openprovenance.prov.model.Value getValue() {
        return value.orElse(null);
    }


    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }

    @Override
    public Kind getKind() {
        return Kind.PROV_ENTITY;
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
    public List<org.openprovenance.prov.model.Location> getLocation() {
        return location;
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
        if (!(object instanceof Entity)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final Entity that = ((Entity) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof Entity)) {
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
        hashCodeBuilder.append(this.getId());
        hashCodeBuilder.append(this.getIndexedAttributes());
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
        if (value.isPresent()) result.add(getValue());
        result.addAll(getType());
        result.addAll(getLocation());
        result.addAll(getOther().stream().map(o -> (Attribute)o).collect(Collectors.toList())); //TODO: collect directly into result
        return result;
    }

    public void setIndexedAttributes (Object qn, Set<Attribute> attributes) {
        List<Value> values_discard=new LinkedList<>();
        List<Role> roles_discard=new LinkedList<>();
        u.distribute((QualifiedName)qn,attributes,getLabel(),values_discard,getLocation(),getType(),roles_discard,getOther());
        if (!values_discard.isEmpty()) value=Optional.of(values_discard.get(0));

    }



    @Override
    public Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        return u.split(getAttributes());
    }


}
