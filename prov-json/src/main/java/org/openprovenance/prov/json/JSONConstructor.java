/**
 * 
 */
package org.openprovenance.prov.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.openprovenance.prov.xml.ActedOnBehalfOf;
import org.openprovenance.prov.xml.Activity;
import org.openprovenance.prov.xml.Agent;
import org.openprovenance.prov.xml.AlternateOf;
import org.openprovenance.prov.xml.Attribute;
import org.openprovenance.prov.xml.Document;
import org.openprovenance.prov.xml.Entity;
import org.openprovenance.prov.xml.HadMember;
import org.openprovenance.prov.xml.InternationalizedString;
import org.openprovenance.prov.xml.MentionOf;
import org.openprovenance.prov.xml.ModelConstructor;
import org.openprovenance.prov.xml.NamedBundle;
import org.openprovenance.prov.xml.QNameExport;
import org.openprovenance.prov.xml.SpecializationOf;
import org.openprovenance.prov.xml.Statement;
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

/**
 * @author Trung Dong Huynh
 * 
 * Constructing JSON-friendly structure from a Document
 *
 */
public class JSONConstructor implements ModelConstructor {
	private class JsonProvRecord {
		String type;
		Object id;
		List<Object[]>  attributes;
		
		public JsonProvRecord(String type, QName id, List<Object[]>  attributes) {
			this.type = type;
			this.id = id;
			this.attributes = attributes;
		}
	}

	final private List<JsonProvRecord> records = new ArrayList<JsonProvRecord>();
	final private QNameExport qnExport;

	public JSONConstructor(QNameExport qnExport) {
		this.qnExport = qnExport;
	}
	
	
	public Object getJSONStructure() {
		// TODO Auto-generated method stub
		return null;
	}

	private static final Map<String,Integer> countMap = new HashMap<String, Integer>();
	
	private static String getBlankID(String type) {
		if (!countMap.containsKey(type)) {
			countMap.put(type, 0);
		}
		int count = countMap.get(type);
		count += 1;
		countMap.put(type, count);
		return "_:" + type + count;
	}

	private Object[] tuple(Object o1, Object o2) {
		Object[] tuple = {o1, o2};
        return tuple;
	}
	
	private Object typedLiteral(String value, String datatype, String lang) {
		// TODO: Converting default types to JSON primitives
		if (datatype == "xsd:string" && lang == null) return value;
		if (datatype == "xsd:double") return Double.parseDouble(value);
		if (datatype == "xsd:int") return Integer.parseInt(value);
		if (datatype == "xsd:boolean") return Boolean.parseBoolean(value);
		
		Map<String, String> result = new HashMap<String, String>();
		result.put("$", value);
		if (datatype != null) {
			result.put("type", datatype);
		}
		if (lang != null) {
			result.put("lang", lang);
		}
		return result;
	}
	
	private Object[] convertAttribute(Attribute attr) {
		String attrName = qnExport.qnameToString(attr.getElementName());
		Object value = attr.getValue();
		Object attrValue;
		if (value instanceof QName) {
			// force type xsd:QName
			attrValue = typedLiteral(qnExport.qnameToString((QName)value), "xsd:QName", null);
		}
		else if (value instanceof InternationalizedString) {
			InternationalizedString iStr = (InternationalizedString)value;
			String lang = iStr.getLang(); 
			if (lang != null) {
				// If 'lang' is defined
				attrValue = typedLiteral(iStr.getValue(), "xsd:string", lang); 
			}
			else {
				// Otherwise, just return the string
				attrValue = iStr.getValue();
			}
		}
		else {
			String datatype = qnExport.qnameToString(attr.getXsdType());
			attrValue = typedLiteral((String)value, datatype, null);
		}
		return tuple(attrName, attrValue);
	}
	
	private List<Object[]> convertAttributes(List<Attribute> attrs) {
		List<Object[]> result = new ArrayList<Object[]>();
		for (Attribute attr: attrs) {
			result.add(convertAttribute(attr));
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newEntity(javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public Entity newEntity(QName id, List<Attribute> attributes) {
		List<Object[]> attrs = convertAttributes(attributes);
		JsonProvRecord record = new JsonProvRecord("entity", id, attrs);
		this.records.add(record);
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newActivity(javax.xml.namespace.QName, javax.xml.datatype.XMLGregorianCalendar, javax.xml.datatype.XMLGregorianCalendar, java.util.List)
	 */
	@Override
	public Activity newActivity(QName id, XMLGregorianCalendar startTime,
			XMLGregorianCalendar endTime, List<Attribute> attributes) {
		if (startTime != null || endTime !=null) {
			List<Attribute> attrs = new ArrayList<Attribute>(attributes);
			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newAgent(javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public Agent newAgent(QName id, List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newUsed(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.datatype.XMLGregorianCalendar, java.util.List)
	 */
	@Override
	public Used newUsed(QName id, QName activity, QName entity,
			XMLGregorianCalendar time, List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasGeneratedBy(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.datatype.XMLGregorianCalendar, java.util.List)
	 */
	@Override
	public WasGeneratedBy newWasGeneratedBy(QName id, QName entity,
			QName activity, XMLGregorianCalendar time,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasInvalidatedBy(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.datatype.XMLGregorianCalendar, java.util.List)
	 */
	@Override
	public WasInvalidatedBy newWasInvalidatedBy(QName id, QName entity,
			QName activity, XMLGregorianCalendar time,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasStartedBy(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.datatype.XMLGregorianCalendar, java.util.List)
	 */
	@Override
	public WasStartedBy newWasStartedBy(QName id, QName activity,
			QName trigger, QName starter, XMLGregorianCalendar time,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasEndedBy(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.datatype.XMLGregorianCalendar, java.util.List)
	 */
	@Override
	public WasEndedBy newWasEndedBy(QName id, QName activity, QName trigger,
			QName ender, XMLGregorianCalendar time, List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasDerivedFrom(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public WasDerivedFrom newWasDerivedFrom(QName id, QName e2, QName e1,
			QName activity, QName generation, QName usage,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasAssociatedWith(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public WasAssociatedWith newWasAssociatedWith(QName id, QName a, QName ag,
			QName plan, List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasAttributedTo(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public WasAttributedTo newWasAttributedTo(QName id, QName e, QName ag,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newActedOnBehalfOf(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public ActedOnBehalfOf newActedOnBehalfOf(QName id, QName ag2, QName ag1,
			QName a, List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasInformedBy(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public WasInformedBy newWasInformedBy(QName id, QName a2, QName a1,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newWasInfluencedBy(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public WasInfluencedBy newWasInfluencedBy(QName id, QName a2, QName a1,
			List<Attribute> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newAlternateOf(javax.xml.namespace.QName, javax.xml.namespace.QName)
	 */
	@Override
	public AlternateOf newAlternateOf(QName e2, QName e1) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newSpecializationOf(javax.xml.namespace.QName, javax.xml.namespace.QName)
	 */
	@Override
	public SpecializationOf newSpecializationOf(QName e2, QName e1) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newMentionOf(javax.xml.namespace.QName, javax.xml.namespace.QName, javax.xml.namespace.QName)
	 */
	@Override
	public MentionOf newMentionOf(QName e2, QName e1, QName b) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newHadMember(javax.xml.namespace.QName, java.util.List)
	 */
	@Override
	public HadMember newHadMember(QName c, List<QName> e) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newDocument(java.util.Hashtable, java.util.Collection, java.util.Collection)
	 */
	@Override
	public Document newDocument(Hashtable<String, String> namespaces,
			Collection<Statement> statements, Collection<NamedBundle> bundles) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#newNamedBundle(javax.xml.namespace.QName, java.util.Hashtable, java.util.Collection)
	 */
	@Override
	public NamedBundle newNamedBundle(QName id,
			Hashtable<String, String> namespaces,
			Collection<Statement> statements) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#startDocument(java.util.Hashtable)
	 */
	@Override
	public void startDocument(Hashtable<String, String> hashtable) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.openprovenance.prov.xml.ModelConstructor#startBundle(javax.xml.namespace.QName, java.util.Hashtable)
	 */
	@Override
	public void startBundle(QName bundleId, Hashtable<String, String> namespaces) {
		// TODO Auto-generated method stub

	}
}