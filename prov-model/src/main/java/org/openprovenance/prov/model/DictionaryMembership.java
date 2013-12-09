package org.openprovenance.prov.model;

import java.util.List;

public interface DictionaryMembership extends Relation0 {

    void setDictionary(QualifiedName dict);

    List<Entry> getKeyEntityPair();

    QualifiedName getDictionary();

}
