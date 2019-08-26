package org.openprovenance.prov.core;

import org.apache.log4j.Logger;
import org.openprovenance.prov.core.serialization.ProvSerialiser;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.Value;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasGeneratedBy;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;


public class ProvFactory extends org.openprovenance.prov.model.ProvFactory {
    static Logger logger = Logger.getLogger(ProvFactory.class);

    private final static ProvFactory oFactory = new ProvFactory();

    public static ProvFactory getFactory() {
        return oFactory;
    }

    public ProvFactory(ObjectFactory of) {
        super(of);
    }

    public ProvFactory () {
        super(null);
    }

    @Override
    public ProvSerialiser getSerializer() {
        return null;
    }

    @Override
    public Attribute newAttribute(org.openprovenance.prov.model.QualifiedName elementName, Object value, org.openprovenance.prov.model.QualifiedName type) {

 //       if (getName().RDF_LITERAL.equals(type)&& (value instanceof String)) {
 //           value=vconv.convertToJava(type,(String)value);
 //       }

        // TODO: use TypedValue.getAttributeKind and switch on a kind
        if (elementName.equals(getName().PROV_LOCATION)) {
            return newLocation(value, type);
        }
        if (elementName.equals(getName().PROV_TYPE)) {
            return newType(value, type);
        }
        if (elementName.equals(getName().PROV_VALUE)) {
            return newValue(value, type);
        }
        if (elementName.equals(getName().PROV_ROLE)) {
            return newRole(value, type);
        }
        if (elementName.equals(getName().PROV_LABEL)) {
            return new Label(type, value);
        }
        if (elementName.equals(getName().PROV_KEY)) {
            new UnsupportedOperationException("key not there yet");
            //return newKey(value, type);
        }
        return new org.openprovenance.prov.core.Other(elementName, type, value);
    }



    @Override
    public Attribute newAttribute(Attribute.AttributeKind kind, Object value, org.openprovenance.prov.model.QualifiedName type) {
        return null;
    }

    @Override
    public QualifiedName newQualifiedName(String namespace, String local, String prefix) {
        return new QualifiedName(namespace,local,prefix);
    }

    @Override
    public QualifiedName newQualifiedName(String namespace, String local, String prefix, ProvUtilities.BuildFlag flag) {
        return new QualifiedName(namespace,local,prefix);
    }

    @Override
    public org.openprovenance.prov.model.LangString newInternationalizedString(String s,
                                                                               String lang) {
        LangString res = new org.openprovenance.prov.core.LangString(s,lang);
        return res;
    }

    @Override
    public org.openprovenance.prov.model.LangString newInternationalizedString(String s) {
        LangString res = new org.openprovenance.prov.core.LangString(s);
        return res;
    }

    @Override
    public org.openprovenance.prov.model.Type newType(Object value, org.openprovenance.prov.model.QualifiedName type) {
        if (value==null) return null;
        org.openprovenance.prov.model.Type res =  new org.openprovenance.prov.core.Type(type,value);
        return res;
    }

    @Override
    public Other newOther(org.openprovenance.prov.model.QualifiedName elementName, Object value, org.openprovenance.prov.model.QualifiedName type) {
        return new org.openprovenance.prov.core.Other(elementName,type,value);
    }


    @Override
    public org.openprovenance.prov.model.Location newLocation(Object value, org.openprovenance.prov.model.QualifiedName type) {
        if (value==null) return null;
        org.openprovenance.prov.model.Location res =  new org.openprovenance.prov.core.Location(type,value);
        return res;
    }



    @Override
    public org.openprovenance.prov.model.Activity newActivity(org.openprovenance.prov.model.QualifiedName a) {
        Activity res = new org.openprovenance.prov.core.Activity(a,null,null,null);
        return res;
    }

    @Override
    public org.openprovenance.prov.model.Entity newEntity(org.openprovenance.prov.model.QualifiedName a) {
        Entity res = new org.openprovenance.prov.core.Entity(a,null);
        return res;
    }


    @Override
    public org.openprovenance.prov.model.Document newDocument() {
        Document res = new org.openprovenance.prov.core.Document();
        return res;
    }

    @Override
    public Key newKey(Object o, org.openprovenance.prov.model.QualifiedName type) {
        return null;
    }

    public org.openprovenance.prov.model.Value newValue(Object value, org.openprovenance.prov.model.QualifiedName type) {
        if (value==null) return null;
        Value res =  new org.openprovenance.prov.core.Value(type,value);

        return res;
    }


    /**
     * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier
     * @param ag a {@link org.openprovenance.prov.model.QualifiedName} for the specificEntity
     * @return an object of type {@link org.openprovenance.prov.model.Agent}
     */
    public org.openprovenance.prov.model.Agent newAgent(org.openprovenance.prov.model.QualifiedName ag) {
        org.openprovenance.prov.model.Agent res = new org.openprovenance.prov.core.Agent(ag,new LinkedList<>());
        return res;
    }

    /**
     * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier and attributes
     * @param id a {@link org.openprovenance.prov.model.QualifiedName} for the specificEntity
     * @param attributes a collection of {@link Attribute} for the specificEntity
     * @return an object of type {@link org.openprovenance.prov.model.Agent}
     */

    public org.openprovenance.prov.model.Agent newAgent(org.openprovenance.prov.model.QualifiedName id, Collection<Attribute> attributes) {
        org.openprovenance.prov.model.Agent res = new org.openprovenance.prov.core.Agent(id,attributes);
        return res;
    }

    /**
     * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier and label
     * @param ag a {@link org.openprovenance.prov.model.QualifiedName} for the specificEntity
     * @param label a String for the label property (see {@link HasLabel#getLabel()}
     * @return an object of type {@link org.openprovenance.prov.model.Agent}
     */
    public org.openprovenance.prov.model.Agent newAgent(org.openprovenance.prov.model.QualifiedName ag, String label) {
        Agent res = newAgent(ag);
        //new Label(null,newInternationalizedString(label));
        if (label != null)
            res.getLabel().add(newInternationalizedString(label));
        return res;
    }


    /** A factory method to create an instance of a usage {@link org.openprovenance.prov.model.Used}
     * @param id an optional identifier for a usage
     * @return an instance of {@link org.openprovenance.prov.model.Used}
     */
    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName id) {
        org.openprovenance.prov.model.Used res = new org.openprovenance.prov.core.Used(id,new LinkedList<>());
        return res;
    }

    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName aid, String role, org.openprovenance.prov.model.QualifiedName eid) {
        org.openprovenance.prov.model.Used res = newUsed(id);
        res.setActivity(aid);
        if (role!=null)
            addRole(res, newRole(role,getName().XSD_STRING));
        res.setEntity(eid);
        return res;
    }

    /** A factory method to create an instance of a usage {@link org.openprovenance.prov.model.Used}
     * @param id an optional identifier for a usage
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an generalEntity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">generalEntity</a> being used
     * @return an instance of {@link org.openprovenance.prov.model.Used}
     */

    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName activity, org.openprovenance.prov.model.QualifiedName entity) {
        org.openprovenance.prov.model.Used res = new org.openprovenance.prov.core.Used(id,activity,entity, new LinkedList<>());
        return res;
    }


    /** A factory method to create an instance of a usage {@link org.openprovenance.prov.model.Used}
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an generalEntity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">generalEntity</a> being used
     * @return an instance of {@link org.openprovenance.prov.model.Used}
     */

    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName activity, org.openprovenance.prov.model.QualifiedName entity) {
        org.openprovenance.prov.model.Used res = newUsed((org.openprovenance.prov.model.QualifiedName)null, activity, entity);
        return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newUsed(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName activity, org.openprovenance.prov.model.QualifiedName entity,
                                                      XMLGregorianCalendar time,
                                                      Collection<Attribute> attributes) {
        org.openprovenance.prov.model.Used res = newUsed(id, activity, null, entity);
        res.setTime(time);
        setAttributes(res, attributes);
        return res;
    }

    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName activity, org.openprovenance.prov.model.QualifiedName entity,
                                                      XMLGregorianCalendar time) {
        Used res = newUsed(id, activity, null, entity);
        res.setTime(time);
        return res;
    }


    @Override
    public org.openprovenance.prov.model.Role newRole(Object value, org.openprovenance.prov.model.QualifiedName type) {
        if (value==null) return null;
        Role res =  new org.openprovenance.prov.core.Role(type,value);
//        res.setValueFromObject(value);
        return res;
    }

    public org.openprovenance.prov.model.WasGeneratedBy newWasGeneratedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                          org.openprovenance.prov.model.QualifiedName aid,
                                                                          String role,
                                                                          org.openprovenance.prov.model.QualifiedName eid) {
        WasGeneratedBy res = new org.openprovenance.prov.core.WasGeneratedBy(id,aid,eid,new LinkedList<>());
        if (role!=null) addRole(res, newRole(role,getName().XSD_STRING));
        return res;
    }


    /** A factory method to create an instance of an Association {@link org.openprovenance.prov.model.WasAssociatedWith}
     * @param id an optional identifier for the association between an activity and an specificEntity
     * @param activity an identifier for the activity
     * @param agent an optional identifier for the specificEntity associated with the activity
     * @return an instance of {@link org.openprovenance.prov.model.WasAssociatedWith}
     */


    public org.openprovenance.prov.model.WasAssociatedWith newWasAssociatedWith(org.openprovenance.prov.model.QualifiedName id,
                                                                                org.openprovenance.prov.model.QualifiedName activity,
                                                                                org.openprovenance.prov.model.QualifiedName agent) {
        org.openprovenance.prov.model.WasAssociatedWith res = new org.openprovenance.prov.core.WasAssociatedWith(id,activity,agent, Collections.EMPTY_LIST);
        return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasAssociatedWith(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */

    public org.openprovenance.prov.model.WasAssociatedWith newWasAssociatedWith(org.openprovenance.prov.model.QualifiedName id,
                                                                                org.openprovenance.prov.model.QualifiedName a,
                                                                                org.openprovenance.prov.model.QualifiedName ag,
                                                                                org.openprovenance.prov.model.QualifiedName plan,
                                                                                Collection<Attribute> attributes) {
        org.openprovenance.prov.model.WasAssociatedWith res = new org.openprovenance.prov.core.WasAssociatedWith(id,a,ag, plan, attributes);
        return res;
    }

    public org.openprovenance.prov.model.WasAssociatedWith newWasAssociatedWith(org.openprovenance.prov.model.QualifiedName id,
                                                                                org.openprovenance.prov.model.QualifiedName a,
                                                                                org.openprovenance.prov.model.QualifiedName ag,
                                                                                org.openprovenance.prov.model.QualifiedName plan) {
        org.openprovenance.prov.model.WasAssociatedWith res = new org.openprovenance.prov.core.WasAssociatedWith(id,a,ag, plan, Collections.EMPTY_LIST);
        return res;
    }



    public org.openprovenance.prov.model.WasAssociatedWith newWasAssociatedWith(org.openprovenance.prov.model.WasAssociatedWith u) {
        WasAssociatedWith u1 = newWasAssociatedWith(u.getId(), u.getActivity(), u.getAgent(), u.getPlan());
        u1.getType().addAll(u.getType());
        u1.getLabel().addAll(u.getLabel());
        u1.getRole().addAll(u.getRole());
        u1.getOther().addAll(u.getOther());
        return u1;
    }


    /** A factory method to create an instance of an attribution {@link org.openprovenance.prov.model.WasAttributedTo}
     * @param id  an optional identifier for the relation
     * @param entity an generalEntity identifier
     * @param agent  the identifier of the specificEntity whom the generalEntity is ascribed to, and therefore bears some responsibility for its existence
     * @return an instance of {@link org.openprovenance.prov.model.WasAttributedTo}
     */
    public org.openprovenance.prov.model.WasAttributedTo newWasAttributedTo(org.openprovenance.prov.model.QualifiedName id,
                                                                            org.openprovenance.prov.model.QualifiedName entity,
                                                                            org.openprovenance.prov.model.QualifiedName agent) {
        org.openprovenance.prov.model.WasAttributedTo res = new org.openprovenance.prov.core.WasAttributedTo(id,entity,agent,Collections.EMPTY_LIST);
        return res;
    }

    public org.openprovenance.prov.model.WasAttributedTo newWasAttributedTo(org.openprovenance.prov.model.QualifiedName entity,
                                                                            org.openprovenance.prov.model.QualifiedName agent) {
        org.openprovenance.prov.model.WasAttributedTo res = new org.openprovenance.prov.core.WasAttributedTo(null,entity,agent,Collections.EMPTY_LIST);
        return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasAttributedTo(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */
    public org.openprovenance.prov.model.WasAttributedTo newWasAttributedTo(org.openprovenance.prov.model.QualifiedName id,
                                                                            org.openprovenance.prov.model.QualifiedName entity,
                                                                            org.openprovenance.prov.model.QualifiedName agent,
                                                                            Collection<Attribute> attributes) {
        WasAttributedTo res = new org.openprovenance.prov.core.WasAttributedTo(id,entity,agent,attributes);
        return res;
    }

    @Override
    public org.openprovenance.prov.model.SpecializationOf newSpecializationOf(org.openprovenance.prov.model.QualifiedName specific, org.openprovenance.prov.model.QualifiedName general) {
        SpecializationOf res = new org.openprovenance.prov.core.SpecializationOf(specific,general);
        return res;
    }

    /** A factory method to create an instance of an alternate {@link org.openprovenance.prov.model.AlternateOf}
     * @param entity1 an identifier for the first {@link org.openprovenance.prov.model.Entity}
     * @param entity2 an identifier for the second {@link org.openprovenance.prov.model.Entity}
     * @return an instance of {@link org.openprovenance.prov.model.AlternateOf}
     */
    public org.openprovenance.prov.model.AlternateOf newAlternateOf(org.openprovenance.prov.model.QualifiedName entity1, org.openprovenance.prov.model.QualifiedName entity2) {
        org.openprovenance.prov.model.AlternateOf res = new org.openprovenance.prov.core.AlternateOf(entity1,entity2);
        return res;
    }

}
