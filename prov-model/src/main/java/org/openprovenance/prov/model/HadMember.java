package org.openprovenance.prov.model;

import java.util.List;

public interface HadMember extends Relation0 {

    void setCollection(IDRef collection);

    List<IDRef> getEntity();

    IDRef getCollection();

}
