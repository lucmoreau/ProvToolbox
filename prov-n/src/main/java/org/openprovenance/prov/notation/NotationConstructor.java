package org.openprovenance.prov.notation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.model.KeyQNamePair;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.QNameExport;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.xml.UncheckedException;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.ValueConverter;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

/** For testing purpose, conversion back to ASN. */

public class NotationConstructor implements ModelConstructor {
    
    public static final String MARKER = "-";
    final private QNameExport qnExport;
    final private BufferedWriter buffer;

    public NotationConstructor(Writer writer, QNameExport qnExport) {
	this.buffer=new BufferedWriter(writer);
	this.qnExport=qnExport;
    }
    
    public boolean standaloneExpression=false;
       
    public void write(String s) {
        try {
            buffer.write(s);
        } catch (IOException e) {
            throw new UncheckedException("NotationConstructor.write() failed", e);
        }
    }
    
    public void writeln(String s) {
        try {
            buffer.write(s);
            if (!standaloneExpression)
            buffer.newLine();
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
        return ((qn==null)? MARKER : qnExport.qnameToString(qn));
    }

    public String timeOrMarker(XMLGregorianCalendar time) {
        return ((time==null)? MARKER : time.toString());
    }

    private String optionalId(QName id) {
        return ((id==null)? "" : (qnExport.qnameToString(id) + ";"));
    }            

    
    @Override
    public Entity newEntity(QName id, Collection<Attribute> attributes) {
        String s=keyword("entity") + "(" + idOrMarker(id)  + optionalAttributes(attributes) + ")";
        writeln(s);
	return null;
    }
    @Override
    public Activity newActivity(QName id, XMLGregorianCalendar startTime,
				XMLGregorianCalendar endTime,
				Collection<Attribute> attributes) {
        String s=keyword("activity") + "(" + idOrMarker(id) + "," + timeOrMarker(startTime) + "," + timeOrMarker(endTime) + optionalAttributes(attributes) + ")";
        writeln(s);
	return null;
    }
    @Override
    public Agent newAgent(QName id, Collection<Attribute> attributes) {
        String s=keyword("agent") + "(" + idOrMarker(id)  + optionalAttributes(attributes) + ")";
        writeln(s);
	return null;
    }
    @Override
    public Used newUsed(QName id, QName activity, QName entity,
			XMLGregorianCalendar time, Collection<Attribute> attributes) {
        String s=keyword("used") + "(" + optionalId(id) + idOrMarker(activity) + "," + idOrMarker(entity) + "," +
                timeOrMarker(time) + optionalAttributes(attributes) + ")";
        writeln(s);
	return null;
    }
    @Override
    public WasGeneratedBy newWasGeneratedBy(QName id, QName entity,
					    QName activity,
					    XMLGregorianCalendar time,
					    Collection<Attribute> attributes) {
        String s=keyword("wasGeneratedBy") + "(" + optionalId(id) + idOrMarker(entity) + "," + idOrMarker(activity) + "," +
                timeOrMarker(time) + optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity,
						QName activity,
						XMLGregorianCalendar time,
						Collection<Attribute> attributes) {
        String s=keyword("wasInvalidatedBy") + "(" + optionalId(id) + idOrMarker(entity) + "," + idOrMarker(activity) + "," +
                timeOrMarker(time) + optionalAttributes(attributes) +  ")";
        writeln(s);
        return null;
    }
    @Override
    public WasStartedBy newWasStartedBy(QName id, QName activity,
					QName trigger, QName starter,
					XMLGregorianCalendar time,
					Collection<Attribute> attributes) {
        String s="wasStartedBy(" + optionalId(id) + idOrMarker(activity) + "," + idOrMarker(trigger) + "," + idOrMarker(starter) + "," +
                timeOrMarker(time) + optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger,
				    QName ender, XMLGregorianCalendar time,
				    Collection<Attribute> attributes) {
        String s="wasEndedBy(" + optionalId(id) + idOrMarker(activity) + "," + idOrMarker(trigger) + "," + idOrMarker(ender) + "," +
                timeOrMarker(time) + optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1,
					    QName activity, QName generation,
					    QName usage,
					    Collection<Attribute> attributes) {
        String s=keyword("wasDerivedFrom") + "(" + optionalId(id) + idOrMarker(e2) + ", " + idOrMarker(e1) + 
                ((activity==null && generation==null && usage==null) ?
                 "" : ", " + idOrMarker(activity) + ", " + idOrMarker(generation) + ", " + idOrMarker(usage)) + optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag,
						  QName plan,
						  Collection<Attribute> attributes) {
        String s=keyword("wasAssociatedWith") + "(" + optionalId(id) + idOrMarker(a) + "," + idOrMarker(ag) + "," +
                idOrMarker(plan) +
                optionalAttributes(attributes) + ")";        
        writeln(s);
        return null;
    }
    @Override
    public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,
					      Collection<Attribute> attributes) {
        String s=keyword("wasAttributedTo") + "(" + optionalId(id) + idOrMarker(e) + ", " + idOrMarker(ag) +
                optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1,
					      QName a,
					      Collection<Attribute> attributes) {
        String s=keyword("actedOnBehalfOf") + "(" + optionalId(id) + idOrMarker(ag2) + "," + idOrMarker(ag1) + "," +
                idOrMarker(a) +
                optionalAttributes(attributes) + ")";
        writeln(s);
	return null;
    }
    
    @Override
    public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1,
					  Collection<Attribute> attributes) {
        String s="wasInformedBy(" + optionalId(id) + idOrMarker(a2) + "," + idOrMarker(a1)
                + optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1,
					      Collection<Attribute> attributes) {
        String s="wasInfluencedBy(" + optionalId(id) + idOrMarker(a2) + "," + idOrMarker(a1)
                + optionalAttributes(attributes) +  ")";
        writeln(s);
	return null;
    }
    @Override
    public AlternateOf newAlternateOf(QName e2, QName e1) {
	writeln("alternateOf(" + idOrMarker(e2) + "," + idOrMarker(e1) + ")");
	return null;
   }
    @Override
    public SpecializationOf newSpecializationOf(QName e2, QName e1) {
	writeln("specializationOf(" + idOrMarker(e2) + "," + idOrMarker(e1) + ")");
	return null;
    }
    @Override
    public MentionOf newMentionOf(QName e2, QName e1, QName b) {
        String s="mentionOf(" + idOrMarker(e2) + ", " + idOrMarker(e1) + ", " + idOrMarker(b) + ")";
        writeln(s);
	return null;
    }
    @Override
    public HadMember newHadMember(QName c, Collection<QName> ll) {
	if ((ll==null) || (ll.size()==0)) { 
	    // strictly speaking it is not a syntactically correct expression, but we print something to support scruffiness
	    String s=keyword("hadMember") + "(" + idOrMarker(c) + "," + idOrMarker(null) + ")";
	    writeln(s);
	} else {
	    for (QName e: ll) {
		String s=keyword("hadMember") + "(" + idOrMarker(c) + "," + idOrMarker(e) + ")";
		writeln(s);
	    }
	}
	return null;
    }
    
    @Override
    public void startDocument(Hashtable<String, String> namespaces) {
        String s = keyword("document") + breakline();
        s = s+ processNamespaces(namespaces);
        write(s);
    }


    public String processNamespaces(Hashtable<String, String> namespaces) {
        String s="";
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
        return s;
    }
    


    public Object convertNamespace(Object pre, Object iri) {
        return keyword("prefix") + " " + showprefix((String)pre) + " " + showuri((String)iri);
    }

    public Object convertDefaultNamespace(Object iri) {
        return  keyword("default") + " " + showuri((String)iri);
    }


    public String showuri(String s) {
        return  s;
    }


    
    @Override
    public Document newDocument(Hashtable<String, String> namespaces,
				Collection<Statement> statements,
				Collection<NamedBundle> bundles) {
        String s="";
        
        s = s + keyword("endDocument");
	
        write(s);
	return null;
    }
    

    @Override
    public void startBundle(QName bundleId, Hashtable<String, String> namespaces) {
        String s = keyword("bundle") + " " + qnExport.qnameToString(bundleId);
        s = s+ processNamespaces(namespaces);
        writeln(s);
 
    }
    
    
    
    @Override
    public NamedBundle newNamedBundle(QName id,
				      Hashtable<String, String> namespaces,
				      Collection<Statement> statements) {
        String s="";    
        s = s + keyword("endBundle");
        writeln(s);
	return null;
    }
    
    public String optionalAttributes(Collection<Attribute> attrs) {
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
     
    public String symbol(String s) {
	return s;
    }
    public String showprefix(String s) {
	return s;
    }


    
    public String keyword(String s) {
        return s;
    }
    public String breakline() {
        return "\n";
    }
    
  
   
    //TODO
    public Object convertExtension(Object name, QName id, Object args, Object dAttrs) {
	System.out.println("Name @" + name);
	System.out.println("Name @" + id);
	System.out.println("Name @" + args);
	System.out.println("Name @" + dAttrs);
  //      String s=keyword((String)name) + "(" + oldOptionalId(id) + args +
  //          oldOptionalAttributes(dAttrs) + ")";
  //	return s;
	return null;
    }

	@Override
	public DerivedByInsertionFrom newDerivedByInsertionFrom(QName id,
			QName after, QName before, List<KeyQNamePair> kes, Collection<Attribute> attributes) {
	    
	    String s="prov:derivedByInsertionFrom(" + optionalId(id) + idOrMarker(after) + "," + idOrMarker(before)
		    + "," + keyEntitySet(kes)
		                + optionalAttributes(attributes) +  ")";
	    writeln(s);
	    return null;
	}
	
	static ValueConverter vc=new ValueConverter(ProvFactory.getFactory());

	private String keyEntitySet(List<KeyQNamePair> kes) {
	    String s="{";
	    if (kes!=null) {
		boolean first=true;
		for (KeyQNamePair p: kes) {
		    if (!first) s=s+", ";
		    first=false;
		    s= s + "(" + org.openprovenance.prov.xml.Attribute.valueToNotationString(p.key,vc.getXsdType(p.key)) + ", " + idOrMarker(p.name) + ")";
		}
	    }
	    s=s+"}";
	    return s;
	}
	private String keySet(List<Object> ks) {
	    String s="{";
	    if (ks!=null) {
		boolean first=true;
		for (Object k: ks) {
		    if (!first) s=s+", ";
		    first=false;
		    s= s +  org.openprovenance.prov.xml.Attribute.valueToNotationString(k,vc.getXsdType(k));
		}
	    }
	    s=s+"}";
	    return s;
	}

	@Override
	public DerivedByRemovalFrom newDerivedByRemovalFrom(QName id,
							    QName after,
							    QName before,
							    List<Object> keys,
							    Collection<Attribute> attributes) {
	    String s="prov:derivedByRemovalFrom(" + optionalId(id) + idOrMarker(after) + "," + idOrMarker(before)
		    + "," + keySet(keys)
	                + optionalAttributes(attributes) +  ")";	    
	    writeln(s);
	    return null;
	}

	boolean abbrev=false;
	@Override
	public DictionaryMembership newDictionaryMembership(QName dict,
							    List<KeyQNamePair> keyEntitySet) {
		if (abbrev) {
			String s="provx:hadDictionaryMember(" +  idOrMarker(dict)  
				    + "," + keyEntitySet(keyEntitySet) +  ")";
			    writeln(s);
		} else {
			for (KeyQNamePair entry: keyEntitySet) {
				
				String s="prov:hadDictionaryMember(" +   idOrMarker(dict) + "," + idOrMarker(entry.name)  						
						 + "," +  org.openprovenance.prov.xml.Attribute.valueToNotationString(entry.key,vc.getXsdType(entry.key)) + ")";
			    writeln(s);	
			}
		}
	    return null;
	}




}
