package org.openprovenance.prov.asn;
import java.util.List;

public class NullConstructor implements TreeConstructor {



    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        return null;
    }
    public Object convertEntity(Object id, Object attrs) {
        return null;
    }
    public Object convertAgent(Object id, Object attrs) {
        return null;
    }
    public Object convertContainer(Object nss, List<Object> records) {
        return null;
    }
    public Object convertAttributes(List<Object> attributes) {
        return null;
    }
    public Object convertId(String id) {
        return null;
    }
    public Object convertAttribute(Object name, Object value) {
        return null;
    }
    public Object convertStart(String start) {
        return null;
    }
    public Object convertEnd(String end) {
        return null;
    }
    public Object convertA(Object a) {
        return null;
    }
    public Object convertU(Object a) {
        return null;
    }
    public Object convertG(Object a) {
        return null;
    }
    public Object convertString(String s) {
        return null;
    }

    public Object convertInt(int s) {
        return null;
    }

    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }
    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        return null;
    }

    public Object convertWasComplementOf(Object id, Object id2,Object id1, Object aAttrs) {
        return null;
    }

    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object aAttrs) {
        return null;
    }

    public Object convertHadPlan(Object id, Object id2,Object id1, Object aAttrs) {
	return null;
    }


    public Object convertQNAME(String qname) {
        return null;
    }
    public Object convertIRI(String iri) {
        return null;
    }

    public Object convertTypedLiteral(String datatype, Object value) {
        return null;
    }
    public Object convertNamespace(Object pre, Object iri) {
        return null;
    }

    public Object convertDefaultNamespace(Object iri) {
        return null;
    }
    public Object convertNamespaces(List<Object> namespaces) {
        return null;
    }
    public Object convertPrefix(String pre) {
        return null;
    }

}