package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.*;
import java.util.stream.Collectors;


public class WasAttributedTo implements org.openprovenance.prov.model.WasAttributedTo, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName agent;
    protected QualifiedName entity;


    final ProvUtilities u=new ProvUtilities();



    private WasAttributedTo() {}

    public WasAttributedTo(QualifiedName id,
                           Collection<Attribute> attributes) {
        this.setId(id);
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type,Collections.EMPTY_LIST,other,null);
    }

    public WasAttributedTo(QualifiedName id,
                           QualifiedName entity,
                           QualifiedName agent,
                           Collection<Attribute> attributes) {
        this.setId(id);
        this.agent=agent;
        this.entity=entity;
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type,Collections.EMPTY_LIST,other,null);
    }



    @Override
    public void setAgent(QualifiedName aid) {
        this.agent=aid;
    }

    @Override
    public void setEntity(QualifiedName eid) {
        this.entity=eid;
    }

    @Override
    public QualifiedName getEntity() {
        return entity;
    }

    @Override
    public QualifiedName getAgent() {
        return agent;
    }



    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @Override
    public Kind getKind() {
        return Kind.PROV_ATTRIBUTION;
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
        if (!(object instanceof WasAttributedTo)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasAttributedTo that = ((WasAttributedTo) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getAgent(), that.getAgent());
        equalsBuilder.append(this.getEntity(), that.getEntity());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasAttributedTo)) {
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
        hashCodeBuilder.append(this.getAgent());
        hashCodeBuilder.append(this.getEntity());
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
            QualifiedName theEntity;
            theEntity = this.getEntity();
            toStringBuilder.append("entity", theEntity);
        }

        {
            QualifiedName theAgent;
            theAgent = this.getAgent();
            toStringBuilder.append("agent", theAgent);
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
        u.distribute((QualifiedName)qn,attributes,getLabel(),Collections.EMPTY_LIST, Collections.EMPTY_LIST,getType(),Collections.EMPTY_LIST, getOther());
    }


    @Override
    public Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        return u.split(getAttributes());
    }

}
