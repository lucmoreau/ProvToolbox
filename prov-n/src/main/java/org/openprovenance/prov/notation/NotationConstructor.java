package org.openprovenance.prov.notation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.BeanConstructor;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Statement;
import org.openprovenance.prov.xml.UncheckedException;
import org.openprovenance.prov.xml.Used;
import org.openprovenance.prov.xml.WasAssociatedWith;
import org.openprovenance.prov.xml.WasAttributedTo;
import org.openprovenance.prov.xml.WasDerivedFrom;
import org.openprovenance.prov.xml.WasEndedBy;
import org.openprovenance.prov.xml.WasGeneratedBy;
import org.openprovenance.prov.xml.WasInfluencedBy;
import org.openprovenance.prov.xml.WasInformedBy;
import org.openprovenance.prov.xml.WasInvalidatedBy;
import org.openprovenance.prov.xml.WasStartedBy;

/** For testing purpose, conversion back to ASN. */

public class NotationConstructor implements BeanConstructor {
    
    final private ProvFactory pFactory;
    final private BufferedWriter buffer;

    public NotationConstructor(Writer writer, ProvFactory pFactory) {
	this.buffer=new BufferedWriter(writer);
	this.pFactory=pFactory;
    }
    
       
    public void write(String s) {
	try {
	    buffer.write(s);
	} catch (IOException e) {
	    throw new UncheckedException("NotationConstructor.write() failed", e);
	}
    }
    
    public void close() {
	try {
	    buffer.close();
	} catch (IOException e) {
	    throw new UncheckedException("convertBeanToNotation: closing writer failed", e);
	}
    }
    
    public void flush() {
	try {
	    buffer.flush();
	} catch (IOException e) {
	    throw new UncheckedException("convertBeanToNotation: closing writer failed", e);
	}
    }


    public String idOrMarker(QName qn) {
        return ((qn==null)? "-" : pFactory.qnameToString(qn));
    }

    
    @Override
    public Entity newEntity(QName id, List<Attribute> attributes) {
        String s=keyword("entity") + "(" + idOrMarker(id)  + optionalAttributes(attributes) + ")";
        write(s);
        System.out.println("s" +s);
	return null;
    }
    @Override
    public Activity newActivity(QName id, XMLGregorianCalendar startTime,
				XMLGregorianCalendar endTime,
				List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public Agent newAgent(QName id, List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public Used newUsed(QName id, QName activity, QName entity,
			XMLGregorianCalendar time, List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity,
					    QName activity,
					    XMLGregorianCalendar time,
					    List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity,
						QName activity,
						XMLGregorianCalendar time,
						List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasStartedBy newWasStartedBy(QName id, QName activity,
					QName trigger, QName starter,
					XMLGregorianCalendar time,
					List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger,
				    QName ender, XMLGregorianCalendar time,
				    List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1,
					    QName activity, QName generation,
					    QName usage,
					    List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag,
						  QName plan,
						  List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,
					      List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1,
					      QName a,
					      List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1,
					  List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1,
					      List<Attribute> attributes) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public AlternateOf newAlternateOf(QName e2, QName e1) {
	write("alternateOf(" + idOrMarker(e2) + "," + idOrMarker(e1) + ")");
	return null;
   }
    @Override
    public SpecializationOf newSpecializationOf(QName e2, QName e1) {
	write("specializationOf(" + idOrMarker(e2) + "," + idOrMarker(e1) + ")");
	return null;
    }
    @Override
    public MentionOf newMentionOf(QName e2, QName e1, QName b) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public HadMember newHadMember(QName c, List<QName> e) {
	// TODO Auto-generated method stub
	return null;
    }
    @Override
    public Document newDocument(Hashtable<String, String> namespaces,
				Collection<Statement> statements,
				Collection<NamedBundle> bundles) {
        String s = keyword("document") + breakline();
        if (namespaces != null) {
            if (namespaces instanceof Hashtable) {
                Hashtable<String, String> nss = (Hashtable<String, String>) namespaces;
                // FIXME TODO: Should not be getting blank keys here.
                if (nss.containsKey("")) {
                    nss.put("_", nss.get(""));
                    nss.remove("");
                }
                String def;
                if ((def=nss.get("_"))!=null) {
                    s = s + convertDefaultNamespace("<" + def + ">") + breakline();
                }
                 
                for (String key : nss.keySet()) {
                    String uri = nss.get(key);
                    if (key.equals("_")) {
                       // IGNORE, we have just handled it
                    } else {
                        s = s + convertNamespace(key, "<" + uri + ">")
                                + breakline();
                    }
                }

            } else {
                s = s + namespaces + breakline();
            }
        }
        for (Statement o : statements) {
            s = s + o + breakline();
        }
        if (bundles != null) {
            for (Object o : bundles) {
                s = s + o + breakline();
            }
        }
        s = s + keyword("endDocument");
	
        write(s);
	return null;
    }
    @Override
    public NamedBundle newNamedBundle(QName id,
				      Hashtable<String, String> namespaces,
				      Collection<Statement> statements) {
	// TODO Auto-generated method stub
	return null;
    }
    
    public String optionalAttributes(List<Attribute> attrs) {
	if ((attrs==null) || (attrs.isEmpty())) {
	    return "";
	}
	StringBuffer sb=new StringBuffer();
	boolean first=true;
	for (Attribute attr: attrs) {
	    if (first) {
		sb.append(symbol(",[")+ attr.toNotationString());
		first=false;
	    } else {
		sb.append(symbol(",")+ " " + attr.toNotationString());
	    }
	}
	if (!first) sb.append(symbol("]"));
	return sb.toString();
    }
     
    private String oldOptionalAttributes(Object attrs) {
        String s_attrs=(String)attrs;
        if ("".equals(s_attrs)) {
            return "";
        } else {
            return symbol(",[") + attrs + symbol("]");
        }
    }
    private String symbol(String s) {
	return s;
    }
    private String showprefix(String s) {
	return s;
    }


    private String optionalTime(Object time) {
        return ((time==null)? "" : (", " + time));
    }            

    private Object optional2(Object str) {
        return ((str==null)? "" : str);
    }


    private Object oldOptional(Object str) {
        return ((str==null)? "-" : str);
    }

    private Object convertActivity(Object id,Object startTime,Object endTime, Object aAttrs) {
        String s=keyword("activity") + "(" + id + "," + oldOptional(startTime) + "," + oldOptional(endTime) + oldOptionalAttributes(aAttrs) + ")";
        return s;
    }
    private Object convertEntity(Object id, Object attrs) {
        String s=keyword("entity") + "(" + id  + oldOptionalAttributes(attrs) + ")";
        return s;
    }
    private Object convertAgent(Object id, Object attrs) {
        String s=keyword("agent") + "(" + id  + oldOptionalAttributes(attrs) + ")";
        return s;
    }
    private String keyword(String s) {
        return s;
    }
    private String breakline() {
        return "\n";
    }
    private String showuri(String s) {
        return  s;
    }

    private Object convertDocument(Object namespaces, List<Object> records,
                                  List<Object> bundles) {
        String s = keyword("document") + breakline();
        if (namespaces != null) {
            if (namespaces instanceof Hashtable) {
                Hashtable<String, String> nss = (Hashtable<String, String>) namespaces;
                // FIXME TODO: Should not be getting blank keys here.
                if (nss.containsKey("")) {
                    nss.put("_", nss.get(""));
                    nss.remove("");
                }
                String def;
                if ((def=nss.get("_"))!=null) {
                    s = s + convertDefaultNamespace("<" + def + ">") + breakline();
                }
                 
                for (String key : nss.keySet()) {
                    String uri = nss.get(key);
                    if (key.equals("_")) {
                       // IGNORE, we have just handled it
                    } else {
                        s = s + convertNamespace(key, "<" + uri + ">")
                                + breakline();
                    }
                }

            } else {
                s = s + namespaces + breakline();
            }
        }
        for (Object o : records) {
            s = s + o + breakline();
        }
        if (bundles != null) {
            for (Object o : bundles) {
                s = s + o + breakline();
            }
        }
        s = s + keyword("endDocument");
        return s;
    }

    private Object convertBundle(Object id, Object namespaces, List<Object> records) {
        String s="bundle " + id + breakline();
	if (namespaces!=null) {
	    s=s+namespaces + breakline();
	}
	if (records!=null) 
	    for (Object o: records) {
		s=s+o+breakline();
	    }
        s=s+"endBundle";
        return s;
    }

    private void startBundle(Object bundleId) {
    }

    private Object convertAttributes(List<Object> attributes) {
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
    private Object convertId(String id) {
        return id;
    }


    private Object convertAttribute(Object name, Object value) {
	return name + "=" + value;
    }
    private Object convertStart(String start) {
        return start;
    }
    private Object convertEnd(String end) {
        return end;
    }
    private Object convertString(String s) {
        return s;
    }

    private Object convertString(String s, String lang) {
        return s + "@" + lang;
    }

    private Object convertInt(int i) {
        return i;
    }

    private String optionalId(Object id) {
        return ((id==null)? "" : (id + ";"));
    }            

    private Object convertUsed(Object id, Object id2,Object id1, Object time, Object aAttrs) {
        String s=keyword("used") + "(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," +
            oldOptional(time) + oldOptionalAttributes(aAttrs) + ")";
        return s;
    }
    private Object convertWasGeneratedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s=keyword("wasGeneratedBy") + "(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," +
            oldOptional(time) + oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }
    private Object convertWasStartedBy(Object id, Object id2,Object id1, Object id3, Object time, Object aAttrs ) {
        String s="wasStartedBy(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," + oldOptional(id3) + "," +
            oldOptional(time) + oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }
    private Object convertWasEndedBy(Object id, Object id2,Object id1, Object id3, Object time, Object aAttrs ) {
        String s="wasEndedBy(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," + oldOptional(id3) + "," +
            oldOptional(time) + oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }

    private Object convertWasInformedBy(Object id, Object id2, Object id1, Object aAttrs) {
        String s="wasInformedBy(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1)
            + oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }


    private Object convertWasInvalidatedBy(Object id, Object id2,Object id1, Object time, Object aAttrs ) {
        String s=keyword("wasInvalidatedBy") + "(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," +
            oldOptional(time) + oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }


    private Object convertWasAttributedTo(Object id, Object id2,Object id1, Object aAttrs ) {
        String s=keyword("wasAttributedTo") + "(" + optionalId(id) + oldOptional(id2) + ", " + oldOptional(id1) +
            oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }


    private Object convertWasDerivedFrom(Object id, Object id2,Object id1, Object pe, Object g2, Object u1, Object aAttrs) {
        String s=keyword("wasDerivedFrom") + "(" + optionalId(id) + oldOptional(id2) + ", " + oldOptional(id1) + 
            ((pe==null && g2==null && u1==null) ?
             "" : ", " + oldOptional(pe) + ", " + oldOptional(g2) + ", " + oldOptional(u1)) + oldOptionalAttributes(aAttrs) +  ")";
        return s;
    }

    
    private Object convertWasInfluencedBy(Object id, Object id2, Object id1, Object dAttrs) {
        String s="wasInfluencedBy(" + optionalId(id) + oldOptional(id2) + ", " + oldOptional(id1) + oldOptionalAttributes(dAttrs) +  ")";
        return s;
    }


    private Object convertAlternateOf(Object id2,Object id1) {
        String s="alternateOf(" + id2 + "," + id1 + ")";
        return s;
    }

    private Object convertSpecializationOf(Object id2, Object id1) {
        String s="specializationOf(" + id2 + "," + id1 + ")";
        return s;
    }

    private Object convertMentionOf(Object su, Object bu, Object ta) {
        String s="mentionOf(" + su + ", " + bu + ", " + oldOptional(ta) + ")";
        return s;
    }

    private Object convertWasAssociatedWith(Object id, Object id2,Object id1, Object pl, Object aAttrs) {
        String s=keyword("wasAssociatedWith") + "(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," +
            oldOptional(pl) +
            oldOptionalAttributes(aAttrs) + ")";
        return s;
    }

    private Object convertActedOnBehalfOf(Object id, Object id2,Object id1, Object a, Object aAttrs) {
        String s=keyword("actedOnBehalfOf") + "(" + optionalId(id) + oldOptional(id2) + "," + oldOptional(id1) + "," +
            oldOptional(a) +
            oldOptionalAttributes(aAttrs) + ")";
        return s;
    }

    private Object convertHadMember(Object collection, Object entity) {
	String s=keyword("hadMember") + "(" + oldOptional(collection) + "," + oldOptional(entity) + ")";
	return s;
    }


    private Object convertExtension(Object name, Object id, Object args, Object dAttrs) {
	System.out.println("Name @" + name);
	System.out.println("Name @" + id);
	System.out.println("Name @" + args);
	System.out.println("Name @" + dAttrs);
        String s=keyword((String)name) + "(" + optionalId(id) + args +
            oldOptionalAttributes(dAttrs) + ")";
	return s;
    }

    private Object convertQualifiedName(String qname) {	
        return qname;
    }
    private Object convertIRI(String iri) {
        return iri;
    }
    //TODO should not use xsd:QName

    private Object convertTypedLiteral(String datatype, Object value) {
        if ("xsd:QName".equals(datatype)) {
            String val=(String)value;
            return "'" + val.substring(1, val.length() -1 ) + "'";
        } else {
	    if ("prov:Qualified_Name".equals(datatype)) {
		String val=(String)value;
		return "'" + val.substring(1, val.length() -1 ) + "'";
	    } else {
		if (value instanceof InternationalizedString) {
		    InternationalizedString is=(InternationalizedString) value;
		    value=convertInternationalizedString(is);
		}
		return value + " %% " + datatype;
	    }
	}
    }


    private Object convertInternationalizedString (InternationalizedString is) {
	String value;
	String lang=is.getLang();
	if (lang==null) {
	    value= "\""+ is.getValue() + "\"";
	} else { 
	    value= "\""+ is.getValue()+"\""+"@"+lang;
	}
	return value;
    }


   private Object convertNamespace(Object pre, Object iri) {
       return keyword("prefix") + " " + showprefix((String)pre) + " " + showuri((String)iri);
   }

   private Object convertDefaultNamespace(Object iri) {
       return  keyword("default") + " " + showuri((String)iri);
   }

    private Object convertNamespaces(List<Object> namespaces) {
        String s="";
        for (Object o: namespaces) {
            s=s+o+breakline();
        }
        return s;
    }

    private Object convertPrefix(String pre) {
        return pre;
    }

    /* Component 5 */

    private Object convertInsertion(Object id, Object id2, Object id1, Object kes, Object iAttrs) {
        String s="derivedByInsertionFrom(" + optionalId(id) + id2 + ", " + id1 + ", "
	    + kes + oldOptionalAttributes(iAttrs) +  ")";
	return s;
    }

    private Object convertRemoval(Object id, Object id2, Object id1, Object keyset, Object rAttrs) {
        String s="derivedByRemovalFrom(" + optionalId(id) + id2 + ", " + id1 + ", "
	    + keyset + oldOptionalAttributes(rAttrs) +  ")";
	return s;

    }

    private Object convertCollectionMemberOf(Object id, Object id2, Object es, Object complete, Object mAttrs) {

        String s="memberOf(" + optionalId(id) + id2 + ", "
	    + es + ((complete==null)? "" : ", "+complete) + oldOptionalAttributes(mAttrs) +  ")";
	return s;

    }


    private Object convertDictionaryMemberOf(Object id, Object id2, Object kes, Object complete, Object mAttrs) {

        String s="memberOf(" + optionalId(id) + id2 + ", "
	    + kes + ((complete==null)? "" : ", "+complete) + oldOptionalAttributes(mAttrs) +  ")";
	return s;

    }

    private Object convertEntry(Object o1, Object o2) {
        String s="{" + o1 + ", " + o2 + "}";
	return s;
    }

    private Object convertKeyEntitySet(List<Object> entries) {
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


    private Object convertKeys(List<Object> keys) {
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
