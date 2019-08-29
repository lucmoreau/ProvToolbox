package org.openprovenance.prov.core;

import org.apache.log4j.Logger;
import org.openprovenance.prov.core.serialization.ProvSerialiser;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.Value;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasStartedBy;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;


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
        switch (kind) {

            case PROV_TYPE:
                return newType(value, type);
            case PROV_LABEL:
                return new Label(type, value);
            case PROV_ROLE:
                return newRole(value, type);
            case PROV_LOCATION:
                return newLocation(value, type);
            case PROV_VALUE:
                return newValue(value, type);
        }

        throw new UnsupportedOperationException("Should never be here, unknown " + kind);
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

    /**
     * Creates a new {@link org.openprovenance.prov.model.Entity} with provided identifier and label
     * @param id a {@link org.openprovenance.prov.model.QualifiedName} for the entity
     * @param label a String for the label property (see {@link HasLabel#getLabel()}
     * @return an object of type {@link org.openprovenance.prov.model.Entity}
     */
    @Override
    public org.openprovenance.prov.model.Entity newEntity(org.openprovenance.prov.model.QualifiedName id, String label) {
        Collection<Attribute> attrs=new LinkedList<>();
        attrs.add(newAttribute(Attribute.AttributeKind.PROV_LABEL,newInternationalizedString(label),getName().XSD_STRING));
        org.openprovenance.prov.model.Entity res =  new org.openprovenance.prov.core.Entity(id,attrs);
        return res;
    }

    /**
     * Creates a new {@link org.openprovenance.prov.model.Entity} with provided identifier and attributes
     * @param id a {@link org.openprovenance.prov.model.QualifiedName} for the entity
     * @param attributes a collection of {@link Attribute} for the entity
     * @return an object of type {@link org.openprovenance.prov.model.Entity}
     */
    @Override
    public org.openprovenance.prov.model.Entity newEntity(org.openprovenance.prov.model.QualifiedName id, Collection<Attribute> attributes) {
        org.openprovenance.prov.model.Entity res = new org.openprovenance.prov.core.Entity(id,attributes);
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
     * @param ag a {@link org.openprovenance.prov.model.QualifiedName} for the agent
     * @return an object of type {@link org.openprovenance.prov.model.Agent}
     */
    public org.openprovenance.prov.model.Agent newAgent(org.openprovenance.prov.model.QualifiedName ag) {
        org.openprovenance.prov.model.Agent res = new org.openprovenance.prov.core.Agent(ag,new LinkedList<>());
        return res;
    }

    /**
     * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier and attributes
     * @param id a {@link org.openprovenance.prov.model.QualifiedName} for the agent
     * @param attributes a collection of {@link Attribute} for the agent
     * @return an object of type {@link org.openprovenance.prov.model.Agent}
     */

    public org.openprovenance.prov.model.Agent newAgent(org.openprovenance.prov.model.QualifiedName id, Collection<Attribute> attributes) {
        org.openprovenance.prov.model.Agent res = new org.openprovenance.prov.core.Agent(id,attributes);
        return res;
    }

    /**
     * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier and label
     * @param ag a {@link org.openprovenance.prov.model.QualifiedName} for the agent
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
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
     * @return an instance of {@link org.openprovenance.prov.model.Used}
     */

    public org.openprovenance.prov.model.Used newUsed(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName activity, org.openprovenance.prov.model.QualifiedName entity) {
        org.openprovenance.prov.model.Used res = new org.openprovenance.prov.core.Used(id,activity,entity, new LinkedList<>());
        return res;
    }


    /** A factory method to create an instance of a usage {@link org.openprovenance.prov.model.Used}
     * @param activity the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#usage.activity">activity</a> that used an entity
     * @param entity an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#usage.entity">entity</a> being used
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
                                                                          org.openprovenance.prov.model.QualifiedName eid,
                                                                          String role,
                                                                          org.openprovenance.prov.model.QualifiedName aid) {
        WasGeneratedBy res = new org.openprovenance.prov.core.WasGeneratedBy(id,eid,aid,new LinkedList<>());
        if (role!=null) addRole(res, newRole(role,getName().XSD_STRING));
        return res;
    }


    public org.openprovenance.prov.model.WasInvalidatedBy newWasInvalidatedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                              org.openprovenance.prov.model.QualifiedName eid,
                                                                              String role,
                                                                              org.openprovenance.prov.model.QualifiedName aid) {
        WasInvalidatedBy res = new org.openprovenance.prov.core.WasInvalidatedBy(id,eid,aid,new LinkedList<>());
        if (role!=null) addRole(res, newRole(role,getName().XSD_STRING));
        return res;
    }


    /** A factory method to create an instance of an invalidation {@link org.openprovenance.prov.model.WasInvalidatedBy}
     * @param id an optional identifier for a usage
     * @param entity an identifier for the created <a href="http://www.w3.org/TR/prov-dm/#invalidation.entity">entity</a>
     * @param activity an optional identifier  for the <a href="http://www.w3.org/TR/prov-dm/#invalidation.activity">activity</a> that creates the entity
     * @return an instance of {@link org.openprovenance.prov.model.WasInvalidatedBy}
     */

    public org.openprovenance.prov.model.WasInvalidatedBy newWasInvalidatedBy(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName entity, org.openprovenance.prov.model.QualifiedName activity) {
        WasInvalidatedBy res = new org.openprovenance.prov.core.WasInvalidatedBy(id,entity,activity,new LinkedList<>());
        return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasInvalidatedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public org.openprovenance.prov.model.WasInvalidatedBy newWasInvalidatedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                              org.openprovenance.prov.model.QualifiedName entity,
                                                                              org.openprovenance.prov.model.QualifiedName activity,
                                                                              XMLGregorianCalendar time,
                                                                              Collection<Attribute> attributes) {
        org.openprovenance.prov.model.WasInvalidatedBy res=new  org.openprovenance.prov.core.WasInvalidatedBy(id,entity,activity,attributes);
        res.setTime(time);
        return res;
    }




    /** A factory method to create an instance of an Association {@link org.openprovenance.prov.model.WasAssociatedWith}
     * @param id an optional identifier for the association between an activity and an agent
     * @param activity an identifier for the activity
     * @param agent an optional identifier for the agent associated with the activity
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
     * @param entity an entity identifier
     * @param agent  the identifier of the agent whom the entity is ascribed to, and therefore bears some responsibility for its existence
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


    /** A factory method to create an instance of a derivation {@link org.openprovenance.prov.model.WasDerivedFrom}
     * @param id an optional identifier for a derivation
     * @param e2 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.generatedEntity">entity generated</a> by the derivation
     * @param e1 the identifier  of the <a href="http://www.w3.org/TR/prov-dm/#derivation.usedEntity">entity used</a> by the derivation
     * @return an instance of {@link org.openprovenance.prov.model.WasDerivedFrom}
     */
    public org.openprovenance.prov.model.WasDerivedFrom newWasDerivedFrom(org.openprovenance.prov.model.QualifiedName id,
                                                                          org.openprovenance.prov.model.QualifiedName e2,
                                                                          org.openprovenance.prov.model.QualifiedName e1) {
        WasDerivedFrom res = new org.openprovenance.prov.core.WasDerivedFrom(id,e2,e1,Collections.EMPTY_LIST);
        return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasDerivedFrom(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */
    @Override
    public WasDerivedFrom newWasDerivedFrom(org.openprovenance.prov.model.QualifiedName id,
                                            org.openprovenance.prov.model.QualifiedName e2,
                                            org.openprovenance.prov.model.QualifiedName e1,
                                            org.openprovenance.prov.model.QualifiedName a,
                                            org.openprovenance.prov.model.QualifiedName gen,
                                            org.openprovenance.prov.model.QualifiedName use,
                                            Collection<Attribute> attributes) {
        WasDerivedFrom res = new org.openprovenance.prov.core.WasDerivedFrom(id,e2,e1, a, gen, use, attributes);
        return res;
    }


    public Document newDocument(Collection<Activity> ps,
                                Collection<org.openprovenance.prov.model.Entity> as,
                                Collection<Agent> ags,
                                Collection<Statement> lks) {
        Document res = newDocument();
        res.getStatementOrBundle().addAll(ps);
        res.getStatementOrBundle().addAll(as);
        res.getStatementOrBundle().addAll(ags);
        res.getStatementOrBundle().addAll(lks);
        return res;
    }


    public Document newDocument(Document graph) {
        Document res = newDocument();
        res.getStatementOrBundle()
                .addAll(graph.getStatementOrBundle());
        if (graph.getNamespace()!=null) {
            res.setNamespace(new Namespace(graph.getNamespace()));
        }
        return res;
    }


    /** A factory method to create an instance of an communication {@link org.openprovenance.prov.model.WasInformedBy}
     * @param id an optional identifier identifying the association;
     * @param informed the identifier of the informed activity;
     * @param informant the identifier of the informant activity;
     * @return an instance of {@link org.openprovenance.prov.model.WasInformedBy}
     */

    public org.openprovenance.prov.model.WasInformedBy newWasInformedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                        org.openprovenance.prov.model.QualifiedName informed,
                                                                        org.openprovenance.prov.model.QualifiedName informant) {
        org.openprovenance.prov.model.WasInformedBy res = new org.openprovenance.prov.core.WasInformedBy(id,informed,informant,Collections.EMPTY_LIST);
        return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasInformedBy(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */

    public org.openprovenance.prov.model.WasInformedBy newWasInformedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                        org.openprovenance.prov.model.QualifiedName informed,
                                                                        org.openprovenance.prov.model.QualifiedName informant,
                                                                        Collection<Attribute> attributes) {
        org.openprovenance.prov.model.WasInformedBy res = new org.openprovenance.prov.core.WasInformedBy(id,informed,informant,attributes);
        return res;
    }



    /**A factory method to create an instance of an influence {@link org.openprovenance.prov.model.WasInfluencedBy}
     * @param id optional identifier identifying the association
     * @param influencee an identifier for an entity, activity, or agent
     * @param influencer an identifier for an ancestor entity, activity, or agent that the former depends on
     *
     * @return an instance of {@link org.openprovenance.prov.model.WasInfluencedBy}
     */

    public org.openprovenance.prov.model.WasInfluencedBy newWasInfluencedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                            org.openprovenance.prov.model.QualifiedName influencee,
                                                                            org.openprovenance.prov.model.QualifiedName influencer) {
        org.openprovenance.prov.model.WasInfluencedBy res = new org.openprovenance.prov.core.WasInfluencedBy(id,influencee,influencer,Collections.EMPTY_LIST);
        return res;
    }

    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasInfluencedBy(org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, org.openprovenance.prov.model.QualifiedName, java.util.Collection)
     */

    public org.openprovenance.prov.model.WasInfluencedBy newWasInfluencedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                            org.openprovenance.prov.model.QualifiedName influencee,
                                                                            org.openprovenance.prov.model.QualifiedName influencer,
                                                                            Collection<Attribute> attributes) {
        org.openprovenance.prov.model.WasInfluencedBy res = new org.openprovenance.prov.core.WasInfluencedBy(id,influencee,influencer,attributes);
        return res;
    }


    public org.openprovenance.prov.model.HadMember newHadMember(org.openprovenance.prov.model.QualifiedName collection, org.openprovenance.prov.model.QualifiedName... entities) {
        org.openprovenance.prov.model.HadMember res = new org.openprovenance.prov.core.jsonld.HadMember(collection,Arrays.asList(entities));
        return res;
    }


    public org.openprovenance.prov.model.HadMember newHadMember(org.openprovenance.prov.model.QualifiedName c, Collection<org.openprovenance.prov.model.QualifiedName> e) {
        List<org.openprovenance.prov.model.QualifiedName> ll=new LinkedList<org.openprovenance.prov.model.QualifiedName>();
        if (e!=null) {
            for (org.openprovenance.prov.model.QualifiedName q: e) {
                ll.add(q);
            }
        }
        HadMember res = new org.openprovenance.prov.core.jsonld.HadMember(c,ll);
        return res;
    }


    public org.openprovenance.prov.model.WasStartedBy newWasStartedBy(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName aid, org.openprovenance.prov.model.QualifiedName eid) {
        org.openprovenance.prov.model.WasStartedBy res = new org.openprovenance.prov.core.WasStartedBy(id,aid,eid,null,null,Collections.EMPTY_LIST);
        return res;
    }

    /** A factory method to create an instance of a start {@link org.openprovenance.prov.model.WasStartedBy}
     * @param id
     * @param activity an identifier for the started <a href="http://www.w3.org/TR/prov-dm/#start.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.trigger">entity triggering</a> the activity
     * @param starter an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#start.starter">activity</a> that generated the (possibly unspecified) entity
     * @return an instance of {@link org.openprovenance.prov.model.WasStartedBy}
     */

    public org.openprovenance.prov.model.WasStartedBy newWasStartedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                      org.openprovenance.prov.model.QualifiedName activity,
                                                                      org.openprovenance.prov.model.QualifiedName trigger,
                                                                      org.openprovenance.prov.model.QualifiedName starter) {
        org.openprovenance.prov.model.WasStartedBy res = new org.openprovenance.prov.core.WasStartedBy(id,activity,trigger,starter,null,Collections.EMPTY_LIST);
        return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasStartedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public org.openprovenance.prov.model.WasStartedBy newWasStartedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                      org.openprovenance.prov.model.QualifiedName activity,
                                                                      org.openprovenance.prov.model.QualifiedName trigger,
                                                                      org.openprovenance.prov.model.QualifiedName starter,
                                                                      XMLGregorianCalendar time,
                                                                      Collection<Attribute> attributes) {
        org.openprovenance.prov.model.WasStartedBy res = new org.openprovenance.prov.core.WasStartedBy(id,activity,trigger,starter,time,attributes);
        return res;
    }




    public org.openprovenance.prov.model.WasEndedBy newWasEndedBy(org.openprovenance.prov.model.QualifiedName id, org.openprovenance.prov.model.QualifiedName aid, org.openprovenance.prov.model.QualifiedName eid) {
        org.openprovenance.prov.model.WasEndedBy res = new org.openprovenance.prov.core.WasEndedBy(id,aid,eid,null,null,Collections.EMPTY_LIST);
        return res;
    }

    /** A factory method to create an instance of a end {@link org.openprovenance.prov.model.WasEndedBy}
     * @param id
     * @param activity an identifier for the ended <a href="http://www.w3.org/TR/prov-dm/#end.activity">activity</a>
     * @param trigger an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.trigger">entity triggering</a> the activity
     * @param ender an optional identifier for the <a href="http://www.w3.org/TR/prov-dm/#end.ender">activity</a> that generated the (possibly unspecified) entity
     * @return an instance of {@link org.openprovenance.prov.model.WasEndedBy}
     */

    public org.openprovenance.prov.model.WasEndedBy newWasEndedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                  org.openprovenance.prov.model.QualifiedName activity,
                                                                  org.openprovenance.prov.model.QualifiedName trigger,
                                                                  org.openprovenance.prov.model.QualifiedName ender) {
        org.openprovenance.prov.model.WasEndedBy res = new org.openprovenance.prov.core.WasEndedBy(id,activity,trigger,ender,null,Collections.EMPTY_LIST);
        return res;
    }

    /* (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newWasEndedBy(org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, org.openprovenance.model.QualifiedName, javax.xml.datatype.XMLGregorianCalendar, java.util.Collection)
     */
    public org.openprovenance.prov.model.WasEndedBy newWasEndedBy(org.openprovenance.prov.model.QualifiedName id,
                                                                  org.openprovenance.prov.model.QualifiedName activity,
                                                                  org.openprovenance.prov.model.QualifiedName trigger,
                                                                  org.openprovenance.prov.model.QualifiedName ender,
                                                                  XMLGregorianCalendar time,
                                                                  Collection<Attribute> attributes) {
        org.openprovenance.prov.model.WasEndedBy res = new org.openprovenance.prov.core.WasEndedBy(id,activity,trigger,ender,time,attributes);
        return res;
    }


    /*
     * (non-Javadoc)
     * @see org.openprovenance.prov.model.ModelConstructor#newDocument(org.openprovenance.prov.model.Namespace, java.util.Collection, java.util.Collection)
     */
    @Override
    public Document newDocument(Namespace namespace,
                                Collection<Statement> statements,
                                Collection<Bundle> bundles) {
        Document res = newDocument();

        res.setNamespace(namespace);
        res.getStatementOrBundle()
                .addAll(statements);
        res.getStatementOrBundle()
                .addAll(bundles);
        return res;
    }

}
