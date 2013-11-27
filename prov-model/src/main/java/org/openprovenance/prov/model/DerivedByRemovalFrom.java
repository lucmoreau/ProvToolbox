package org.openprovenance.prov.model;

import java.util.List;

public interface DerivedByRemovalFrom extends Identifiable, HasType, HasLabel, Influence {

    void setNewDictionary(QualifiedName after);

    void setOldDictionary(QualifiedName before);

    List<Key> getKey();

    QualifiedName getNewDictionary();

    QualifiedName getOldDictionary();

}
