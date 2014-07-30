/**
 * Provides the classes instantiating the PROV Data Model in Java with a view to persist it with an ORM mapping.
 * 
 */

@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters({
        @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(AnyAdapter.class),
        @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(IDRefAdapter.class)
            })
@javax.xml.bind.annotation.XmlSchema(namespace = "http://www.w3.org/ns/prov#", 
                                     elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)

package org.openprovenance.prov.sql;
