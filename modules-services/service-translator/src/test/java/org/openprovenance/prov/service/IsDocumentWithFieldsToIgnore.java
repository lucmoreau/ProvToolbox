package org.openprovenance.prov.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openprovenance.prov.model.Namespace;

public interface IsDocumentWithFieldsToIgnore {

//    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY)
//    @JsonSubTypes({
//            @JsonSubTypes.Type(value = org.openprovenance.prov.core.xml.Document.class,         name = "document")
//
//    })

    @JsonIgnore
    @JsonProperty("@context")
    Namespace getNamespace();

    @JsonIgnore
    org.openprovenance.prov.model.Document getDocument();

    @JsonIgnore
    org.openprovenance.prov.model.QualifiedName getId();

}
