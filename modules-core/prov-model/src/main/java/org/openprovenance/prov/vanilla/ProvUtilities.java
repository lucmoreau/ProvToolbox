package org.openprovenance.prov.vanilla;


import java.util.*;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.QualifiedName;

public class ProvUtilities extends org.openprovenance.prov.model.ProvUtilities {


    private static final QualifiedName QualifiedName_PROV_TYPE = ProvFactory.getFactory().getName().PROV_TYPE;
    private static final QualifiedName QualifiedName_PROV_LABEL = ProvFactory.getFactory().getName().PROV_LABEL;
    private static final QualifiedName QualifiedName_PROV_VALUE = ProvFactory.getFactory().getName().PROV_VALUE;
    private static final QualifiedName QualifiedName_PROV_LOCATION = ProvFactory.getFactory().getName().PROV_LOCATION;
    private static final QualifiedName QualifiedName_PROV_ROLE = ProvFactory.getFactory().getName().PROV_ROLE;

    public static final String PROV_LABEL_URI = QualifiedName_PROV_LABEL.getUri();
    public static final String PROV_TYPE_URI = QualifiedName_PROV_TYPE.getUri();
    public static final String PROV_LOCATION_URI = QualifiedName_PROV_LOCATION.getUri();
    public static final String PROV_ROLE_URI = QualifiedName_PROV_ROLE.getUri();
    public static final String PROV_VALUE_URI = QualifiedName_PROV_VALUE.getUri();



    Map<QualifiedName, Set<Attribute>> split(Collection<Attribute> attributes) {
        Collection<Label> labels=new LinkedList<>();
        Collection<org.openprovenance.prov.vanilla.Type> types=new LinkedList<>();
        Collection<Value> values=new LinkedList<>();
        Collection<org.openprovenance.prov.vanilla.Location> locations=new LinkedList<>();
        Collection<Role> roles=new LinkedList<>();
        Map<QualifiedName,Collection<Other>> others=new HashMap<>();
        split(attributes,labels,types,values,locations,roles,others);

        Map<QualifiedName, Set<Attribute>> result=new HashMap<>();
        if (!labels.isEmpty()) result.put(QualifiedName_PROV_LABEL,new HashSet<>(labels));
        if (!types.isEmpty()) result.put(QualifiedName_PROV_TYPE,new HashSet<>(types));
        if (!values.isEmpty()) result.put(QualifiedName_PROV_VALUE,new HashSet<>(values));
        if (!locations.isEmpty()) result.put(QualifiedName_PROV_LOCATION,new HashSet<>(locations));
        if (!roles.isEmpty()) result.put(QualifiedName_PROV_ROLE,new HashSet<>(roles));
        for (Map.Entry<QualifiedName, Collection<Other>> entry: others.entrySet()) {
            result.put(entry.getKey(),new HashSet<>(entry.getValue()));
        }
        return result;
    }


    void split(Collection<Attribute> attributes,
               Collection<Label> labels,
               Collection<org.openprovenance.prov.vanilla.Type> types,
               Collection<Value> values,
               Collection<org.openprovenance.prov.vanilla.Location> locations,
               Collection<Role> roles,
               Map<QualifiedName,Collection<Other>> others) {
        for (Attribute attribute: attributes) {
            switch (attribute.getKind()) {
                case PROV_TYPE:
                    types.add((org.openprovenance.prov.vanilla.Type) attribute);
                    break;
                case PROV_LABEL:
                    labels.add((Label)attribute);
                    break;
                case PROV_ROLE:
                    roles.add((Role)attribute);
                    break;
                case PROV_LOCATION:
                    locations.add((org.openprovenance.prov.vanilla.Location)attribute);
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
                        some = new LinkedList<>();
                        others.put(name,some);
                    }
                    some.add(other);
                    break;
            }

        }
    }


//TODO: these URI need to be constants in the Constants interface, also need to have proper dispatch
    public void distribute(QualifiedName qn,
                           Collection<Attribute> attributes,
                           Collection<LangString> labels,
                           Collection<org.openprovenance.prov.model.Value> values,
                           Collection<org.openprovenance.prov.model.Location> locations,
                           Collection<org.openprovenance.prov.model.Type> types,
                           Collection<org.openprovenance.prov.model.Role> roles,
                           Collection<org.openprovenance.prov.model.Other> others) {
        String uri=qn.getUri();
        if (PROV_LABEL_URI.equals(uri)) {
            for (Attribute attr: attributes) {
                LangString ls= (LangString) attr.getValue();
                labels.add(ls);
            }
            return;
        }
        if (PROV_TYPE_URI.equals(uri)) {
            for (Attribute attr: attributes) {
                types.add((org.openprovenance.prov.model.Type)attr);
            }
            return;
        }
        if (PROV_LOCATION_URI.equals(uri)) {
            for (Attribute attr: attributes) {
                locations.add((org.openprovenance.prov.model.Location)attr);
            }
            return;
        }
        if (PROV_VALUE_URI.equals(uri)) {
            for (Attribute attr: attributes) {
                values.add((org.openprovenance.prov.model.Value)attr);
            }
            return;
        }
        if (PROV_ROLE_URI.equals(uri)) {
            for (Attribute attr: attributes) {
                roles.add((org.openprovenance.prov.model.Role)attr);
            }
            return;
        }
        for (Attribute attr: attributes) {
            others.add((org.openprovenance.prov.model.Other) attr);
        }


    }

    void populateAttributes(Collection<Attribute> attributes,
                            List<org.openprovenance.prov.model.LangString> label,
                            List<org.openprovenance.prov.model.Location> location,
                            List<org.openprovenance.prov.model.Type> type,
                            List<org.openprovenance.prov.model.Role> role,
                            List<org.openprovenance.prov.model.Other> other,
                            org.openprovenance.prov.model.Value [] value) {

        boolean foundValue = false;
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                switch (attribute.getKind()) {
                    case PROV_TYPE:
                        type.add((org.openprovenance.prov.model.Type) attribute);
                        break;
                    case PROV_LABEL:
                        label.add((LangString) ((org.openprovenance.prov.model.Label) attribute).getValue());
                        break;
                    case PROV_ROLE:
                        role.add((org.openprovenance.prov.model.Role) attribute);
                        break;
                    case PROV_LOCATION:
                        location.add((org.openprovenance.prov.model.Location) attribute);
                        break;
                    case PROV_VALUE:
                        if (!foundValue) {
                            foundValue = true;
                            value[0] = (org.openprovenance.prov.model.Value) attribute;
                        }
                        break;
                    case PROV_KEY:
                        /* Ignore */
                        break;
                    case OTHER:
                        other.add((org.openprovenance.prov.model.Other) attribute);
                }
            }
        }
    }

/*
        if (attributes != null) {
            split(attributes,
                    labels,
                    types,
                    values,
                    locations,
                    roles,
                    others);


            location.addAll(locations);
            type.addAll(types);
            role.addAll(roles);
            for (Collection<Other> col : others.values())
                other.addAll(col);

            for (Label lab : labels) {
                label.add((LangString) lab.value);
            }
            if (value != null) {
                for (Value val : values) {
                    value[0] = val;
                    break;
                }
            }
        }

 */


}
