package org.openprovenance.prov.model;

import java.util.List;

public interface DictionaryMembership extends Relation0 {

    void setDictionary(IDRef dict);

    List<Entry> getKeyEntityPair();

    IDRef getDictionary();

}
