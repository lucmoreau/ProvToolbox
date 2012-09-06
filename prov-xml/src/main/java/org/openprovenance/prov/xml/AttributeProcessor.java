package org.openprovenance.prov.xml;
import java.util.List;
import java.util.LinkedList;

import javax.xml.namespace.QName;
import java.util.Hashtable;
import javax.xml.bind.JAXBElement;

public class AttributeProcessor {

    final private Hashtable<String,Hashtable<String,List<Object>>> namespaceIndex=
        new Hashtable<String,Hashtable<String,List<Object>>>();
    
    
    final private List<Object> attributes;

    public AttributeProcessor(List<Object> attributes) {
        this.attributes=attributes;
    }
                                                     

    /* Processing of attributes */
    
    Hashtable<String,List<Object>> attributesWithNamespace(String namespace) {


        Hashtable<String,List<Object>> result=namespaceIndex.get(namespace);
        if (result==null) {
            result=new Hashtable<String,List<Object>>();
            namespaceIndex.put(namespace,result);
        }

        for (Object attribute: attributes) {
            if (attribute instanceof JAXBElement) {
                JAXBElement je=(JAXBElement) attribute;
                QName name=je.getName();
                if (namespace.equals(name.getNamespaceURI())) {
                    List<Object> ll=result.get(name.getLocalPart());
                    if (ll==null) {
                        List<Object> tmp=new LinkedList<Object>();
                        tmp.add(je.getValue());
                        result.put(name.getLocalPart(),tmp);
                    } else {
                        ll.add(je.getValue());                        
                    }
                }
            }
        }
        return result;
    }
}