package org.openprovenance.prov.notation;
import java.util.List;
import org.openprovenance.prov.xml.InternationalizedString;

/** For testing purpose, conversion back to ASN. */

public class ASNConstructor implements TreeConstructor {

    public String symbol(String s) {
	return s;
    }
    public String showprefix(String s) {
	return s;
    }

    public String optionalAttributes(Object attrs) {
        String s_attrs=(String)attrs;
        if ("".equals(s_attrs)) {
            return "";
        } else {
            return symbol(",[") + attrs + symbol("]");
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
        String s=keyword("activity") + "(" + id + "," + optional(startTime) + "," + optional(endTime) + optionalAttributes(aAttrs) + ")";
        return s;
    }
    public Object convertEntity(Object id, Object attrs) {
        String s=keyword("entity") + "(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }
    public Object convertAgent(Object id, Object attrs) {
        String s=keyword("agent") + "(" + id  + optionalAttributes(attrs) + ")";
        return s;
    }
    public String keyword(String s) {
        return s;
    }
    public String breakline() {
        return "\n";
    }
    public String showuri(String s) {
        return  s;
    }

    public Object convertBundle(Object namespaces, List<Object> records, List<Object> bundles) {
        String s=keyword("bundle") + breakline();
        s=s+namespaces;
        for (Object o: records) {
            s=s+o+breakline();
        }
	if (bundles!=null) {
	    for (Object o: bundles) {
		s=s+o+breakline();
	    }
	}
        s=s+keyword("endBundle");
        return s;
    }

    public Object convertNamedBundle(Object id, Object namespaces, List<Object> records) {
        String s="bundle " + id + breakline();
        s=s+namespaces;
	if (records!=null) 
	    for (Object o: records) {
		s=s+o+breakline();
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
                s=s+symbol(",")+ " " +o;
            }
        }
        return s;
    }
    public Object convertId(String id) {
        return id;
    }


    public Object convertAttribute(Object name, Object value) {
	if (value instanceof InternationalizedString) {
	    InternationalizedString is=(InternationalizedString) value;
	    String lang=is.getLang();
	    if (lang==null) {
		value= "\""+ is.getValue() + "\"";
	    } else { 
		value= "\""+ is.getValue()+"\""+"@"+lang;
	    }
	}
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

    public Object convertString(String s, String lang) {
        return s + "@" + lang;
    }

    public Object convertInt(int i) {
        return i;
    }

    public String optionalId(Object id) {
        return ((id==null)? "" : (id + ";"));
    }            

    public Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        String s=keyword("used") + "(" + optionalId(id) + id2 + "," + id1 +
            optionalTime(time) + optionalAttributes(aAttrs) + ")";
        return s;
    }
    public Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s=keyword("wasGeneratedBy") + "(" + optionalId(id) + id2 + "," + optional(id1) + "," +
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
        String s=keyword("wasInvalidatedBy") + "(" + optionalId(id) + id2 + "," + optional(id1) + "," +
            optional(time) + optionalAttributes(aAttrs) +  ")";
        return s;
    }


    public Object convertWasAttributedTo(Object id, Object id2,Object id1, Object aAttrs ) {
        String s=keyword("wasAttributedTo") + "(" + optionalId(id) + id2 + ", " + optional(id1) +
            optionalAttributes(aAttrs) +  ")";
        return s;
    }


    public Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s=keyword("wasDerivedFrom") + "(" + optionalId(id) + id2 + ", " + id1 + 
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
    public Object convertHadPrimarySource(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s="hadPrimarySource(" + optionalId(id) + id2 + ", " + id1 + 
            ((pe==null && g2==null && u1==null) ?
             "" : ", " + optional(pe) + ", " + optional(g2) + ", " + optional(u1)) + optionalAttributes(aAttrs) +  ")";
        return s;
    }

    public Object convertWasInfluencedBy(Object id, Object id2, Object id1, Object dAttrs) {
        String s="wasInfluencedBy(" + optionalId(id) + id2 + ", " + id1 + optionalAttributes(dAttrs) +  ")";
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

    public Object convertMentionOf(Object su, Object bu, Object ta) {
        String s="mentionOf(" + su + ", " + bu + ", " + ta + ")";
        return s;
    }

    public Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        String s=keyword("wasAssociatedWith") + "(" + optionalId(id) + id2 + "," + optional(id1) + "," +
            optional(pl) +
            optionalAttributes(aAttrs) + ")";
        return s;
    }

    public Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        String s=keyword("actedOnBehalfOf") + "(" + optionalId(id) + id2 + "," + id1 + "," +
            optional(a) +
            optionalAttributes(aAttrs) + ")";
        return s;
    }

    public Object convertExtension(Object name, Object id, Object args, Object dAttrs) {
	System.out.println("Name @" + name);
	System.out.println("Name @" + id);
	System.out.println("Name @" + args);
	System.out.println("Name @" + dAttrs);
        String s=keyword((String)name) + "(" + optionalId(id) + args +
            optionalAttributes(dAttrs) + ")";
	return s;
    }

    public Object convertQualifiedName(String qname) {
        return qname;
    }
    public Object convertIRI(String iri) {
        return iri;
    }
    //TODO should not use xsd:QName

    public Object convertTypedLiteral(String datatype, Object value) {
        if ("xsd:QName".equals(datatype)) {
            String val=(String)value;
            return "'" + val.substring(1, val.length() -1 ) + "'";
        } else {
	    if ("prov:Qualified_Name".equals(datatype)) {
		String val=(String)value;
		return "'" + val.substring(1, val.length() -1 ) + "'";
	    } else {
		return value + "%%" + datatype;
	    }
	}
    }

   public Object convertNamespace(Object pre, Object iri) {
       return keyword("prefix") + " " + showprefix((String)pre) + " " + showuri((String)iri);
   }

   public Object convertDefaultNamespace(Object iri) {
       return  keyword("default") + " " + showuri((String)iri);
   }

    public Object convertNamespaces(List<Object> namespaces) {
        String s="";
        for (Object o: namespaces) {
            s=s+o+breakline();
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

    public Object convertCollectionMemberOf(Object id, Object id2, Object es, Object complete, Object mAttrs) {

        String s="memberOf(" + optionalId(id) + id2 + ", "
	    + es + ((complete==null)? "" : ", "+complete) + optionalAttributes(mAttrs) +  ")";
	return s;

    }


    public Object convertDictionaryMemberOf(Object id, Object id2, Object kes, Object complete, Object mAttrs) {

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





}
