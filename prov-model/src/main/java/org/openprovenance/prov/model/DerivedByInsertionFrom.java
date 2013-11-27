package org.openprovenance.prov.model;

import java.util.List;

public interface DerivedByInsertionFrom extends Identifiable, HasOther, HasType, HasLabel, Influence {

    void setNewDictionary(QualifiedName after);

    void setOldDictionary(QualifiedName before);

    List<Entry> getKeyEntityPair();

    QualifiedName getNewDictionary();

    QualifiedName getOldDictionary();

}
