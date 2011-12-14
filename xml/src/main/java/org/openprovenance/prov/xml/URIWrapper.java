package org.openprovenance.prov.xml;

import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name = "anyURI", namespace = "http://www.w3.org/2001/XMLSchema", propOrder = {
    "value"
})
public class URIWrapper {
    @XmlJavaTypeAdapter(URIAdapter .class)
    @XmlValue
	public java.net.URI value;

    public java.net.URI getValue() {
        return value;
    }

    public void setValue(java.net.URI  value) {
        this.value = value;
    }

    public String toString() {
        return value.toString();
    }


}
  