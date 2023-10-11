package org.openprovenance.prov.vanilla;

import org.openprovenance.apache.commons.lang.builder.EqualsBuilder;
import org.openprovenance.apache.commons.lang.builder.HashCodeBuilder;
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
import java.util.Objects;


public class DictionaryMembership implements org.openprovenance.prov.model.DictionaryMembership, Identifiable, HasType, HasLabel, HasOther {
    private QualifiedName id;
    private final List<LangString> labels=new LinkedList<>();
    private final List<Type> type=new LinkedList<>();
    private final List<Other> other= new LinkedList<>();

    private QualifiedName dictionary;
    private final List<Entry> keyEntityPair;
    final ProvUtilities u=new ProvUtilities();


    public DictionaryMembership(QualifiedName id, QualifiedName dictionary, List<Entry> keyEntityPair, Collection<Attribute> attributes) {
        this.id=id;
        this.dictionary=dictionary;
        this.keyEntityPair = Objects.requireNonNullElseGet(keyEntityPair, LinkedList::new);
        u.populateAttributes(attributes, labels, new LinkedList<>(), type, new LinkedList<>(),other,null);

    }

    @Override
    public QualifiedName getDictionary() {
        return dictionary;
    }

    @Override
    public List<Entry> getKeyEntityPair() {
        return keyEntityPair;
    }

    @Override
    public void setDictionary(QualifiedName dictionary) {
        this.dictionary=dictionary;
    }

    @Override
    public Kind getKind() {
        return Kind.PROV_DICTIONARY_MEMBERSHIP;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof DictionaryMembership)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final DictionaryMembership that = ((DictionaryMembership) object);
        equalsBuilder.append(this.getDictionary(), that.getDictionary());
        equalsBuilder.append(this.getKeyEntityPair(), that.getKeyEntityPair());
    }

    public boolean equals(Object object) {
        if (!(object instanceof DictionaryMembership)) {
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
        hashCodeBuilder.append(this.getDictionary());
        hashCodeBuilder.append(this.getKeyEntityPair());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {

        {
            QualifiedName theId;
            theId = this.getDictionary();
            toStringBuilder.append("dictionary", theId);
            toStringBuilder.append("entries", this.getKeyEntityPair());
        }
    }

    @Override
    public String toString() {
        final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
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
}
