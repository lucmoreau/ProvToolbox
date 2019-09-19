/**
 * 
 */
/**
 * @author lavm
 *
 */

@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters({
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openprovenance.prov.xml.AnyAdapter.class),
        @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(org.openprovenance.prov.xml.QualifiedNameAdapter.class)
        })
@javax.xml.bind.annotation.XmlSchema(namespace = "http://openprovenance.org/prov/extension#", 
elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
package org.openprovenance.prov.xml.extension;
