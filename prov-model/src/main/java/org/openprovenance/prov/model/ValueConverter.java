package org.openprovenance.prov.model;

import javax.xml.namespace.QName;

public interface ValueConverter {

    Object convertToJava(QName type, String value);

}
