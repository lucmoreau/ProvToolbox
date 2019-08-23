package org.openprovenance.prov.core;

import org.apache.log4j.Logger;
import org.openprovenance.prov.core.serialization.ProvSerialiser;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.Value;


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
        Entity res = new org.openprovenance.prov.core.Entity(a,null,null,null);
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

}
