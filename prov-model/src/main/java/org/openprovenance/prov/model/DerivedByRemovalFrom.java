package org.openprovenance.prov.model;

import java.util.List;

public interface DerivedByRemovalFrom extends Identifiable, HasType, HasLabel, Influence {

    void setNewDictionary(IDRef after);

    void setOldDictionary(IDRef before);

    List<Key> getKey();

    IDRef getNewDictionary();

    IDRef getOldDictionary();

}
