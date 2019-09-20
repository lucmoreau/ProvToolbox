package org.openprovenance.prov.vanilla;


import org.openprovenance.apache.commons.lang.builder.*;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Value;

import java.util.*;
import java.util.stream.Collectors;


public class WasInfluencedBy implements org.openprovenance.prov.model.WasInfluencedBy, Equals, HashCode, ToString, HasAttributes {

    private final QualifiedName QUALIFIED_NAME_XSD_STRING = ProvFactory.getFactory().getName().XSD_STRING;
    private Optional<QualifiedName> id=Optional.empty();
    private List<org.openprovenance.prov.model.LangString> labels = new LinkedList<>();
    private List<org.openprovenance.prov.model.Other> other = new LinkedList<>();
    private List<org.openprovenance.prov.model.Type> type = new LinkedList<>();
    protected QualifiedName influencee;
    protected QualifiedName influencer;


    final ProvUtilities u=new ProvUtilities();



    private WasInfluencedBy() {}

    public WasInfluencedBy(QualifiedName id,
                           Collection<Attribute> attributes) {
        this.setId(id);
        u.populateAttributes(attributes, labels, new LinkedList<>(), type,new LinkedList<>(),other,null);
    }

    public WasInfluencedBy(QualifiedName id,
                           QualifiedName influencee,
                           QualifiedName influencer,
                           Collection<Attribute> attributes) {
        this.setId(id);
        this.influencee=influencee;
        this.influencer=influencer;
        u.populateAttributes(attributes, labels, new LinkedList<>(), type,new LinkedList<>(),other,null);
    }



    @Override
    public void setInfluencee(QualifiedName influencee) {
        this.influencee=influencee;
    }

    @Override
    public void setInfluencer(QualifiedName influencer) {
        this.influencer=influencer;
    }

    @Override
    public QualifiedName getInfluencer() {
        return influencer;
    }

    @Override
    public QualifiedName getInfluencee() {
        return influencee;
    }


    @Override
    public QualifiedName getId() {
        return id.orElse(null);
    }


    @Override
    public Kind getKind() {
        return Kind.PROV_INFLUENCE;
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
        if (!(object instanceof WasInfluencedBy)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final WasInfluencedBy that = ((WasInfluencedBy) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getInfluencee(), that.getInfluencee());
        equalsBuilder.append(this.getInfluencer(), that.getInfluencer());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof WasInfluencedBy)) {
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
        hashCodeBuilder.append(this.getInfluencee());
        hashCodeBuilder.append(this.getInfluencer());
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
            QualifiedName theInfluencee;
            theInfluencee = this.getInfluencee();
            toStringBuilder.append("influencee", theInfluencee);
        }

        {
            QualifiedName theInfluencer;
            theInfluencer = this.getInfluencer();
            toStringBuilder.append("influencer", theInfluencer);
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
