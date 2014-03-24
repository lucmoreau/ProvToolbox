package org.openprovenance.prov.model;
import java.util.List;
import java.util.LinkedList;

import java.util.Hashtable;



public class AttributeProcessor {

    
    final private Hashtable<String,Hashtable<String,List<Other>>> namespaceIndex=
        new Hashtable<String,Hashtable<String,List<Other>>>();
    
    
    final private List<Other> attributes;

    public AttributeProcessor(List<Other> attributes) {
        this.attributes=attributes;
    }
              
    /* Processing of attributes */
    
    public Hashtable<String,List<Other>> attributesWithNamespace(String namespace) {


        Hashtable<String,List<Other>> result=namespaceIndex.get(namespace);
        if (result==null) {
            result=new Hashtable<String,List<Other>>();
            namespaceIndex.put(namespace,result);
        }

        for (Other attribute: attributes) {
            
            QualifiedName name=attribute.getElementName();
            if (namespace.equals(name.getNamespaceURI())) {
                List<Other> ll=result.get(name.getLocalPart());
                if (ll==null) {
                    List<Other> tmp=new LinkedList<Other>();
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
