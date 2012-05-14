package org.openprovenance.prov.notation;
import java.util.List;

/** For testing purpose, conversion back to ASN. */

public class ASNConstructor implements TreeConstructor {


    public String optionalAttributes(Object attrs) {
        String s_attrs=(String)attrs;
        if ("".equals(s_attrs)) {
            return "";
        } else {
            return ",[" + attrs + "]";
        }
    }
    public String optionalTime(Object time) {
        return ((time==null)? "" : (", " + time));
    }            

    public Object optional2(Object str) {
        return ((str==null)? "" : str);
    }


    public Object optional(Object str) {
        return ((str==null)? "-" : str);
    }

    public Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        String s="activity(" + id + "," + optional(startTime) + "," + optional(endTime) + optionalAttributes(aAttrs) + ")";
        return s;
    }
    public Object convertEntity(Object id, Object attrs) {
        String s="entity(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }
    public Object convertAgent(Object id, Object attrs) {
        String s="agent(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }
    public Object convertBundle(Object namespaces, List<Object> records, List<Object> bundles) {
        String s="bundle\n";
        s=s+namespaces;
        for (Object o: records) {
            s=s+o+"\n";
        }
	if (bundles!=null) {
	    for (Object o: bundles) {
		s=s+o+"\n";
	    }
	}
        s=s+"endBundle";
        return s;
    }

    public Object convertNamedBundle(Object id, Object namespaces, List<Object> records) {
        String s="bundle " + id + "\n";
        s=s+namespaces;
	if (records!=null) 
	    for (Object o: records) {
		s=s+o+"\n";
	    }
        s=s+"endBundle";
        return s;
    }



    public Object convertAttributes(List<Object> attributes) {
        String s="";
        boolean first=true;
        for (Object o: attributes) {
            if (first) {
                first=false;
                s=s+o;
            } else {
                s=s+","+o;
            }
        }
        return s;
    }
    public Object convertId(String id) {
        return id;
    }


    public Object convertAttribute(Object name, Object value) {
        return name + "=" + value;
    }
    public Object convertStart(String start) {
        return start;
    }
    public Object convertEnd(String end) {
        return end;
    }
    public Object convertString(String s) {
        return s;
    }

    public Object convertInt(int i) {
        return i;
    }

    public String optionalId(Object id) {
        return ((id==null)? "" : (id + ","));
    }            

    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        String s="used(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) + ")";
        return s;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasGeneratedBy(" + optionalId(id) + id2 + "," + optional(id1) + "," +
            optional(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }
    public Object convertWasStartedBy(Object id, Object id2,Object id1, Object id3, Object time, Object aAttrs ) {
        String s="wasStartedBy(" + optionalId(id) + id2 + "," + optional(id1) + "," + optional(id3) + "," +
            optional(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }
    public Object convertWasEndedBy(Object id, Object id2,Object id1, Object id3, Object time, Object aAttrs ) {
        String s="wasEndedBy(" + optionalId(id) + id2 + "," + optional(id1) + "," + optional(id3) + "," +
            optional(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        String s="wasInformedBy(" + optionalId(id) + id2 + "," + optional(id1)
            + optionalAttributes(aAttrs) +  ")";
        return s;
    }


    public Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s="wasInvalidatedBy(" + optionalId(id) + id2 + "," + optional(id1) + "," +
            optional(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }


    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object aAttrs ) {
        String s="wasAttributedTo(" + optionalId(id) + id2 + ", " + optional(id1) +
            optionalAttributes(aAttrs) +  ")";
        return s;
    }


    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="wasDerivedFrom(" + optionalId(id) + id2 + ", " + id1 + 
            ((pe==null && g2==null && u1==null) ?
             "" : ", " + optional(pe) + ", " + optional(g2) + ", " + optional(u1)) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasRevisionOf(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="wasRevisionOf(" + optionalId(id) + id2 + ", " + id1 + 
            ((pe==null && g2==null && u1==null) ?
             "" : ", " + optional(pe) + ", " + optional(g2) + ", " + optional(u1)) + optionalAttributes(aAttrs) +  ")";
        return s;
    }
    public Object convertWasQuotedFrom(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="wasQuotedFrom(" + optionalId(id) + id2 + ", " + id1 + 
            ((pe==null && g2==null && u1==null) ?
             "" : ", " + optional(pe) + ", " + optional(g2) + ", " + optional(u1)) + optionalAttributes(aAttrs) +  ")";
        return s;
    }
    public Object convertHadOriginalSource(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="hadOriginalSource(" + optionalId(id) + id2 + ", " + id1 + 
            ((pe==null && g2==null && u1==null) ?
             "" : ", " + optional(pe) + ", " + optional(g2) + ", " + optional(u1)) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertTracedTo(Object id, Object id2, Object id1, Object dAttrs) {
        String s="tracedTo(" + optionalId(id) + id2 + ", " + id1 + optionalAttributes(dAttrs) +  ")";
        return s;
    }


    public Object convertAlternateOf(Object id2,Object id1) {
        String s="alternateOf(" + id2 + "," + id1 + ")";
        return s;
    }

    public Object convertSpecializationOf(Object id2, Object id1) {
        String s="specializationOf(" + id2 + "," + id1 + ")";
        return s;
    }


    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        String s="wasAssociatedWith(" + optionalId(id) + id2 + "," + optional(id1) + "," +
            optional(pl) +
            optionalAttributes(aAttrs) + ")";
        return s;
    }

    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        String s="actedOnBehalfOf(" + optionalId(id) + id2 + "," + id1 + "," +
            optional(a) +
            optionalAttributes(aAttrs) + ")";
        return s;
    }


    public Object convertQualifiedName(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        return iri;
    }

    public Object convertTypedLiteral(String datatype, Object value) {
        if ("xsd:QName".equals(datatype)) {
            String val=(String)value;
            return "'" + val.substring(1, val.length() -1 ) + "'";
        } else {
            return value + "%%" + datatype;
        }
    }

   public Object convertNamespace(Object pre, Object iri) {
       return "prefix " + pre + " " + iri;
   }

   public Object convertDefaultNamespace(Object iri) {
       return  "default " + iri;
   }

    public Object convertNamespaces(List<Object> namespaces) {
        String s="";
        for (Object o: namespaces) {
            s=s+o+"\n";
        }
        return s;
    }

    public Object convertPrefix(String pre) {
        return pre;
    }

    /* Component 5 */

    public Object convertInsertion(Object id, Object id2, Object id1, Object kes, Object iAttrs) {
        String s="derivedByInsertionFrom(" + optionalId(id) + id2 + ", " + id1 + ", "
	    + kes + optionalAttributes(iAttrs) +  ")";
	return s;
    }

    public Object convertRemoval(Object id, Object id2, Object id1, Object keyset, Object rAttrs) {
        String s="derivedByRemovalFrom(" + optionalId(id) + id2 + ", " + id1 + ", "
	    + keyset + optionalAttributes(rAttrs) +  ")";
	return s;

    }

    public Object convertMemberOf(Object id, Object id2, Object kes, Object complete, Object mAttrs) {

        String s="memberOf(" + optionalId(id) + id2 + ", "
	    + kes + ((complete==null)? "" : ", "+complete) + optionalAttributes(mAttrs) +  ")";
	return s;

    }

    public Object convertEntry(Object o1, Object o2) {
        String s="{" + o1 + ", " + o2 + "}";
	return s;
    }

    public Object convertKeyEntitySet(List<Object> entries) {
        String s="{";

	boolean first=true;

	for (Object entry: entries) {
	    if (!first) {
		s=s+", ";
	    } else {
		first=false;
	    }


	    s=s + entry;
	}
	s=s+"}";
	return s;
    }


    public Object convertKeys(List<Object> keys) {
        String s="{";

	boolean first=true;

	for (Object key: keys) {
	    if (!first) {
		s=s+", ";
	    } else {
		first=false;
	    }


	    s=s + key;
	}
	s=s+"}";
	return s;
    }



    /* Component 6 */

    public Object convertNote(Object id, Object attrs) {
        String s="note(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }

    public Object convertHasAnnotation(Object something, Object note) {
        String s="hasAnnotation(" + something  + "," + note + ")";
        return s;
    }

    public Object convertHasProvenanceIn(Object uid,Object su, Object bu, Object ta, Object se, Object pr, Object dAttrs) {
        String s="hasProvenanceIn(" + optionalId(uid) + "," + su + optional(bu) + optional(ta) + optional(se) + optional(pr) + optionalAttributes(dAttrs) + ")";
        return s;
    }


}