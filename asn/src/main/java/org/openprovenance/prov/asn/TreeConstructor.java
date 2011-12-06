package org.openprovenance.prov.asn;
import java.util.List;

public interface TreeConstructor {

    public Object convertActivity(Object id,Object recipe,Object startTime,Object endTime, Object aAttrs);
    public Object convertEntity(Object id, Object attrs);
    public Object convertAgent(Object id, Object attrs);
    public Object convertContainer(Object nss, List<Object> records);
    public Object convertAttributes(List<Object> attributes);
    public Object convertId(String id);
    public Object convertAttribute(Object name, Object value);
    public Object convertStart(String start);
    public Object convertEnd(String end);
    public Object convertA(Object a);
    public Object convertU(Object a);
    public Object convertG(Object a);
    public Object convertString(String s);
    public Object convertInt(int i);
    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs);
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs);
    public Object convertWasDerivedFrom(Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs);
    public Object convertQNAME(String qname);
    public Object convertIRI(String iri);
    public Object convertRecipe(String recipe);
    public Object convertPrefix(String pre);
    public Object convertTypedLiteral(String datatype, Object value);
    public Object convertNamespace(Object pre, Object iri);
    public Object convertDefaultNamespace(Object iri);
    public Object convertNamespaces(List<Object> namespaces);
}