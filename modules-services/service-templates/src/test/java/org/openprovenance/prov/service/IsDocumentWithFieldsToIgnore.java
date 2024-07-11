package org.openprovenance.prov.service;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IsDocumentWithFieldsToIgnore {

//    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY)
//    @JsonSubTypes({
//            @JsonSubTypes.Type(value = org.openprovenance.prov.core.xml.Document.class,         name = "document")
//
//    })

    @JsonIgnore
    org.openprovenance.prov.model.Document getDocument();

    @JsonIgnore
    org.openprovenance.prov.model.QualifiedName getId();

}
