package org.openprovenance.prov.model;

import java.util.List;

public interface DerivedByInsertionFrom extends Identifiable, HasOtherAttribute, HasType, HasLabel, Influence {

    void setNewDictionary(IDRef after);

    void setOldDictionary(IDRef before);

    List<Entry> getKeyEntityPair();

    IDRef getNewDictionary();

    IDRef getOldDictionary();

}
