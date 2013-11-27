package org.openprovenance.prov.model;

import java.util.List;

public interface HadMember extends Relation0 {

    void setCollection(QualifiedName collection);

    List<QualifiedName> getEntity();

    QualifiedName getCollection();
    
}
