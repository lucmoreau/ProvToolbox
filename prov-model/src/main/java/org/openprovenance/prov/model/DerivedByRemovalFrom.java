package org.openprovenance.prov.model;

import java.util.List;

public interface DerivedByRemovalFrom extends Identifiable, HasExtensibility, Influence {

    void setNewDictionary(IDRef after);

    void setOldDictionary(IDRef before);

    List<Object> getKey();

    IDRef getNewDictionary();

    IDRef getOldDictionary();

}
