package org.openprovenance.prov.core;


import java.util.*;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.QualifiedName;

public class ProvUtilities extends org.openprovenance.prov.model.ProvUtilities {


    private static final QualifiedName QualifiedName_PROV_TYPE = ProvFactory.getFactory().getName().PROV_TYPE;
    private static final QualifiedName QualifiedName_PROV_LABEL = ProvFactory.getFactory().getName().PROV_LABEL;
    private static final QualifiedName QualifiedName_PROV_VALUE = ProvFactory.getFactory().getName().PROV_VALUE;
    private static final QualifiedName QualifiedName_PROV_LOCATION = ProvFactory.getFactory().getName().PROV_LOCATION;
    private static final QualifiedName QualifiedName_PROV_ROLE = ProvFactory.getFactory().getName().PROV_ROLE;


    Map<QualifiedName, Set<Attribute>> split(Collection<Attribute> attributes) {
        Collection<Label> labels=new LinkedList<>();
        Collection<Type> types=new LinkedList<>();
        Collection<Value> values=new LinkedList<>();
        Collection<Location> locations=new LinkedList<>();
        Collection<Role> roles=new LinkedList<>();
        Map<QualifiedName,Collection<Other>> others=new HashMap<>();
        split(attributes,labels,types,values,locations,roles,others);

        Map<QualifiedName, Set<Attribute>> result=new HashMap<>();
        result.put(QualifiedName_PROV_LABEL,new HashSet<>(labels));
        result.put(QualifiedName_PROV_TYPE,new HashSet<>(types));
        result.put(QualifiedName_PROV_VALUE,new HashSet<>(values));
        result.put(QualifiedName_PROV_LOCATION,new HashSet<>(locations));
        result.put(QualifiedName_PROV_ROLE,new HashSet<>(roles));
        for (Map.Entry<QualifiedName, Collection<Other>> entry: others.entrySet()) {
            result.put(entry.getKey(),new HashSet<>(entry.getValue()));
        }
        return result;
    }

    Map<QualifiedName, Attribute[]> split2(Collection<Attribute> attributes) {
        Map<QualifiedName, Attribute[]> result=new HashMap<>();
        Map<QualifiedName, Set<Attribute>> m=split(attributes);
        for (Map.Entry<QualifiedName, Set<Attribute>> entry: m.entrySet()) {
            result.put(entry.getKey(),entry.getValue().toArray(new Attribute[0]));
        }
        return result;
    }


        void split(Collection<Attribute> attributes,
               Collection<Label> labels,
               Collection<Type> types,
               Collection<Value> values,
               Collection<Location> locations,
               Collection<Role> roles,
               Map<QualifiedName,Collection<Other>> others) {
        for (Attribute attribute: attributes) {
            switch (attribute.getKind()) {
                case PROV_TYPE:
                    types.add((Type) attribute);
                    break;
                case PROV_LABEL:
                    labels.add((Label)attribute);
                    break;
                case PROV_ROLE:
                    roles.add((Role)attribute);
                    break;
                case PROV_LOCATION:
                    locations.add((Location)attribute);
                    break;
                case PROV_VALUE:
                    values.add((Value)attribute);
                    break;
                case PROV_KEY:
                    /* Ignore */
                    break;
                case OTHER:
                    Other other=(Other) attribute;
                    QualifiedName name=other.getElementName();
                    Collection<Other> some=others.get(name);
                    if (some==null) {
                        some=new LinkedList<>();
                        others.put(name,some);
                    }
                    some.add(other);
                    break;
            }

        }
    }


}
