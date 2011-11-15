package org.openprovenance.prov.asn;
import java.util.List;

public class NullConstructor implements TreeConstructor {



    public Object convertActivity(Object id,Object recipe,Object startTime,Object endTime, Object aAttrs) {
        return null;
    }
    public Object convertEntity(Object id, Object attrs) {
        return null;
    }
    public Object convertContainer(List<Object> records) {
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
    public Object convertString(String s) {
        return null;
    }
    public Object convertUsed(Object id2,Object id1, Object aAttrs, Object time) {
        return null;
    }
    public Object convertWasGeneratedBy(Object id2,Object id1, Object aAttrs, Object time) {
        return null;
    }
    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object q2, Object q1) {
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

}