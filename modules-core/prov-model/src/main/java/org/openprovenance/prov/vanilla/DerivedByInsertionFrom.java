package org.openprovenance.prov.vanilla;

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

public class DerivedByInsertionFrom implements org.openprovenance.prov.model.DerivedByInsertionFrom, Identifiable, HasType, HasLabel, HasOther {
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
}
