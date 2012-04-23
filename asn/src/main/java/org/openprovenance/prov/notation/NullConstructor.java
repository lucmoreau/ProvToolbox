package org.openprovenance.prov.notation;
import java.util.List;

public class NullConstructor implements TreeConstructor {



    /* Component 1 */
    public Object convertEntity(Object id, Object attrs) {
        return null;
    }

    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        return null;
    }
    public Object convertStart(String start) {
        return null;
    }
    public Object convertEnd(String end) {
        return null;
    }
    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }

    public Object convertWasStartedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }

    public Object convertWasEndedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }

    public Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        return null;
    }

    public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        return null;
    }

    public Object convertWasStartedByActivity(Object id, Object id2, Object id1, Object aAttrs) {
        return null;
    }


    /* Component 2 */

    public Object convertAgent(Object id, Object attrs) {
        return null;
    }
    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object aAttrs) {
        return null;
    }
    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        return null;
    }
    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        return null;
    }

    /* Component 3 */

    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object q2, Object q1, Object dAttrs) {
        return null;
    }
    public Object convertWasRevisionOf(Object id, Object id2,Object id1, Object ag, Object dAttrs) {
        return null;
    }
    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object ag2, Object ag1, Object dAttrs) {
        return null;
    }
    public Object convertHadOriginalSource(Object id, Object id2,Object id1, Object dAttrs) {
        return null;
    }
    public Object convertTracedTo(Object id, Object id2, Object id1, Object dAttrs) {
        return null;
    }


    /* Component 4 */

    public Object convertAlternateOf(Object id2, Object id1) {
        return null;
    }

    public Object convertSpecializationOf(Object id2,Object id1) {
        return null;
    }
    /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1, Object map, Object dAttrs) {
	return null;
    }

    public Object convertRemoval(Object id, Object id2, Object id1, Object keyset, Object dAttrs) {
	return null;
    }

    public Object convertMemberOf(Object id, Object id2, Object map, Object complete, Object dAttrs) {
	return null;
    }
    
    public Object convertEntry(Object o1, Object o2) {
	return null;
    }

    public Object convertKeyEntitySet(List<Object> o) {
	return null;
    }

    public Object convertKeys(List<Object> o) {
	return null;
    }

    /* Component 6 */

    public Object convertNote(Object id, Object attrs) {
        return null;
    }
    public Object convertHasAnnotation(Object something, Object note) {
        return null;
    }


    /* Others */

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

    public Object convertString(String s) {
        return null;
    }

    public Object convertInt(int s) {
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