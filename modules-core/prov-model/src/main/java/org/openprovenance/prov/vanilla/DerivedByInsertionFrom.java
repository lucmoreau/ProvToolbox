package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.Equals;
import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCode;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
import org.openprovenance.apache.commons.lang.builder.ToString;
import org.openprovenance.apache.commons.lang.builder.ToStringBuilder;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.Entry;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Type;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class DerivedByInsertionFrom implements org.openprovenance.prov.model.DerivedByInsertionFrom, Identifiable, HasType, HasLabel, HasOther, Equals, HashCode, ToString {
    private final List<Entry> keyEntityPair;
    private QualifiedName newDictionary;
    private QualifiedName oldDictionary;
    private QualifiedName id;
    final ProvUtilities u=new ProvUtilities();

    private final List<LangString> labels=new LinkedList<>();
    private final List<Type> type=new LinkedList<>();
    private final List<Other> other= new LinkedList<>();

    public DerivedByInsertionFrom(QualifiedName id, QualifiedName after, QualifiedName before, List<Entry> keyEntitySet, Collection<Attribute> attributes) {
        this.id=id;
        this.newDictionary=after;
        this.oldDictionary=before;
        this.keyEntityPair = Objects.requireNonNullElseGet(keyEntitySet, LinkedList::new);
        u.populateAttributes(attributes, labels, new LinkedList<>(), type, new LinkedList<>(),other,null);
    }

    @Override
    public void setNewDictionary(QualifiedName newDictionary) {
        this.newDictionary=newDictionary;

    }

    @Override
    public void setOldDictionary(QualifiedName oldDictionary) {
        this.oldDictionary=oldDictionary;
    }

    @Override
    public List<Entry> getKeyEntityPair() {
        return keyEntityPair;
    }

    @Override
    public QualifiedName getNewDictionary() {
        return newDictionary;
    }

    @Override
    public QualifiedName getOldDictionary() {
        return oldDictionary;
    }

    @Override
    public List<LangString> getLabel() {
        return labels;
    }

    @Override
    public List<Other> getOther() {
        return other;
    }

    @Override
    public List<Type> getType() {
        return type;
    }

    @Override
    public QualifiedName getId() {
        return id;
    }

    @Override
    public void setId(QualifiedName id) {
        this.id=id;
    }

    @Override
    public Kind getKind() {
        return Kind.PROV_DICTIONARY_INSERTION;
    }

    /** The attributes (labels, types, others), indexed by name, as the other statements compare them. */
    Map<QualifiedName, Set<Attribute>> getIndexedAttributes() {
        LinkedList<Attribute> attributes = new LinkedList<>();
        for (LangString label : getLabel()) attributes.add(new Label(ActedOnBehalfOf.QUALIFIED_NAME_XSD_STRING, label));
        attributes.addAll(getType());
        attributes.addAll(getOther());
        return new ProvUtilities().split(attributes);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof DerivedByInsertionFrom)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final DerivedByInsertionFrom that = ((DerivedByInsertionFrom) object);
        equalsBuilder.append(this.getId(), that.getId());
        equalsBuilder.append(this.getNewDictionary(), that.getNewDictionary());
        equalsBuilder.append(this.getOldDictionary(), that.getOldDictionary());
        equalsBuilder.append(this.getKeyEntityPair(), that.getKeyEntityPair());
        equalsBuilder.append(this.getIndexedAttributes(), that.getIndexedAttributes());
    }

    public boolean equals(Object object) {
        if (!(object instanceof DerivedByInsertionFrom)) {
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
        hashCodeBuilder.append(this.getNewDictionary());
        hashCodeBuilder.append(this.getOldDictionary());
        hashCodeBuilder.append(this.getKeyEntityPair());
        hashCodeBuilder.append(this.getIndexedAttributes());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        toStringBuilder.append("id", this.getId());
        toStringBuilder.append("newDictionary", this.getNewDictionary());
        toStringBuilder.append("oldDictionary", this.getOldDictionary());
        toStringBuilder.append("keyEntityPair", this.getKeyEntityPair());
        toStringBuilder.append("attributes", this.getIndexedAttributes());
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }
}
