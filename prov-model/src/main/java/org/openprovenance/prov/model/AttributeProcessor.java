package org.openprovenance.prov.model;
import java.util.List;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import java.util.Hashtable;
import javax.xml.bind.JAXBElement;



public class AttributeProcessor {

    final private Hashtable<String,Hashtable<String,List<Attribute>>> namespaceIndex=
        new Hashtable<String,Hashtable<String,List<Attribute>>>();
    
    
    final private List<Attribute> attributes;

    public AttributeProcessor(List<Attribute> attributes) {
        this.attributes=attributes;
    }
              
    /* Processing of attributes */
    
    public Hashtable<String,List<Attribute>> attributesWithNamespace(String namespace) {


        Hashtable<String,List<Attribute>> result=namespaceIndex.get(namespace);
        if (result==null) {
            result=new Hashtable<String,List<Attribute>>();
            namespaceIndex.put(namespace,result);
        }

        for (Attribute attribute: attributes) {
            
            QName name=attribute.getElementName();
            if (namespace.equals(name.getNamespaceURI())) {
                List<Attribute> ll=result.get(name.getLocalPart());
                if (ll==null) {
                    List<Attribute> tmp=new LinkedList<Attribute>();
                    tmp.add(attribute);
                    result.put(name.getLocalPart(),tmp);
                } else {
                    ll.add(attribute);                        
                }
            }
        }
        return result;
    }
}
