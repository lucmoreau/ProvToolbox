package org.openprovenance.prov.template.types;


import org.openprovenance.prov.model.QualifiedName;

import java.util.Map;
import java.util.Set;

public class TypesRecordProcessor  {
    public void process(String methodName, Object [] args) {
        System.out.println(methodName);
        System.out.println(args);
    }

    public void end(Map<QualifiedName, Set<String>> knownTypeMap, Map<QualifiedName, Set<String>> unknownTypeMap) {
        System.out.println("Known Type Map");
        knownTypeMap.forEach((k,v) -> {if (k!=null) System.out.println(k.getPrefix()+":"+ k.getLocalPart()+ " -> " + v);});
        System.out.println("Unknown Type Map");
        unknownTypeMap.forEach((k,v) -> {if (k!=null) System.out.println(k.getPrefix()+":"+ k.getLocalPart() + " -> " + v);});
    }
}
