/**
 * Provides the classes instantiating the PROV Data Model in Java with a view to serialize/deserialize to/from XML.
 * 
 */
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters({
	@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(AnyAdapter.class),
        @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(QualifiedNameAdapter.class)
	    })
@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.w3.org/ns/prov#", 
elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)

package org.openprovenance.prov.xml;
