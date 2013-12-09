package org.openprovenance.prov.model;

public interface MentionOf extends Relation0 {

    void setSpecificEntity(QualifiedName infra);

    void setBundle(QualifiedName bundle);

    void setGeneralEntity(QualifiedName supra);

    QualifiedName getSpecificEntity();

    QualifiedName getBundle();

    QualifiedName getGeneralEntity();

}
