package org.openprovenance.prov.xml;
import javax.xml.namespace.QName;

public interface Identifiable {
    public void setId(QName s);
    public QName getId();
} 