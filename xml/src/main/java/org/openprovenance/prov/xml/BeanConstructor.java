package org.openprovenance.prov.xml;
import java.util.List;
import javax.xml.namespace.QName;

/** An interface for constructing data structures when PROV Java beans
 * are traversed by BeanTraversal class. */

public interface BeanConstructor {
    public Object convert(QName q);
    public Object convertEntity(Object id, List<Object> tAttrs, Object lAttr, List<Object> otherAttrs);
    public Object convertAgent(Object id, List<Object> tAttrs, Object lAttr, List<Object> otherAttrs);
    public Object convertActivity(Object id, List<Object> tAttrs, Object lAttr, List<Object> otherAttrs, Object startTime, Object endTime);
    public Object convertContainer(Object namespaces,
                                   List<Object> aRecords,
                                   List<Object> eRecords,
                                   List<Object> agRecords,
                                   List<Object> lnkRecords);

    public Object convertWasAssociatedWith(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object activity, Object agent, Object plan);
    public Object convertUsed(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object activity, Object entity);
    public Object convertWasDerivedFrom(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object effect, Object cause);

    public Object convertHasAnnotation(Object id, List<Object> tAttrs, List<Object> otherAttrs);
    public Object convertWasInformedBy(Object id, List<Object> tAttrs, List<Object> otherAttrs);
    public Object convertAlternateOf(Object entity2, Object entity1);
    public Object convertSpecializationOf(Object entity2, Object entity1);
    public Object convertWasGeneratedBy(Object id, List<Object> tAttrs, List<Object> otherAttrs, Object entity, Object activity);

    public Object convertAttributeValue(org.w3c.dom.Element a);
    public Object convertAttribute(Object name, Object value);
    public Object convertTypedLiteral(String datatype, Object value);

}
