package org.openprovenance.prov.xml;
import java.util.List;
import javax.xml.namespace.QName;

/** An interface for constructing data structures when PROV Java beans
 * are traversed by BeanTraversal class. */

public interface BeanConstructor {
    public Object convert(QName q);

    public Object convertEntity(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, Object value, List<Attribute> otherAttrs);
    public Object convertAgent(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Attribute> otherAttrs);
    public Object convertActivity(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Attribute> otherAttrs, Object startTime, Object endTime);

    public Object convertUsed(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Object> roleAttr, List<Attribute> otherAttrs, Object activity, Object entity, Object time);
    public Object convertWasStartedBy(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Object> roleAttr, List<Attribute> otherAttrs, Object activity, Object entity, Object starter, Object time);
    public Object convertWasEndedBy(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Object> roleAttr, List<Attribute> otherAttrs, Object activity, Object entity, Object ender, Object time);
    public Object convertWasGeneratedBy  (Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Object> roleAttr, List<Attribute> otherAttrs, Object entity, Object activity, Object time);
    public Object convertWasInvalidatedBy(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> locAttr, List<Object> roleAttr, List<Attribute> otherAttrs, Object entity, Object activity, Object time);

    public Object convertWasInformedBy(Object id, List<Object> tAttrs, List<Object> lAttr,List<Attribute> otherAttrs, Object effect, Object cause);
    public Object convertWasInfluencedBy(Object id, List<Object> tAttrs,List<Object> lAttr, List<Attribute> otherAttrs, Object effect, Object cause);
 

    public Object convertWasDerivedFrom(Object id, List<Object> tAttrs, List<Object> lAttr, List<Attribute> otherAttrs, Object effect, Object cause, Object activity, Object generation, Object usage);

    public Object convertWasAssociatedWith(Object id, List<Object> tAttrs, List<Object> lAttr, List<Object> rAttr, List<Attribute> otherAttrs, Object activity, Object agent, Object plan);
    public Object convertWasAttributedTo(Object id, List<Object> tAttrs, List<Object> lAttr, List<Attribute> otherAttrs, Object entity, Object agent);
    public Object convertActedOnBehalfOf(Object id, List<Object> tAttrs, List<Object> lAttr, List<Attribute> otherAttrs, Object subordinate, Object responsible, Object activity);



    public Object convertAlternateOf(Object entity2, Object entity1);
    public Object convertSpecializationOf(Object entity2, Object entity1);
    public Object convertMentionOf(Object entity2, Object entity1, Object bundle);



    public Object convertAttributeValue(org.w3c.dom.Element a);
    public Object convertAttribute(Object name, Object value);
    public Object convertTypedLiteral(String datatype, Object value);
    public void startBundle(Object bundleId);
    public Object convertBundle(Object namespaces,
				List<Object> sRecords,
				List<Object> bRecords);
    public Object convertNamedBundle(Object id,
				     Object namespaces,
                                     List<Object> aRecords,
                                     List<Object> eRecords,
                                     List<Object> agRecords,
                                     List<Object> lnkRecords);

    public Object convertHadMember(Object collection, Object entity);


}
