package org.openprovenance.prov.core.xml;

import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.openprovenance.prov.core.xml.serialization.deserial.CustomQualifiedNameDeserializer;
import org.openprovenance.prov.core.xml.serialization.serial.CustomQualifiedNameSerializer;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.QualifiedName;

public class QualifiedNameRef {

    private  QualifiedName ref;

    public QualifiedNameRef(QualifiedName qn) {
        this.ref =qn;
    }

    public QualifiedNameRef(){}

    public void setRef(QualifiedName qn) {
        this.ref = qn;
    }

    @JacksonXmlProperty(isAttribute = true, localName = "ref", namespace = NamespacePrefixMapper.PROV_NS)
    @JsonDeserialize(using = CustomQualifiedNameDeserializer.class)
    @JsonSerialize(using = CustomQualifiedNameSerializer.class)
    public QualifiedName getRef() {
        return ref;
    }
}
