package org.openprovenance.prov.model;

public interface MentionOf extends Relation0 {

    void setSpecificEntity(IDRef infra);

    void setBundle(IDRef bundle);

    void setGeneralEntity(IDRef supra);

    IDRef getSpecificEntity();

    IDRef getBundle();

    IDRef getGeneralEntity();

}
