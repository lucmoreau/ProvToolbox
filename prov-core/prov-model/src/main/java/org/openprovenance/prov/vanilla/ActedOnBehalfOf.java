package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;

import java.util.*;
import java.util.stream.Collectors;


public class ActedOnBehalfOf implements org.openprovenance.prov.model.ActedOnBehalfOf, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName delegate;
    protected QualifiedName responsible;
    private Optional<QualifiedName> activity=Optional.empty();


    final ProvUtilities u=new ProvUtilities();



    private ActedOnBehalfOf() {}

    public ActedOnBehalfOf(QualifiedName id,
                           Collection<Attribute> attributes) {
        this.setId(id);
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type,Collections.EMPTY_LIST, other,null);
    }

    public ActedOnBehalfOf(QualifiedName id,
                           QualifiedName delegate,
                           QualifiedName responsible,
                           QualifiedName activity,
                           Collection<Attribute> attributes) {
        this.setId(id);
        this.delegate=delegate;
        this.responsible=responsible;
        this.activity=Optional.ofNullable(activity);
        u.populateAttributes(attributes, labels, Collections.EMPTY_LIST, type,Collections.EMPTY_LIST, other,null);
    }



    @Override
    public void setDelegate(QualifiedName aid) {
        this.delegate=aid;
    }

    @Override
    public void setResponsible(QualifiedName aid) {
        this.responsible=aid;
    }

    @Override
    public void setActivity(QualifiedName eid) {
        this.activity=Optional.ofNullable(eid);
    }

    @Override
    public QualifiedName getDelegate() {
        return delegate;
    }

    @Override
    public QualifiedName getResponsible() {
        return responsible;
    }


    @Override
    public QualifiedName getActivity() {
        return activity.orElse(null);
    }

    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @Override
    public Kind getKind() {
        return Kind.PROV_DELEGATION;
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
        if (!(object instanceof ActedOnBehalfOf)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final ActedOnBehalfOf that = ((ActedOnBehalfOf) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getActivity(), that.getActivity());
        equalsBuilder.append(this.getDelegate(), that.getDelegate());
        equalsBuilder.append(this.getResponsible(), that.getResponsible());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ActedOnBehalfOf)) {
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
        hashCodeBuilder.append(this.getResponsible());
        hashCodeBuilder.append(this.getDelegate());
        hashCodeBuilder.append(this.getActivity());
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
            QualifiedName theDelegate;
            theDelegate = this.getDelegate();
            toStringBuilder.append("delegate", theDelegate);
        }

        {
            QualifiedName theResponisble;
            theResponisble = this.getResponsible();
            toStringBuilder.append("responsible", theResponisble);
        }

        {
            QualifiedName theActivity;
            theActivity = this.getActivity();
            toStringBuilder.append("activity", theActivity);
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
